package net.landofrails.landofsignals.render.block;

import cam72cam.mod.model.obj.OBJModel;
import cam72cam.mod.render.OpenGL;
import cam72cam.mod.render.StandardModel;
import cam72cam.mod.render.obj.OBJRender;
import cam72cam.mod.resource.Identifier;
import net.landofrails.landofsignals.LandOfSignals;
import net.landofrails.landofsignals.tile.TileSignalSO12;
import org.lwjgl.opengl.GL11;

import java.util.Collections;

public class TileSignalSO12Render {

    private static OBJRender renderer;
    private static OBJModel model;

    public static StandardModel render(TileSignalSO12 ts) {
        return new StandardModel().addCustom(() -> renderStuff(ts));
    }

    private static void renderStuff(TileSignalSO12 ts) {
        if (renderer == null || model == null) {
            try {
                model = new OBJModel(new Identifier(LandOfSignals.MODID, "models/block/so12/signalso12.obj"), 0);
                renderer = new OBJRender(model);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        ts.setFullHeight(model.heightOfGroups(Collections.singleton("SO12_SO01")));
        ts.setFullWidth(model.widthOfGroups(Collections.singleton("SO12_SO01")));
        ts.setFullLength(model.lengthOfGroups(Collections.singleton("SO12_SO01")));
        try (OpenGL.With matrix = OpenGL.matrix(); OpenGL.With tex = renderer.bindTexture()) {
            GL11.glTranslated(0.5, 0, 0.5);
            renderer.draw();
        }
    }
}
