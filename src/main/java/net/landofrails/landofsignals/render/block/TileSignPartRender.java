package net.landofrails.landofsignals.render.block;

import cam72cam.mod.math.Vec3d;
import cam72cam.mod.model.obj.OBJModel;
import cam72cam.mod.render.OpenGL;
import cam72cam.mod.render.StandardModel;
import cam72cam.mod.render.obj.OBJRender;
import cam72cam.mod.resource.Identifier;
import net.landofrails.landofsignals.LOSBlocks;
import net.landofrails.landofsignals.LandOfSignals;
import net.landofrails.landofsignals.tile.TileSignPart;
import org.apache.commons.lang3.tuple.Pair;
import org.lwjgl.opengl.GL11;

import java.util.HashMap;
import java.util.Map;

public class TileSignPartRender {

    private TileSignPartRender() {

    }

    private static final Map<String, Pair<OBJModel, OBJRender>> cache = new HashMap<>();

    public static StandardModel render(TileSignPart tsp) {
        return new StandardModel().addCustom(() -> renderStuff(tsp));
    }

    private static void renderStuff(TileSignPart tsp) {
        String id = tsp.getId();
        if (!cache.containsKey(id)) {
            try {
                OBJModel model = new OBJModel(new Identifier(LandOfSignals.MODID, LOSBlocks.BLOCK_SIGN_PART.getPath(id)), 0);
                OBJRender renderer = new OBJRender(model);
                cache.put(id, Pair.of(model, renderer));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        OBJRender renderer = cache.get(id).getRight();
        try (OpenGL.With matrix = OpenGL.matrix(); OpenGL.With ignored1 = renderer.bindTexture()) {
            Vec3d scale = LOSBlocks.BLOCK_SIGN_PART.getScaling(id);
            GL11.glScaled(scale.x, scale.y, scale.z);
            Vec3d trans = LOSBlocks.BLOCK_SIGN_PART.getTranslation(id).add(tsp.getOffset());
            GL11.glTranslated(trans.x, trans.y, trans.z);
            GL11.glRotated(tsp.getBlockRotate(), 0, 1, 0);
            renderer.draw();
        }

    }

    /**
     * Releases the renderer in to the wild and frees the cache preventing a deadlock situation
     */
    public static void releaseRenderersIntoTheWild() {

        cache.values().forEach(pair -> pair.getValue().free());
        cache.clear();
    }

}
