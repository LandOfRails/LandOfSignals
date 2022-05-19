package net.landofrails.landofsignals.render.block;

import cam72cam.mod.model.obj.OBJModel;
import cam72cam.mod.render.StandardModel;
import cam72cam.mod.render.obj.OBJRender;
import cam72cam.mod.render.opengl.RenderState;
import cam72cam.mod.resource.Identifier;
import net.landofrails.landofsignals.LandOfSignals;
import net.landofrails.landofsignals.tile.TileTicketMachineSBB;

@SuppressWarnings("java:S3878")
public class TileTicketMachineSBBRender {

    private TileTicketMachineSBBRender() {

    }

    private static OBJModel model;

    @SuppressWarnings("java:S1611")
    public static StandardModel render(TileTicketMachineSBB ts) {
        return new StandardModel().addCustom((state, partialTicks) -> renderStuff(ts, state));
    }

    @SuppressWarnings("java:S1172")
    private static void renderStuff(TileTicketMachineSBB ts, RenderState state) {
        try {
            if (model == null) {
                model = new OBJModel(new Identifier(LandOfSignals.MODID, "models/block/landofsignals/fahrkartenautomat_sbb/ticketautomat.obj"), 0);
            }

            state.scale(0.65, 0.65, 0.65);
            state.translate(0.75, 0, 0.75);
            state.rotate(ts.getBlockRotate(), 0, 1, 0);
            try (OBJRender.Binding vbo = model.binder().bind(state)) {
                vbo.draw();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
