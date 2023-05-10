package net.landofrails.landofsignals.render.block;

import cam72cam.mod.ModCore;
import cam72cam.mod.math.Vec3d;
import cam72cam.mod.model.obj.OBJModel;
import cam72cam.mod.render.StandardModel;
import cam72cam.mod.render.obj.OBJRender;
import cam72cam.mod.render.opengl.RenderState;
import cam72cam.mod.resource.Identifier;
import net.landofrails.api.contentpacks.v2.signal.ContentPackSignal;
import net.landofrails.landofsignals.LOSBlocks;
import net.landofrails.landofsignals.LandOfSignals;
import net.landofrails.landofsignals.render.item.ItemRenderException;
import net.landofrails.landofsignals.tile.TileSignalPart;
import net.landofrails.landofsignals.utils.Static;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class TileSignalPartRender {

    private TileSignalPartRender() {

    }

    private static final Map<String, OBJModel> cache = new HashMap<>();
    protected static final Map<String, Boolean> cacheInfoOldContentPack = new HashMap<>();

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
            renderBase(id, signal, tsp, state);
        }
        renderSignals(id, signal, tsp, state);

    }

    @SuppressWarnings("java:S1134")
    private static void renderBase(String blockId, ContentPackSignal signal, TileSignalPart tile, RenderState state) {

        final Vec3d offset = tile.getOffset();
        final String base = signal.getBase();
        final String objPath = signal.getModel();

        if (!cache.containsKey(objPath)) {
            try {
                String[] states = LOSBlocks.BLOCK_SIGNAL_PART.getAllStates(blockId);
                cache.put(objPath, new OBJModel(new Identifier(LandOfSignals.MODID, objPath), 0, Arrays.asList(states)));
                cacheInfoOldContentPack.putIfAbsent(blockId, LOSBlocks.BLOCK_SIGNAL_PART.isOldContentPack(blockId));
            } catch (Exception e) {
                throw new ItemRenderException("Error loading item model/renderer...", e);
            }
        }
        final OBJModel model = cache.get(objPath);

        final float[] originalTranslate = signal.getTranslation();
        final Vec3d translate = new Vec3d(originalTranslate[0], originalTranslate[1], originalTranslate[2]).add(offset);
        final float[] scale = signal.getScaling();

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
        final String signalState = tile.getState();
        final String objPath = signal.getModel();

        if (!cache.containsKey(objPath)) {
            try {
                String[] states = LOSBlocks.BLOCK_SIGNAL_PART.getAllStates(blockId);
                cache.put(objPath, new OBJModel(new Identifier(LandOfSignals.MODID, objPath), 0, Arrays.asList(states)));
                cacheInfoOldContentPack.putIfAbsent(blockId, LOSBlocks.BLOCK_SIGNAL_PART.isOldContentPack(blockId));
            } catch (Exception e) {
                throw new ItemRenderException("Error loading item model/renderer...", e);
            }
        }
        final OBJModel model = cache.get(objPath);

        final float[] originalTranslate = signal.getTranslation();
        final Vec3d translate = new Vec3d(originalTranslate[0], originalTranslate[1], originalTranslate[2]).add(offset);
        final float[] scale = signal.getScaling();


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

}