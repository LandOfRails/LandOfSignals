package net.landofrails.landofsignals.render.block;

import cam72cam.mod.ModCore;
import cam72cam.mod.math.Vec3d;
import cam72cam.mod.model.obj.OBJModel;
import cam72cam.mod.render.GlobalRender;
import cam72cam.mod.render.StandardModel;
import cam72cam.mod.render.obj.OBJRender;
import cam72cam.mod.render.opengl.RenderState;
import cam72cam.mod.resource.Identifier;
import net.landofrails.api.contentpacks.v2.parent.ContentPackBlock;
import net.landofrails.api.contentpacks.v2.parent.ContentPackModel;
import net.landofrails.api.contentpacks.v2.sign.ContentPackSign;
import net.landofrails.landofsignals.LOSBlocks;
import net.landofrails.landofsignals.LandOfSignals;
import net.landofrails.landofsignals.tile.TileSignPart;
import net.landofrails.landofsignals.utils.Static;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class TileSignPartRender {

    private TileSignPartRender() {

    }

    private static final Map<String, OBJModel> cache = new HashMap<>();
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

                Set<String> objTextures = LOSBlocks.BLOCK_SIGN_PART.getContentpackSigns().get(blockId).getObjTextures().get(path);
                OBJModel model = new OBJModel(new Identifier(LandOfSignals.MODID, path), 0, objTextures);
                cache.putIfAbsent(objId, model);

                for (ContentPackModel signModel : modelEntry.getValue()) {
                    String[] groups = signModel.getObj_groups();
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

    public static StandardModel render(TileSignPart tsp) {
        return new StandardModel().addCustom((state, partialTicks) -> renderStuff(tsp, state));
    }

    private static void renderStuff(TileSignPart tsp, RenderState state) {

        String id = tsp.getId();

        if (id == null) {
            id = Static.MISSING;
        }

        // Textrendering
        ContentPackSign contentPackSign = LOSBlocks.BLOCK_SIGN_PART.getContentpackSigns().get(id);
        if(contentPackSign.isWriteable()){
            RenderState textState  = state.clone()
                    .lighting(false)
                    .depth_test(true)
                    .translate(0.5, 0.675, 0.5)
                    .rotate(180, 1.0F, 0.0F, 0.0F) // X
                    .rotate(-tsp.getBlockRotate(), 0.0F, 1.0F, 0.0F) // Y
                    .rotate(0, 0.0F, 0.0F, 1.0F) // Z
                    .translate(0.025, 0, -0.065)
                    .scale(0.05f, 0.05f, 0.05f);

            GlobalRender.drawRawCenteredText(tsp.getText(), textState.clone(), GlobalRender.rgbToInt(255, 0, 0));
            textState.translate(0,-10, 0);
            GlobalRender.drawRawLeftOrientedText(tsp.getText(), textState.clone(), GlobalRender.rgbToInt(0, 255, 0));
            textState.translate(0, 20, 0);
            GlobalRender.drawRawRightOrientedText(tsp.getText(), textState.clone(), GlobalRender.rgbToInt(0, 0, 255));
        }
        //

        renderBase(id, tsp, state);

    }

    private static void renderBase(String blockId, TileSignPart tile, RenderState state) {

        Vec3d offset = tile.getOffset();
        Vec3d customScaling = tile.getScaling();
        ContentPackSign contentPackSign = LOSBlocks.BLOCK_SIGN_PART.getContentpackSigns().get(blockId);

        if(contentPackSign == null) contentPackSign = LOSBlocks.BLOCK_SIGN_PART.getContentpackSigns().get(Static.MISSING);

        checkCache(blockId, LOSBlocks.BLOCK_SIGN_PART.getContentpackSigns().get(blockId).getBase());
        for (Map.Entry<String, ContentPackModel[]> baseModels : contentPackSign.getBase().entrySet()) {

            String path = baseModels.getKey();

            String objId = blockId + "/" + path;
            OBJModel model = cache.get(objId);

            for (ContentPackModel baseModel : baseModels.getValue()) {

                RenderState iterationState = state.clone();

                ContentPackBlock block = baseModel.getBlock();
                Vec3d translate = block.getAsVec3d(block::getTranslation).add(offset);
                Vec3d scale = Static.multiply(block.getAsVec3d(block::getScaling), customScaling);
                Vec3d rotation = block.getAsVec3d(block::getRotation);

                iterationState.scale(scale);
                iterationState.translate(translate);
                iterationState.rotate(rotation.x, 1, 0, 0);
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
                    ModCore.error("Removing local SignPart (x%d, y%d, z%d) due to exceptions: %s", tile.getPos().x, tile.getPos().y, tile.getPos().z, e.getMessage());
                    tile.getWorld().breakBlock(tile.getPos());

                }

            }
        }

    }

}
