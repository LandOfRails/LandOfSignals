package net.landofrails.landofsignals.render.block;

import cam72cam.mod.model.obj.OBJModel;
import cam72cam.mod.render.OpenGL;
import cam72cam.mod.render.StandardModel;
import cam72cam.mod.render.obj.OBJRender;
import cam72cam.mod.resource.Identifier;
import net.landofrails.landofsignals.LandOfSignals;
import net.landofrails.landofsignals.tile.TileTicketMachineSBB;
import org.lwjgl.opengl.GL11;

@SuppressWarnings("java:S3878")
public class TileTicketMachineSBBRender {

    private TileTicketMachineSBBRender() {

    }

    private static OBJRender renderer;
    private static OBJModel model;

    @SuppressWarnings("java:S1611")
    public static StandardModel render(final TileTicketMachineSBB ts) {
        return new StandardModel().addCustom((partialTicks) -> renderStuff(ts, partialTicks));
    }

    @SuppressWarnings("java:S1172")
    private static void renderStuff(final TileTicketMachineSBB ts, final float partialTicks) {
        try {
            if (renderer == null || model == null) {
                model = new OBJModel(new Identifier(LandOfSignals.MODID, "models/block/landofsignals/fahrkartenautomat_sbb/ticketautomat.obj"), 0);
                renderer = new OBJRender(model);
            }
            try (final OpenGL.With matrix = OpenGL.matrix(); final OpenGL.With tex = renderer.bindTexture()) {
                GL11.glScaled(0.65, 0.65, 0.65);
                GL11.glTranslated(0.75, 0, 0.75);
                GL11.glRotated(ts.getBlockRotate(), 0, 1, 0);
                renderer.draw();
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }
}
