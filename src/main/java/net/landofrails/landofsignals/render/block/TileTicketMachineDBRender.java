package net.landofrails.landofsignals.render.block;

import cam72cam.mod.model.obj.OBJModel;
import cam72cam.mod.render.StandardModel;
import cam72cam.mod.render.obj.OBJRender;
import cam72cam.mod.render.opengl.RenderState;
import cam72cam.mod.resource.Identifier;
import net.landofrails.landofsignals.LandOfSignals;
import net.landofrails.landofsignals.tile.TileTicketMachineDB;

import java.util.Collections;
import java.util.List;

@SuppressWarnings("java:S3878")
public class TileTicketMachineDBRender {

    private TileTicketMachineDBRender() {

    }

    private static OBJModel model;
    private static final List<String> groupNames = Collections.singletonList("Polygon-Objekt");

    @SuppressWarnings("java:S1611")
    public static StandardModel render(TileTicketMachineDB ts) {
        return new StandardModel().addCustom((state, partialTicks) -> renderStuff(ts, state));
    }

    @SuppressWarnings("java:S1172")
    private static void renderStuff(TileTicketMachineDB ts, RenderState state) {
        try {
            if (model == null) {
                model = new OBJModel(new Identifier(LandOfSignals.MODID, "models/block/landofsignals/fahrkartenautomat_db/fahrkartenautomat_db.obj"), 0);
            }
            state.translate(0.5, 0, 0.5);
            state.rotate(ts.getBlockRotate(), 0, 1, 0);
            try (OBJRender.Binding vbo = model.binder().bind(state)) {
                vbo.draw();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
