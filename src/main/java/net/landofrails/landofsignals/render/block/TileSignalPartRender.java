package net.landofrails.landofsignals.render.block;

import cam72cam.mod.ModCore;
import cam72cam.mod.math.Vec3d;
import cam72cam.mod.model.obj.OBJModel;
import cam72cam.mod.render.StandardModel;
import cam72cam.mod.render.obj.OBJRender;
import cam72cam.mod.render.opengl.BlendMode;
import cam72cam.mod.render.opengl.DirectDraw;
import cam72cam.mod.render.opengl.RenderState;
import cam72cam.mod.render.opengl.Texture;
import cam72cam.mod.resource.Identifier;
import net.landofrails.api.contentpacks.v2.signal.ContentPackSignal;
import net.landofrails.landofsignals.LOSBlocks;
import net.landofrails.landofsignals.LandOfSignals;
import net.landofrails.landofsignals.render.item.ItemRenderException;
import net.landofrails.landofsignals.tile.TileSignalPart;
import net.landofrails.landofsignals.utils.HighlightingUtil;
import net.landofrails.landofsignals.utils.Static;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class TileSignalPartRender {

    private TileSignalPartRender() {

    }

    private static final Map<String, OBJModel> cache = new HashMap<>();

    public static StandardModel render(final TileSignalPart tsp) {
        return new StandardModel().addCustom((state, partialTicks) -> renderStuff(tsp, state));
    }

    private static void renderStuff(final TileSignalPart tsp, RenderState state) {

        String id = tsp.getId();

        if (id == null) {
            id = Static.MISSING;
        }

        ContentPackSignal signal = LOSBlocks.BLOCK_SIGNAL_PART.getContentpackSignals().get(id);

        if(signal == null) {
            signal = LOSBlocks.BLOCK_SIGNAL_PART.getContentpackSignals().get(Static.MISSING);
            tsp.setState("");
        }

        if (signal.getUseBase()) {
            renderBase(id, signal, tsp, state.clone());
        }
        renderSignals(id, signal, tsp, state.clone());

        // TODO remove/adjust when generalized
        if(id.contains("block_signal_part_light_flare"))
            renderFlares(id, signal, tsp, state.clone());
        //

        if(tsp.isHighlighting()){
            HighlightingUtil.renderHighlighting(state.clone());
        }

    }

    @SuppressWarnings("java:S1134")
    private static void renderBase(String blockId, ContentPackSignal signal, TileSignalPart tile, RenderState state) {

        final Vec3d offset = tile.getOffset();
        final Vec3d customScaling = tile.getScaling();
        final String base = signal.getBase();
        final String objPath = signal.getModel();

        if (!cache.containsKey(objPath)) {
            try {
                String[] states = LOSBlocks.BLOCK_SIGNAL_PART.getAllStates(blockId);
                cache.put(objPath, new OBJModel(new Identifier(LandOfSignals.MODID, objPath), 0, Arrays.asList(states)));
            } catch (Exception e) {
                throw new ItemRenderException("Error loading item model/renderer...", e);
            }
        }
        final OBJModel model = cache.get(objPath);

        final float[] originalTranslate = signal.getTranslation();
        final Vec3d translate = new Vec3d(originalTranslate[0], originalTranslate[1], originalTranslate[2]).add(offset);
        final float[] scale = signal.getScaling().clone();
        scale[0] *= customScaling.x;
        scale[1] *= customScaling.y;
        scale[2] *= customScaling.z;

        state.scale(scale[0], scale[1], scale[2]);
        state.translate(translate.x, translate.y, translate.z);
        state.rotate(tile.getBlockRotate(), 0, 1, 0);

        try (OBJRender.Binding vbo = model.binder().texture(base).bind(state)) {
            // Render
            vbo.draw();
        } catch (Exception e) {
            // Removes TileEntity on client-side, prevents crash
            ModCore.error("Removing local SignalPart (x%d, y%d, z%d) due to exceptions: %s", tile.getPos().x, tile.getPos().y, tile.getPos().z, e.getMessage());
            tile.getWorld().breakBlock(tile.getPos());

        }

    }


    @SuppressWarnings("java:S1134")
    private static void renderSignals(String blockId, ContentPackSignal signal, TileSignalPart tile, RenderState state) {

        final Vec3d offset = tile.getOffset();
        final Vec3d customScaling = tile.getScaling();
        final String signalState = tile.getState();
        final String objPath = signal.getModel();

        if (!cache.containsKey(objPath)) {
            try {
                String[] states = LOSBlocks.BLOCK_SIGNAL_PART.getAllStates(blockId);
                cache.put(objPath, new OBJModel(new Identifier(LandOfSignals.MODID, objPath), 0, Arrays.asList(states)));
            } catch (Exception e) {
                throw new ItemRenderException("Error loading item model/renderer...", e);
            }
        }
        final OBJModel model = cache.get(objPath);

        final float[] originalTranslate = signal.getTranslation();
        final Vec3d translate = new Vec3d(originalTranslate[0], originalTranslate[1], originalTranslate[2]).add(offset);
        final float[] scale = signal.getScaling().clone();
        scale[0] *= customScaling.x;
        scale[1] *= customScaling.y;
        scale[2] *= customScaling.z;


        state.scale(scale[0], scale[1], scale[2]);
        state.translate(translate);
        state.rotate(tile.getBlockRotate(), 0, 1, 0);

        try (OBJRender.Binding vbo = model.binder().texture(signalState).bind(state)) {
            // Render
            vbo.draw();
        } catch (Exception e) {
            // Removes TileEntity on client-side, prevents crash
            ModCore.error("Removing local SignalPart (x%d, y%d, z%d) due to exceptions: %s", tile.getPos().x, tile.getPos().y, tile.getPos().z, e.getMessage());
            tile.getWorld().breakBlock(tile.getPos());

        }
    }

    public static Map<String, OBJModel> cache(){
        return cache;
    }


    private static void renderFlares(String id, ContentPackSignal signal, TileSignalPart tile, RenderState state) {

        // TODO remove when generalized
        final String signalState = tile.getState();
        if(!signalState.equalsIgnoreCase("red"))
            return;
        //

        float red = 165f / 255f; // TODO Should come from contentpack
        float green = 32f / 255f; // TODO Should come from contentpack
        float blue = 25f / 255f; // TODO Should come from contentpack
        float intensity = 2f; // TODO Should be calculated

        Identifier lightTex = new Identifier(LandOfSignals.MODID, "textures/light/antivignette.png");

        state.texture(Texture.wrap(lightTex))
                .lightmap(1, 1)
                .depth_test(true)
                .depth_mask(false)
                .alpha_test(false).blend(new BlendMode(BlendMode.GL_SRC_ALPHA, BlendMode.GL_ONE_MINUS_SRC_ALPHA));

        state.color((float)Math.sqrt(red), (float)Math.sqrt(green), (float)Math.sqrt(blue), 1 - (intensity/3f));

        final String objPath = signal.getModel();
        final OBJModel model = cache.get(objPath);
        Vec3d centerOfModel = model.centerOfGroups(model.groups());
        Predicate<String> isLightFlare = group -> group.startsWith("lightflare_1"); // TODO Should come from the contentpack
        Vec3d centerOfLightFlare = model.centerOfGroups(model.groups().stream().filter(isLightFlare).collect(Collectors.toSet()));

        Vec3d flareOffset = new Vec3d(0.5f, 0.5f,0.5f); // Set position to center of block
        state.translate(flareOffset);
        state.rotate(tile.getBlockRotate() - 180,0,1,0); // TODO Needs to use the value from the element in the obj later on.

        Vec3d modelOffset = centerOfLightFlare.subtract(centerOfModel);
        modelOffset = new Vec3d(modelOffset.x, modelOffset.y, -modelOffset.z - 0.45);
        state.translate(modelOffset); // move it towards the position of the light flare

        DirectDraw buffer = new DirectDraw();
        buffer.vertex(-1, -1, 0).uv(0, 0);
        buffer.vertex(-1, 1, 0).uv(0, 1);
        buffer.vertex(1, 1, 0).uv(1, 1);
        buffer.vertex(1, -1, 0).uv(1, 0);
        buffer.draw(state);

    }

}