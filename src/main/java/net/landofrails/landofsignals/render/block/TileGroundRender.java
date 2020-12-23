package net.landofrails.landofsignals.render.block;

import cam72cam.mod.model.obj.OBJModel;
import cam72cam.mod.render.OpenGL;
import cam72cam.mod.render.StandardModel;
import cam72cam.mod.render.obj.OBJRender;
import net.landofrails.landofsignals.tile.TileGround;
import net.landofrails.landofsignals.utils.Static;
import org.lwjgl.opengl.GL11;

import java.util.Arrays;
import java.util.List;

public class TileGroundRender {

    private static OBJRender renderer;
    private static OBJModel model;
    private static final List<String> groupNames = Arrays.asList(new String[]{"all"});

    public static StandardModel render(TileGround ts) {
        return new StandardModel().addCustom(() -> renderStuff(ts));
    }

    private static void renderStuff(TileGround ts) {
        if (renderer == null || model == null) {
            try {
                model = new OBJModel(Static.listGroundModels.get(ts.getBlock()), 0);
                renderer = new OBJRender(model);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try (OpenGL.With matrix = OpenGL.matrix(); OpenGL.With tex = renderer.bindTexture()) {
            GL11.glTranslated(0.5, 0, 0.5);
            GL11.glRotated(ts.getBlockRotate(), 0, 1, 0);
            renderer.draw();
        }
        ts.setFullHeight(model.heightOfGroups(groupNames));
        ts.setFullWidth(model.widthOfGroups(groupNames));
        ts.setFullLength(model.lengthOfGroups(groupNames));
    }
}
