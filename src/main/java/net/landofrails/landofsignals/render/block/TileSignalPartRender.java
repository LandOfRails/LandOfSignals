package net.landofrails.landofsignals.render.block;

import cam72cam.mod.ModCore;
import cam72cam.mod.math.Vec3d;
import cam72cam.mod.model.common.ModelLoader;
import cam72cam.mod.model.common.mesh.Model;
import cam72cam.mod.render.StandardModel;
import cam72cam.mod.render.common.ModelConfig;
import cam72cam.mod.render.common.ModelRenderer;
import cam72cam.mod.render.opengl.RenderState;
import cam72cam.mod.resource.Identifier;
import net.landofrails.api.contentpacks.v2.signal.ContentPackSignal;
import net.landofrails.landofsignals.LOSBlocks;
import net.landofrails.landofsignals.LandOfSignals;
import net.landofrails.landofsignals.render.item.ItemRenderException;
import net.landofrails.landofsignals.tile.TileSignalPart;
import net.landofrails.landofsignals.utils.FlareUtils;
import net.landofrails.landofsignals.utils.HighlightingUtil;
import net.landofrails.landofsignals.utils.Static;

import java.util.*;

public class TileSignalPartRender {


    private TileSignalPartRender() {

    }

    private static final Map<String, Model> cache = new HashMap<>();

    public static StandardModel render(final TileSignalPart tsp) {
        return new StandardModel().addCustom((state, _) -> renderStuff(tsp, state));
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

        if(signal.getFlares().length > 0)
            FlareUtils.renderFlares(id, signal, tsp, state.clone());

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
                cache.put(objPath, ModelLoader.load(new Identifier(LandOfSignals.MODID, objPath), Arrays.asList(states)));
            } catch (Exception e) {
                throw new ItemRenderException("Error loading item model/renderer...", e);
            }
        }
        final Model model = cache.get(objPath);

        final float[] originalTranslate = signal.getTranslation();
        final Vec3d translate = new Vec3d(originalTranslate[0], originalTranslate[1], originalTranslate[2]).add(offset);
        final float[] scale = signal.getScaling().clone();
        scale[0] *= (float) customScaling.x;
        scale[1] *= (float) customScaling.y;
        scale[2] *= (float) customScaling.z;

        state.scale(scale[0], scale[1], scale[2]);
        state.translate(translate.x, translate.y, translate.z);
        state.rotate(tile.getBlockRotate(), 0, 1, 0);

        ModelConfig cfg = new ModelConfig().variant(base);
        try (ModelRenderer.Binding bound = ModelRenderer.getRendererFor(model).bind(cfg, state)) {
            // Render
            bound.enqueueOpaque();
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
                cache.put(objPath, ModelLoader.load(new Identifier(LandOfSignals.MODID, objPath), Arrays.asList(states)));
            } catch (Exception e) {
                throw new ItemRenderException("Error loading item model/renderer...", e);
            }
        }
        final Model model = cache.get(objPath);

        final float[] originalTranslate = signal.getTranslation();
        final Vec3d translate = new Vec3d(originalTranslate[0], originalTranslate[1], originalTranslate[2]).add(offset);
        final float[] scale = signal.getScaling().clone();
        scale[0] *= (float) customScaling.x;
        scale[1] *= (float) customScaling.y;
        scale[2] *= (float) customScaling.z;

        state.scale(scale[0], scale[1], scale[2]);
        state.translate(translate);
        state.rotate(tile.getBlockRotate(), 0, 1, 0);

        ModelConfig cfg = new ModelConfig().variant(signalState);
        try (ModelRenderer.Binding bound = ModelRenderer.getRendererFor(model).bind(cfg, state)) {
            // Render
            bound.enqueueOpaque();
        } catch (Exception e) {
            // Removes TileEntity on client-side, prevents crash
            ModCore.error("Removing local SignalPart (x%d, y%d, z%d) due to exceptions: %s", tile.getPos().x, tile.getPos().y, tile.getPos().z, e.getMessage());
            tile.getWorld().breakBlock(tile.getPos());

        }
    }

    public static Map<String, Model> cache(){
        return cache;
    }




}