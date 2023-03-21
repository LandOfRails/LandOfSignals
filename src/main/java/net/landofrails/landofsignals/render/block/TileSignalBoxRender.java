package net.landofrails.landofsignals.render.block;

import cam72cam.mod.ModCore;
import cam72cam.mod.math.Vec3d;
import cam72cam.mod.model.obj.OBJModel;
import cam72cam.mod.render.OpenGL;
import cam72cam.mod.render.StandardModel;
import cam72cam.mod.render.obj.OBJRender;
import cam72cam.mod.resource.Identifier;
import net.landofrails.api.contentpacks.v2.parent.ContentPackBlock;
import net.landofrails.api.contentpacks.v2.parent.ContentPackModel;
import net.landofrails.api.contentpacks.v2.signalbox.ContentPackSignalbox;
import net.landofrails.landofsignals.LOSBlocks;
import net.landofrails.landofsignals.LandOfSignals;
import net.landofrails.landofsignals.tile.TileSignalBox;
import net.landofrails.landofsignals.utils.Static;
import org.lwjgl.opengl.GL11;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class TileSignalBoxRender {

    private TileSignalBoxRender() {

    }

    private static final Map<String, OBJRender> cache = new HashMap<>();
    private static final Map<String, List<String>> groupCache = new HashMap<>();

    private static void checkCache(String blockId, Map<String, ContentPackModel[]> models) {
        Optional<String> firstPath = models.keySet().stream().findFirst();
        if (!firstPath.isPresent())
            return;
        final String firstObjId = blockId + "/" + firstPath.get();
        if (cache.containsKey(firstObjId)) {
            return;
        }

        for (Map.Entry<String, ContentPackModel[]> modelEntry : models.entrySet()) {
            try {
                final String path = modelEntry.getKey();
                final String objId = blockId + "/" + path;

                Set<String> objTextures = LOSBlocks.BLOCK_SIGNAL_BOX.getContentpackSignalboxes().get(blockId).getObjTextures().get(path);
                OBJRender renderer = new OBJRender(new OBJModel(new Identifier(LandOfSignals.MODID, path), 0, objTextures));
                cache.putIfAbsent(objId, renderer);

                for (ContentPackModel signalboxModel : modelEntry.getValue()) {
                    String[] groups = signalboxModel.getObj_groups();
                    if (groups.length > 0) {
                        Predicate<String> targetGroup = renderOBJGroup -> Arrays.stream(groups).anyMatch(renderOBJGroup::startsWith);
                        List<String> modes = renderer.model.groups().stream().filter(targetGroup)
                                .collect(Collectors.toCollection(ArrayList::new));
                        String groupCacheId = objId + "@" + String.join("+", groups);
                        groupCache.put(groupCacheId, modes);
                    }
                }
            } catch (Exception e) {
                String message = String.format("Couldn't cache the following: blockId: %s", blockId);
                throw new BlockRenderException(message, e);
            }
        }

    }

    public static StandardModel render(final TileSignalBox tsp) {
        return new StandardModel().addCustom(() -> renderStuff(tsp));
    }

    private static void renderStuff(final TileSignalBox tsp) {

        String id = tsp.getId();

        if (id == null) {
            id = Static.MISSING;
        }

        renderBase(id, tsp);

    }

    private static void renderBase(final String blockId, final TileSignalBox tile) {

        final ContentPackSignalbox contentPackSignalboxes = LOSBlocks.BLOCK_SIGNAL_BOX.getContentpackSignalboxes().get(blockId);
        checkCache(blockId, contentPackSignalboxes.getBase());
        for (Map.Entry<String, ContentPackModel[]> baseModels : contentPackSignalboxes.getBase().entrySet()) {

            final String path = baseModels.getKey();

            final String objId = blockId + "/" + path;
            final OBJRender renderer = cache.get(objId);

            for (ContentPackModel baseModel : baseModels.getValue()) {
                final ContentPackBlock block = baseModel.getBlock();
                final Vec3d translate = block.getAsVec3d(block::getTranslation);
                final Vec3d scale = block.getAsVec3d(block::getScaling);
                final Vec3d rotation = block.getAsVec3d(block::getRotation);

                try (OpenGL.With ignored1 = OpenGL.matrix(); OpenGL.With ignored2 = renderer.bindTexture(baseModel.getTextures())) {

                    // Render
                    GL11.glScaled(scale.x, scale.y, scale.z);
                    GL11.glTranslated(translate.x, translate.y, translate.z);
                    GL11.glRotated(rotation.x, 1, 0, 0);
                    GL11.glRotated(tile.getBlockRotate() + rotation.y, 0, 1, 0);
                    GL11.glRotated(rotation.z, 0, 0, 1);

                    String[] groups = baseModel.getObj_groups();
                    if (groups.length == 0) {
                        renderer.draw();
                    } else {
                        String groupCacheId = objId + "@" + String.join("+", groups);
                        renderer.drawGroups(groupCache.get(groupCacheId));
                    }

                } catch (Exception e) {
                    // Removes TileEntity on client-side, prevents crash
                    ModCore.error("Removing local Signalbox- (x%d, y%d, z%d) due to exceptions: %s", tile.getPos().x, tile.getPos().y, tile.getPos().z, e.getMessage());
                    tile.getWorld().breakBlock(tile.getPos());

                }

            }
        }
    }

}
