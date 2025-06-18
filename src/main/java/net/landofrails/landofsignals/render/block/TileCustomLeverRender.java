package net.landofrails.landofsignals.render.block;

import cam72cam.mod.ModCore;
import cam72cam.mod.math.Vec3d;
import cam72cam.mod.model.obj.OBJModel;
import cam72cam.mod.render.StandardModel;
import cam72cam.mod.render.obj.OBJRender;
import cam72cam.mod.render.opengl.RenderState;
import cam72cam.mod.resource.Identifier;
import net.landofrails.api.contentpacks.v2.lever.ContentPackLever;
import net.landofrails.api.contentpacks.v2.parent.ContentPackBlock;
import net.landofrails.api.contentpacks.v2.parent.ContentPackModel;
import net.landofrails.landofsignals.LOSBlocks;
import net.landofrails.landofsignals.LandOfSignals;
import net.landofrails.landofsignals.tile.TileCustomLever;
import net.landofrails.landofsignals.utils.FlareUtils;
import net.landofrails.landofsignals.utils.Static;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class TileCustomLeverRender {

    private TileCustomLeverRender(){

    }

    private static final Map<String, OBJModel> cache = new HashMap<>();
    private static final Map<String, List<String>> groupCache = new HashMap<>();

    public static void checkCache(String blockId, Map<String, ContentPackModel[]> models, String state) {
        Optional<String> firstPath = models.keySet().stream().findFirst();
        if (!firstPath.isPresent())
            return;
        final String firstObjId = blockId + "/" + firstPath.get() + ":" + state;
        if (cache.containsKey(firstObjId)) {
            return;
        }

        for (Map.Entry<String, ContentPackModel[]> modelEntry : models.entrySet()) {
            try {
                final String path = modelEntry.getKey();
                final String objId = blockId + "/" + path + ":" + state;

                Set<String> objTextures = LOSBlocks.BLOCK_CUSTOM_LEVER.getContentpackLever().get(blockId).getObjTextures().get(path);
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

    public static StandardModel render(TileCustomLever tsp) {
        return new StandardModel().addCustom((renderState, partialTicks) -> renderStuff(renderState, tsp));
    }

    private static void renderStuff(RenderState renderState, TileCustomLever tsp) {

        String id = tsp.getId();

        if (id == null) {
            id = Static.MISSING;
        }

        ContentPackLever contentPackLever = LOSBlocks.BLOCK_CUSTOM_LEVER.getContentpackLever().get(id);
        if(contentPackLever == null)
            contentPackLever = LOSBlocks.BLOCK_CUSTOM_LEVER.getContentpackLever().get(Static.MISSING);


        renderLever(renderState, id, contentPackLever, tsp);

        if(contentPackLever.getFlares().length > 0)
            FlareUtils.renderFlares(id, contentPackLever, tsp, renderState.clone());
    }

    private static void renderLever(RenderState renderState, String blockId, ContentPackLever contentPackLever, TileCustomLever tile) {
        Vec3d offset = tile.getOffset();
        Vec3d customScaling = tile.getScaling();

        boolean isActive = tile.isActive();
        String state;
        Map<String, ContentPackModel[]> models;
        if(isActive){
            state = "active";
            models = contentPackLever.getActive();
        }else{
            state = "inactive";
            models = contentPackLever.getInactive();
        }
        checkCache(blockId, models, state);

        for (Map.Entry<String, ContentPackModel[]> activeModels : models.entrySet()) {

            String path = activeModels.getKey();

            String objId = blockId + "/" + path + ":" + state;
            OBJModel model = cache.get(objId);

            for (ContentPackModel activeModel : activeModels.getValue()) {

                RenderState iterationState = renderState.clone();

                ContentPackBlock block = activeModel.getBlock();
                Vec3d translate = block.getAsVec3d(block::getTranslation).add(offset);
                final Vec3d scale = Static.multiply(block.getAsVec3d(block::getScaling), customScaling);
                Vec3d rotation = block.getAsVec3d(block::getRotation);

                // Render
                iterationState.scale(scale.x, scale.y, scale.z);
                iterationState.translate(translate.x, translate.y, translate.z);
                iterationState.rotate(rotation.x, 1, 0, 0);
                iterationState.rotate(tile.getBlockRotate() + rotation.y, 0, 1, 0);
                iterationState.rotate(rotation.z, 0, 0, 1);

                try (OBJRender.Binding vbo = model.binder().texture(activeModel.getTextures()).bind(iterationState)) {

                    String[] groups = activeModel.getObj_groups();
                    if (groups.length == 0) {
                        vbo.draw();
                    } else {
                        String groupCacheId = objId + "@" + String.join("+", groups);
                        vbo.draw(groupCache.get(groupCacheId));
                    }

                } catch (Exception e) {
                    // Removes TileEntity on client-side, prevents crash
                    ModCore.error("Removing local TileCustomLever (x%d, y%d, z%d) due to exceptions: %s", tile.getPos().x, tile.getPos().y, tile.getPos().z, e.getMessage());
                    tile.getWorld().breakBlock(tile.getPos());

                }

            }
        }
    }

    public static Map<String, OBJModel> cache() {
        return cache;
    }
}
