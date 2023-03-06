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
import net.landofrails.landofsignals.utils.Static;
import org.lwjgl.opengl.GL11;

import java.util.HashMap;
import java.util.Map;

public class TileSignalPartRender {

    private TileSignalPartRender() {

    }

    private static final Map<String, OBJRender> cache = new HashMap<>();
    protected static final Map<String, Boolean> cacheInfoOldContentPack = new HashMap<>();

    public static StandardModel render(final TileSignalPart tsp) {
        return new StandardModel().addCustom(() -> renderStuff(tsp));
    }

    private static void renderStuff(final TileSignalPart tsp) {

        String id = tsp.getId();

        if (id == null) {
            id = Static.MISSING;
        }

        ContentPackSignal signal = LOSBlocks.BLOCK_SIGNAL_PART.getContentpackSignals().get(id);
        renderBase(id, signal, tsp);
        renderSignals(id, signal, tsp);

    }

    @SuppressWarnings("java:S1134")
    private static void renderBase(String blockId, ContentPackSignal signal, TileSignalPart tile) {

        final Vec3d offset = tile.getOffset();
        final String base = signal.getBase();
        final String objPath = signal.getModel();

        if (!cache.containsKey(objPath)) {
            try {
                cache.put(objPath, new OBJRender(new OBJModel(new Identifier(LandOfSignals.MODID, objPath), 0)));
                cacheInfoOldContentPack.putIfAbsent(blockId, LOSBlocks.BLOCK_SIGNAL_PART.isOldContentPack(blockId));
            } catch (Exception e) {
                throw new ItemRenderException("Error loading item model/renderer...", e);
            }
        }
        final OBJRender renderer = cache.get(objPath);

        final float[] originalTranslate = signal.getTranslation();
        final Vec3d translate = new Vec3d(originalTranslate[0], originalTranslate[1], originalTranslate[2]).add(offset);
        final float[] scale = signal.getScaling();

        try (OpenGL.With ignored1 = OpenGL.matrix(); OpenGL.With ignored2 = renderer.bindTexture(base)) {

            // Render
            GL11.glScaled(scale[0], scale[1], scale[2]);
            GL11.glTranslated(translate.x, translate.y, translate.z);
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
        final String signalState = tile.getState();
        final String objPath = signal.getModel();

        if (!cache.containsKey(objPath)) {
            try {
                cache.put(objPath, new OBJRender(new OBJModel(new Identifier(LandOfSignals.MODID, objPath), 0)));
                cacheInfoOldContentPack.putIfAbsent(blockId, LOSBlocks.BLOCK_SIGNAL_PART.isOldContentPack(blockId));
            } catch (Exception e) {
                throw new ItemRenderException("Error loading item model/renderer...", e);
            }
        }
        final OBJRender renderer = cache.get(objPath);

        final float[] originalTranslate = signal.getTranslation();
        final Vec3d translate = new Vec3d(originalTranslate[0], originalTranslate[1], originalTranslate[2]).add(offset);
        final float[] scale = signal.getScaling();

        try (OpenGL.With ignored1 = OpenGL.matrix(); OpenGL.With ignored2 = renderer.bindTexture(signalState)) {

            // Render
            GL11.glScaled(scale[0], scale[1], scale[2]);
            GL11.glTranslated(translate.x, translate.y, translate.z);
            renderer.draw();

        } catch (Exception e) {
            // Removes TileEntity on client-side, prevents crash
            ModCore.error("Removing local SignalPart (x%d, y%d, z%d) due to exceptions: %s", tile.getPos().x, tile.getPos().y, tile.getPos().z, e.getMessage());
            tile.getWorld().breakBlock(tile.getPos());

        }
    }

}