package net.landofrails.landofsignals.render.block;

import cam72cam.mod.model.obj.OBJModel;
import cam72cam.mod.render.OpenGL;
import cam72cam.mod.render.StandardModel;
import cam72cam.mod.render.obj.OBJRender;
import cam72cam.mod.resource.Identifier;
import net.landofrails.landofsignals.LandOfSignals;
import net.landofrails.landofsignals.tile.TileSignalBox;
import org.lwjgl.opengl.GL11;

import java.util.Arrays;
import java.util.List;

public class TileSignalBoxRender {

    private static final List<String> groupNames = Arrays.asList(new String[]{"all"});
    private static OBJRender renderer;
    private static OBJModel model;

    public static StandardModel render(TileSignalBox ts) {
        return new StandardModel().addCustom(() -> renderStuff(ts));
    }

    private static void renderStuff(TileSignalBox ts) {
        if (renderer == null || model == null) {
            try {
                model = new OBJModel(new Identifier(LandOfSignals.MODID, "models/block/signalbox/untitled.obj"), 0);
                renderer = new OBJRender(model);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try (OpenGL.With matrix = OpenGL.matrix(); OpenGL.With tex = renderer.bindTexture()) {
            GL11.glScaled(0.30, 0.30, 0.30);
            GL11.glTranslated(1.5, 0, 1.5);
            renderer.draw();
        }
    }
}
