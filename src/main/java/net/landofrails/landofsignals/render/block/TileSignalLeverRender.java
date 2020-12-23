package net.landofrails.landofsignals.render.block;

import cam72cam.mod.model.obj.OBJModel;
import cam72cam.mod.render.OpenGL;
import cam72cam.mod.render.StandardModel;
import cam72cam.mod.render.obj.OBJRender;
import cam72cam.mod.resource.Identifier;
import net.landofrails.landofsignals.LandOfSignals;
import net.landofrails.landofsignals.tile.TileSignalLever;
import org.lwjgl.opengl.GL11;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TileSignalLeverRender {

    private static OBJRender renderer;
    private static OBJModel model;
    private static final List<String> groupNames = Arrays.asList(new String[]{"Base01_B01", "Hebelwerk01_H01", "Hebelwerk02_H02"});

    public static StandardModel render(TileSignalLever ts) {
        return new StandardModel().addCustom((partialTicks) -> renderStuff(ts, partialTicks));
    }

    private static void renderStuff(TileSignalLever ts, float partialTicks) {
        if (renderer == null || model == null) {
            try {
                model = new OBJModel(new Identifier(LandOfSignals.MODID, "models/block/signalslever/signalslever.obj"), 0);
                renderer = new OBJRender(model);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try (OpenGL.With matrix = OpenGL.matrix(); OpenGL.With tex = renderer.bindTexture()) {
            GL11.glTranslated(0.5, 0.6, 0.5);
            GL11.glRotated(ts.getBlockRotate(), 0, 1, 0);
            renderer.drawGroups(Collections.singleton(groupNames.get(0)));

            //Animation
            GL11.glRotated((ts.getLeverRotate() * 2), 1, 0, 0);
            renderer.drawGroups(groupNames.subList(1, groupNames.size() - 1));
        }
        ts.setFullHeight(model.heightOfGroups(groupNames));
        ts.setFullWidth(model.widthOfGroups(groupNames));
        ts.setFullLength(model.lengthOfGroups(groupNames));
    }
}
