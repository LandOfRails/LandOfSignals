package net.landofrails.landofsignals.render.block;

import cam72cam.mod.ModCore;
import cam72cam.mod.math.Vec3d;
import cam72cam.mod.model.obj.OBJModel;
import cam72cam.mod.render.OpenGL;
import cam72cam.mod.render.StandardModel;
import cam72cam.mod.render.obj.OBJRender;
import cam72cam.mod.resource.Identifier;
import net.landofrails.api.contentpacks.v2.signal.ContentPackSignal;
import net.landofrails.landofsignals.LOSBlocks;
import net.landofrails.landofsignals.LandOfSignals;
import net.landofrails.landofsignals.render.item.ItemRenderException;
import net.landofrails.landofsignals.tile.TileSignalPart;
import net.landofrails.landofsignals.utils.HighlightingUtil;
import net.landofrails.landofsignals.utils.Static;
import org.lwjgl.opengl.GL11;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class TileSignalPartRender {

    private TileSignalPartRender() {

    }

    private static final Map<String, OBJRender> cache = new HashMap<>();

    public static StandardModel render(final TileSignalPart tsp) {
        return new StandardModel().addCustom(() -> renderStuff(tsp));
    }

    private static void renderStuff(final TileSignalPart tsp) {

        String id = tsp.getId();

        if (id == null) {
            id = Static.MISSING;
        }

        ContentPackSignal signal = LOSBlocks.BLOCK_SIGNAL_PART.getContentpackSignals().get(id);

        if(signal == null) {
            signal = LOSBlocks.BLOCK_SIGNAL_PART.getContentpackSignals().get(Static.MISSING);
            tsp.setState(null);
        }

        if (signal.getUseBase()) {
            renderBase(id, signal, tsp);
        }
        renderSignals(id, signal, tsp);

        if(tsp.isHighlighting()){
            HighlightingUtil.renderHighlighting();
        }

    }

    @SuppressWarnings("java:S1134")
    private static void renderBase(String blockId, ContentPackSignal signal, TileSignalPart tile) {

        final Vec3d offset = tile.getOffset();
        final Vec3d customScaling = tile.getScaling();
        final String base = signal.getBase();
        final String objPath = signal.getModel();

        if (!cache.containsKey(objPath)) {
            try {
                String[] states = LOSBlocks.BLOCK_SIGNAL_PART.getAllStates(blockId);
                cache.put(objPath, new OBJRender(new OBJModel(new Identifier(LandOfSignals.MODID, objPath), 0, Arrays.asList(states))));
            } catch (Exception e) {
                throw new ItemRenderException("Error loading item model/renderer...", e);
            }
        }
        final OBJRender renderer = cache.get(objPath);

        final float[] originalTranslate = signal.getTranslation();
        final Vec3d translate = new Vec3d(originalTranslate[0], originalTranslate[1], originalTranslate[2]).add(offset);
        final float[] scale = signal.getScaling().clone();
        scale[0] *= customScaling.x;
        scale[1] *= customScaling.y;
        scale[2] *= customScaling.z;

        try (OpenGL.With ignored1 = OpenGL.matrix(); OpenGL.With ignored2 = renderer.bindTexture(base)) {

            // Render
            GL11.glScaled(scale[0], scale[1], scale[2]);
            GL11.glTranslated(translate.x, translate.y, translate.z);
            GL11.glRotated(tile.getBlockRotate(), 0, 1, 0);
            renderer.draw();

        } catch (Exception e) {
            // Removes TileEntity on client-side, prevents crash
            ModCore.error("Removing local SignalPart (x%d, y%d, z%d) due to exceptions: %s", tile.getPos().x, tile.getPos().y, tile.getPos().z, e.getMessage());
            tile.getWorld().breakBlock(tile.getPos());

        }

    }


    @SuppressWarnings("java:S1134")
    private static void renderSignals(String blockId, ContentPackSignal signal, TileSignalPart tile) {

        final Vec3d offset = tile.getOffset();
        final Vec3d customScaling = tile.getScaling();
        final String signalState = tile.getState();
        final String objPath = signal.getModel();

        if (!cache.containsKey(objPath)) {
            try {
                String[] states = LOSBlocks.BLOCK_SIGNAL_PART.getAllStates(blockId);
                cache.put(objPath, new OBJRender(new OBJModel(new Identifier(LandOfSignals.MODID, objPath), 0, Arrays.asList(states))));
            } catch (Exception e) {
                throw new ItemRenderException("Error loading item model/renderer...", e);
            }
        }
        final OBJRender renderer = cache.get(objPath);

        final float[] originalTranslate = signal.getTranslation();
        final Vec3d translate = new Vec3d(originalTranslate[0], originalTranslate[1], originalTranslate[2]).add(offset);
        final float[] scale = signal.getScaling().clone();
        scale[0] *= customScaling.x;
        scale[1] *= customScaling.y;
        scale[2] *= customScaling.z;

        try (OpenGL.With ignored1 = OpenGL.matrix(); OpenGL.With ignored2 = renderer.bindTexture(signalState)) {

            // Render
            GL11.glScaled(scale[0], scale[1], scale[2]);
            GL11.glTranslated(translate.x, translate.y, translate.z);
            GL11.glRotated(tile.getBlockRotate(), 0, 1, 0);
            renderer.draw();

        } catch (Exception e) {
            // Removes TileEntity on client-side, prevents crash
            ModCore.error("Removing local SignalPart (x%d, y%d, z%d) due to exceptions: %s", tile.getPos().x, tile.getPos().y, tile.getPos().z, e.getMessage());
            tile.getWorld().breakBlock(tile.getPos());

        }
    }

    public static Map<String, OBJRender> cache(){
        return cache;
    }

}