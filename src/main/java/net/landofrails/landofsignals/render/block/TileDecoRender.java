package net.landofrails.landofsignals.render.block;

import cam72cam.mod.ModCore;
import cam72cam.mod.math.Vec3d;
import cam72cam.mod.model.obj.OBJModel;
import cam72cam.mod.render.StandardModel;
import cam72cam.mod.render.obj.OBJRender;
import cam72cam.mod.render.opengl.RenderState;
import cam72cam.mod.resource.Identifier;
import net.landofrails.api.contentpacks.v2.deco.ContentPackDeco;
import net.landofrails.api.contentpacks.v2.parent.ContentPackBlock;
import net.landofrails.api.contentpacks.v2.parent.ContentPackModel;
import net.landofrails.landofsignals.LOSBlocks;
import net.landofrails.landofsignals.LandOfSignals;
import net.landofrails.landofsignals.tile.TileDeco;
import net.landofrails.landofsignals.utils.Static;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class TileDecoRender {

    private TileDecoRender() {

    }

    private static final Map<String, OBJModel> cache = new HashMap<>();
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

                Set<String> objTextures = LOSBlocks.BLOCK_DECO.getContentpackDeco().get(blockId).getObjTextures().get(path);
                // TODO is null okay or should it be replaced with ""?
                OBJModel model = new OBJModel(new Identifier(LandOfSignals.MODID, path), 0, objTextures);
                cache.putIfAbsent(objId, model);

                for (ContentPackModel decoModel : modelEntry.getValue()) {
                    String[] groups = decoModel.getObj_groups();
                    if (groups.length > 0) {
                        Predicate<String> targetGroup = renderOBJGroup -> Arrays.stream(groups).anyMatch(renderOBJGroup::startsWith);
                        List<String> modes = model.groups().stream().filter(targetGroup)
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

    public static StandardModel render(TileDeco tsp) {
        return new StandardModel().addCustom((state, partialTicks) -> renderStuff(tsp, state));
    }

    private static void renderStuff(TileDeco tsp, RenderState state) {

        String id = tsp.getId();

        if (id == null) {
            id = Static.MISSING;
        }

        renderBase(id, tsp, state);

    }

    private static void renderBase(String blockId, TileDeco tile, RenderState state) {

        ContentPackDeco contentPackDeco = LOSBlocks.BLOCK_DECO.getContentpackDeco().get(blockId);

        if(contentPackDeco == null) contentPackDeco = LOSBlocks.BLOCK_DECO.getContentpackDeco().get(Static.MISSING);

        checkCache(blockId, contentPackDeco.getBase());

        for (Map.Entry<String, ContentPackModel[]> baseModels : contentPackDeco.getBase().entrySet()) {

            String path = baseModels.getKey();

            String objId = blockId + "/" + path;
            OBJModel model = cache.get(objId);

            for (ContentPackModel baseModel : baseModels.getValue()) {

                RenderState iterationState = state.clone();

                ContentPackBlock block = baseModel.getBlock();
                Vec3d translate = block.getAsVec3d(block::getTranslation);
                Vec3d scale = block.getAsVec3d(block::getScaling);
                Vec3d rotation = block.getAsVec3d(block::getRotation);

                iterationState.scale(scale);
                iterationState.translate(translate);
                iterationState.rotate(rotation.x,1, 0, 0);
                iterationState.rotate(tile.getBlockRotate() + rotation.y, 0, 1, 0);
                iterationState.rotate(rotation.z, 0, 0, 1);

                try (OBJRender.Binding vbo = model.binder().texture(baseModel.getTextures()).bind(iterationState)) {

                    // Render
                    String[] groups = baseModel.getObj_groups();
                    if (groups.length == 0) {
                        vbo.draw();
                    } else {
                        String groupCacheId = objId + "@" + String.join("+", groups);
                        vbo.draw(groupCache.get(groupCacheId));
                    }

                } catch (Exception e) {
                    // Removes TileEntity on client-side, prevents crash
                    ModCore.error("Removing local TileDeco (x%d, y%d, z%d) due to exceptions: %s", tile.getPos().x, tile.getPos().y, tile.getPos().z, e.getMessage());
                    tile.getWorld().breakBlock(tile.getPos());

                }

            }
        }
    }

}
