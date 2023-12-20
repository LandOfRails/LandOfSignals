package net.landofrails.landofsignals.render.block;

import cam72cam.mod.ModCore;
import cam72cam.mod.math.Vec3d;
import cam72cam.mod.model.obj.OBJModel;
import cam72cam.mod.render.OpenGL;
import cam72cam.mod.render.StandardModel;
import cam72cam.mod.render.obj.OBJRender;
import cam72cam.mod.resource.Identifier;
import net.landofrails.api.contentpacks.v2.lever.ContentPackLever;
import net.landofrails.api.contentpacks.v2.parent.ContentPackBlock;
import net.landofrails.api.contentpacks.v2.parent.ContentPackModel;
import net.landofrails.landofsignals.LOSBlocks;
import net.landofrails.landofsignals.LandOfSignals;
import net.landofrails.landofsignals.tile.TileCustomLever;
import net.landofrails.landofsignals.utils.Static;
import org.lwjgl.opengl.GL11;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class TileCustomLeverRender {

    private TileCustomLeverRender(){

    }

    private static final Map<String, OBJRender> cache = new HashMap<>();
    private static final Map<String, List<String>> groupCache = new HashMap<>();

    public static void checkCache(String blockId, Map<String, ContentPackModel[]> models) {
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

                Set<String> objTextures = LOSBlocks.BLOCK_CUSTOM_LEVER.getContentpackLever().get(blockId).getObjTextures().get(path);
                OBJRender renderer = new OBJRender(new OBJModel(new Identifier(LandOfSignals.MODID, path), 0, objTextures));
                cache.putIfAbsent(objId, renderer);

                for (ContentPackModel decoModel : modelEntry.getValue()) {
                    String[] groups = decoModel.getObj_groups();
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

    public static StandardModel render(TileCustomLever tsp) {
        return new StandardModel().addCustom(() -> renderStuff(tsp));
    }

    private static void renderStuff(TileCustomLever tsp) {

        String id = tsp.getId();

        if (id == null) {
            id = Static.MISSING;
        }

        if(tsp.isActive()) {
            renderActive(id, tsp);
        }else{
            renderInactive(id, tsp);
        }
    }

    private static void renderActive(String blockId, TileCustomLever tile) {
        Vec3d offset = tile.getOffset();
        Vec3d customScaling = tile.getScaling();

        ContentPackLever contentPackLever = LOSBlocks.BLOCK_CUSTOM_LEVER.getContentpackLever().get(blockId);

        if(contentPackLever == null) contentPackLever = LOSBlocks.BLOCK_CUSTOM_LEVER.getContentpackLever().get(Static.MISSING);

        checkCache(blockId, contentPackLever.getActive());

        for (Map.Entry<String, ContentPackModel[]> activeModels : contentPackLever.getActive().entrySet()) {

            String path = activeModels.getKey();

            String objId = blockId + "/" + path;
            OBJRender renderer = cache.get(objId);

            for (ContentPackModel activeModel : activeModels.getValue()) {
                ContentPackBlock block = activeModel.getBlock();
                Vec3d translate = block.getAsVec3d(block::getTranslation).add(offset);
                final Vec3d scale = Static.multiply(block.getAsVec3d(block::getScaling), customScaling);
                Vec3d rotation = block.getAsVec3d(block::getRotation);

                try (OpenGL.With ignored1 = OpenGL.matrix(); OpenGL.With ignored2 = renderer.bindTexture(activeModel.getTextures())) {

                    // Render
                    GL11.glScaled(scale.x, scale.y, scale.z);
                    GL11.glTranslated(translate.x, translate.y, translate.z);
                    GL11.glRotated(rotation.x, 1, 0, 0);
                    GL11.glRotated(tile.getBlockRotate() + rotation.y, 0, 1, 0);
                    GL11.glRotated(rotation.z, 0, 0, 1);

                    String[] groups = activeModel.getObj_groups();
                    if (groups.length == 0) {
                        renderer.draw();
                    } else {
                        String groupCacheId = objId + "@" + String.join("+", groups);
                        renderer.drawGroups(groupCache.get(groupCacheId));
                    }

                } catch (Exception e) {
                    // Removes TileEntity on client-side, prevents crash
                    ModCore.error("Removing local TileCustomLever (x%d, y%d, z%d) due to exceptions: %s", tile.getPos().x, tile.getPos().y, tile.getPos().z, e.getMessage());
                    tile.getWorld().breakBlock(tile.getPos());

                }

            }
        }
    }

    private static void renderInactive(String blockId, TileCustomLever tile) {
        Vec3d offset = tile.getOffset();
        Vec3d customScaling = tile.getScaling();

        ContentPackLever contentPackLever = LOSBlocks.BLOCK_CUSTOM_LEVER.getContentpackLever().get(blockId);

        if(contentPackLever == null) contentPackLever = LOSBlocks.BLOCK_CUSTOM_LEVER.getContentpackLever().get(Static.MISSING);

        checkCache(blockId, contentPackLever.getInactive());

        for (Map.Entry<String, ContentPackModel[]> inactiveModels : contentPackLever.getInactive().entrySet()) {

            String path = inactiveModels.getKey();

            String objId = blockId + "/" + path;
            OBJRender renderer = cache.get(objId);

            for (ContentPackModel inactiveModel : inactiveModels.getValue()) {
                ContentPackBlock block = inactiveModel.getBlock();
                Vec3d translate = block.getAsVec3d(block::getTranslation).add(offset);
                final Vec3d scale = Static.multiply(block.getAsVec3d(block::getScaling), customScaling);
                Vec3d rotation = block.getAsVec3d(block::getRotation);

                try (OpenGL.With ignored1 = OpenGL.matrix(); OpenGL.With ignored2 = renderer.bindTexture(inactiveModel.getTextures())) {

                    // Render
                    GL11.glScaled(scale.x, scale.y, scale.z);
                    GL11.glTranslated(translate.x, translate.y, translate.z);
                    GL11.glRotated(rotation.x, 1, 0, 0);
                    GL11.glRotated(tile.getBlockRotate() + rotation.y, 0, 1, 0);
                    GL11.glRotated(rotation.z, 0, 0, 1);

                    String[] groups = inactiveModel.getObj_groups();
                    if (groups.length == 0) {
                        renderer.draw();
                    } else {
                        String groupCacheId = objId + "@" + String.join("+", groups);
                        renderer.drawGroups(groupCache.get(groupCacheId));
                    }

                } catch (Exception e) {
                    // Removes TileEntity on client-side, prevents crash
                    ModCore.error("Removing local TileCustomLever (x%d, y%d, z%d) due to exceptions: %s", tile.getPos().x, tile.getPos().y, tile.getPos().z, e.getMessage());
                    tile.getWorld().breakBlock(tile.getPos());

                }

            }
        }
    }

}
