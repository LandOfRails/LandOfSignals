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

@SuppressWarnings("java:S3878")
public class TileSignalLeverRender {

    private TileSignalLeverRender() {

    }

    private static OBJRender renderer;
    private static OBJModel model;
    private static final List<String> groupNames = Arrays.asList("Base01_B01", "Hebelwerk01_H01", "Hebelwerk02_H02");

    public static StandardModel render(final TileSignalLever ts) {
        return new StandardModel().addCustom(partialTicks -> renderStuff(ts, partialTicks));
    }

    @SuppressWarnings("java:S1172")
    private static void renderStuff(final TileSignalLever ts, final float partialTicks) {
        try {
            if (renderer == null || model == null) {
                model = new OBJModel(
                        new Identifier(LandOfSignals.MODID, "models/block/landofsignals/signalslever/signalslever.obj"),
                        0);
                renderer = new OBJRender(model);
            }
            try (final OpenGL.With matrix = OpenGL.matrix(); final OpenGL.With tex = renderer.bindTexture()) {
                GL11.glTranslated(0.5, 0.6, 0.5);
                GL11.glRotated(ts.getBlockRotate(), 0, 1, 0);

                renderer.drawGroups(Collections.singleton(groupNames.get(0)));

                // Animation
                GL11.glRotated((ts.getLeverRotate() * 2), 1, 0, 0);
                renderer.drawGroups(groupNames.subList(1, groupNames.size() - 1));
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }
}
