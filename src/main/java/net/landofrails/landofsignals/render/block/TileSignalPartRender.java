package net.landofrails.landofsignals.render.block;

import cam72cam.mod.math.Vec3d;
import cam72cam.mod.model.obj.OBJModel;
import cam72cam.mod.render.OpenGL;
import cam72cam.mod.render.StandardModel;
import cam72cam.mod.render.obj.OBJRender;
import cam72cam.mod.resource.Identifier;
import net.landofrails.landofsignals.LandOfSignals;
import net.landofrails.landofsignals.blocks.BlockSignalPart;
import net.landofrails.landofsignals.tile.TileSignalPart;
import org.apache.commons.lang3.tuple.Pair;
import org.lwjgl.opengl.GL11;

import java.util.HashMap;
import java.util.Map;

public class TileSignalPartRender {

    private TileSignalPartRender() {

    }

    private static Map<String, Pair<OBJModel, OBJRender>> cache = new HashMap<>();

    public static StandardModel render(TileSignalPart tsp) {
        return new StandardModel().addCustom(() -> renderStuff(tsp));
    }

    private static void renderStuff(TileSignalPart tsp) {
        BlockSignalPart block = tsp.getBlock();
        if (!cache.containsKey(block.getName())) {
            try {
                OBJModel model = new OBJModel(new Identifier(LandOfSignals.MODID, block.getPath()), 0);
                OBJRender renderer = new OBJRender(model, block.getStates());
                cache.put(block.getName(), Pair.of(model, renderer));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        OBJRender renderer = cache.get(block.getName()).getRight();
        try (OpenGL.With matrix = OpenGL.matrix(); OpenGL.With tex = renderer.bindTexture(tsp.getTexturePath())) {
            Vec3d scale = block.getScaling();
            GL11.glScaled(scale.x, scale.y, scale.z);
            Vec3d trans = block.getTranslation();
            GL11.glTranslated(trans.x, trans.y, trans.z);
            GL11.glRotated(tsp.getBlockRotate(), 0, 1, 0);
            renderer.draw();
        }
    }
}
