package net.landofrails.landofsignals.render.block;

import cam72cam.mod.block.BlockEntity;
import cam72cam.mod.model.obj.OBJModel;
import cam72cam.mod.render.StandardModel;
import cam72cam.mod.render.obj.OBJRender;
import cam72cam.mod.render.opengl.RenderState;
import cam72cam.mod.resource.Identifier;
import net.landofrails.landofsignals.LandOfSignals;
import net.landofrails.landofsignals.utils.Static;

public class TileMissingRender {

    private TileMissingRender() {

    }
    private static OBJModel model;

    public static StandardModel render(BlockEntity ts) {
        return new StandardModel().addCustom((state, partialTicks) -> renderStuff(ts, state));
    }

    @SuppressWarnings("java:S1172")
    private static void renderStuff(BlockEntity ts, RenderState state) {
        try {
            if (model == null) {
                model = new OBJModel(
                        new Identifier(LandOfSignals.MODID, Static.MISSING_OBJ),
                        0);
            }
            state.translate(0.5, 0.5, 0.5);
            try (OBJRender.Binding vbo = model.binder().bind(state)) {

                vbo.draw();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
