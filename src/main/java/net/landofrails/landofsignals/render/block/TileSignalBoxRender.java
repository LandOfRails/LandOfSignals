package net.landofrails.landofsignals.render.block;

import cam72cam.mod.model.obj.OBJModel;
import cam72cam.mod.render.OpenGL;
import cam72cam.mod.render.StandardModel;
import cam72cam.mod.render.obj.OBJRender;
import cam72cam.mod.resource.Identifier;
import net.landofrails.landofsignals.LandOfSignals;
import net.landofrails.landofsignals.tile.TileSignalBox;
import org.lwjgl.opengl.GL11;

public class TileSignalBoxRender {

    private TileSignalBoxRender() {

    }

    private static OBJRender renderer;
    private static OBJModel model;

    public static StandardModel render(final TileSignalBox ts) {
        return new StandardModel().addCustom(() -> renderStuff(ts));
    }

    @SuppressWarnings("java:S1172")
    private static void renderStuff(final TileSignalBox ts) {
        try {
            if (renderer == null || model == null) {
                model = new OBJModel(new Identifier(LandOfSignals.MODID, "models/block/landofsignals/signalbox/untitled.obj"), 0);
                renderer = new OBJRender(model);
            }
            try (final OpenGL.With matrix = OpenGL.matrix(); final OpenGL.With tex = renderer.bindTexture()) {
                GL11.glScaled(0.30, 0.30, 0.30);
                GL11.glTranslated(1.5, 0, 1.5);
                renderer.draw();
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }
}
