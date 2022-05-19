package net.landofrails.landofsignals.render.block;

import cam72cam.mod.model.obj.OBJModel;
import cam72cam.mod.render.StandardModel;
import cam72cam.mod.render.obj.OBJRender;
import cam72cam.mod.render.opengl.RenderState;
import cam72cam.mod.resource.Identifier;
import net.landofrails.landofsignals.LandOfSignals;
import net.landofrails.landofsignals.tile.TileSignalBox;

public class TileSignalBoxRender {

    private TileSignalBoxRender() {

    }

    private static OBJModel model;

    public static StandardModel render(TileSignalBox ts) {
        return new StandardModel().addCustom((state, partialTicks) -> renderStuff(ts, state));
    }

    @SuppressWarnings("java:S1172")
    private static void renderStuff(TileSignalBox ts, RenderState state) {
        try {
            if (model == null) {
                model = new OBJModel(new Identifier(LandOfSignals.MODID, "models/block/landofsignals/signalbox/untitled.obj"), 0);
            }
            state.scale(0.30, 0.30, 0.30);
            state.translate(1.5, 0, 1.5);
            try (OBJRender.Binding vbo = model.binder().bind(state)) {
                vbo.draw();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
