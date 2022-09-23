package net.landofrails.landofsignals.render.block;

import cam72cam.mod.block.BlockEntity;
import cam72cam.mod.model.obj.OBJModel;
import cam72cam.mod.render.OpenGL;
import cam72cam.mod.render.StandardModel;
import cam72cam.mod.render.obj.OBJRender;
import cam72cam.mod.resource.Identifier;
import net.landofrails.landofsignals.LandOfSignals;
import net.landofrails.landofsignals.utils.Static;
import org.lwjgl.opengl.GL11;

public class TileMissingRender {

    private TileMissingRender() {

    }

    private static OBJRender renderer;
    private static OBJModel model;

    public static StandardModel render(BlockEntity ts) {
        return new StandardModel().addCustom(partialTicks -> renderStuff(ts, partialTicks));
    }

    @SuppressWarnings("java:S1172")
    private static void renderStuff(BlockEntity ts, float partialTicks) {
        try {
            if (renderer == null || model == null) {
                model = new OBJModel(
                        new Identifier(LandOfSignals.MODID, Static.MISSING_OBJ),
                        0);
                renderer = new OBJRender(model);
            }
            try (OpenGL.With ignored = OpenGL.matrix(); OpenGL.With ignored1 = renderer.bindTexture()) {
                GL11.glTranslated(0.5, 0, 0.5);

                renderer.draw();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
