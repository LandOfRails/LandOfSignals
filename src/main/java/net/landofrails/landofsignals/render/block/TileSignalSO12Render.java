package net.landofrails.landofsignals.render.block;

import cam72cam.mod.model.obj.OBJModel;
import cam72cam.mod.render.StandardModel;
import cam72cam.mod.render.obj.OBJRender;
import cam72cam.mod.render.opengl.RenderState;
import cam72cam.mod.resource.Identifier;
import net.landofrails.landofsignals.LandOfSignals;
import net.landofrails.landofsignals.tile.TileSignalSO12;

import java.util.Collections;
import java.util.List;

@SuppressWarnings("java:S3878")
public class TileSignalSO12Render {

    private TileSignalSO12Render() {

    }

    private static final List<String> groupNames = Collections.singletonList("SO12_SO01");
    private static OBJModel model;

    public static StandardModel render(TileSignalSO12 ts) {
        return new StandardModel().addCustom((state, partialTicks) -> renderStuff(ts, state));
    }

    private static void renderStuff(TileSignalSO12 ts, RenderState state) {
        try {
            if (model == null) {
                model = new OBJModel(new Identifier(LandOfSignals.MODID, "models/block/landofsignals/so12/signalso12.obj"), 0);
            }
            ts.setFullHeight(model.heightOfGroups(groupNames));
            ts.setFullWidth(model.widthOfGroups(groupNames));
            ts.setFullLength(model.lengthOfGroups(groupNames));
            state.translate(0.5, 0, 0.5);
            try (OBJRender.Binding vbo = model.binder().bind(state)) {
                vbo.draw();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
