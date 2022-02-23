package net.landofrails.landofsignals.render.block;

import cam72cam.mod.model.obj.OBJModel;
import cam72cam.mod.render.OpenGL;
import cam72cam.mod.render.StandardModel;
import cam72cam.mod.render.obj.OBJRender;
import cam72cam.mod.resource.Identifier;
import net.landofrails.landofsignals.LandOfSignals;
import net.landofrails.landofsignals.tile.TileSignalSO12;
import org.lwjgl.opengl.GL11;

import java.util.Arrays;
import java.util.List;

@SuppressWarnings("java:S3878")
public class TileSignalSO12Render {

    private TileSignalSO12Render() {

    }

    private static final List<String> groupNames = Arrays.asList("SO12_SO01");
    private static OBJRender renderer;
    private static OBJModel model;

    public static StandardModel render(final TileSignalSO12 ts) {
        return new StandardModel().addCustom(() -> renderStuff(ts));
    }

    private static void renderStuff(final TileSignalSO12 ts) {
        try {
            if (renderer == null || model == null) {
                model = new OBJModel(new Identifier(LandOfSignals.MODID, "models/block/landofsignals/so12/signalso12.obj"), 0);
                renderer = new OBJRender(model);
            }
            ts.setFullHeight(model.heightOfGroups(groupNames));
            ts.setFullWidth(model.widthOfGroups(groupNames));
            ts.setFullLength(model.lengthOfGroups(groupNames));
            try (final OpenGL.With matrix = OpenGL.matrix(); final OpenGL.With tex = renderer.bindTexture()) {
                GL11.glTranslated(0.5, 0, 0.5);
                renderer.draw();
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }
}
