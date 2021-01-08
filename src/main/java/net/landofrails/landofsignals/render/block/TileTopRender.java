package net.landofrails.landofsignals.render.block;

import cam72cam.mod.math.Vec3d;
import cam72cam.mod.model.obj.OBJModel;
import cam72cam.mod.render.OpenGL;
import cam72cam.mod.render.StandardModel;
import cam72cam.mod.render.obj.OBJRender;
import net.landofrails.landofsignals.tile.TileTop;
import net.landofrails.landofsignals.utils.Static;
import org.lwjgl.opengl.GL11;
import scala.Tuple2;

import java.util.HashMap;
import java.util.Map;

public class TileTopRender {

    private TileTopRender() {

    }

    private static final Map<String, Tuple2<OBJModel, OBJRender>> cache = new HashMap<>();

    public static StandardModel render(TileTop ts) {
        return new StandardModel().addCustom(() -> renderStuff(ts));
    }

    private static void renderStuff(TileTop ts) {
        String blockName = ts.getBlock();
        if (!cache.containsKey(blockName)) {
            try {
                OBJModel model = new OBJModel(Static.listTopModels.get(blockName)._1(), 0);
                OBJRender renderer = new OBJRender(model, Static.listTopModels.get(blockName)._4());
                cache.put(blockName, new Tuple2<>(model, renderer));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        OBJRender renderer = cache.get(blockName)._2();
        try (OpenGL.With ignored = renderer.bindTexture(ts.getTexturePath())) {
            Vec3d scale = Static.listTopModels.get(blockName)._5();
            GL11.glScaled(scale.x, scale.y, scale.z);
            Vec3d trans = Static.listTopModels.get(blockName)._6();
            GL11.glTranslated(trans.x, trans.y, trans.z);
            GL11.glRotated(ts.getBlockRotate(), 0, 1, 0);
            renderer.draw();
        }
    }
}
