package net.landofrails.landofsignals.render.block;

import cam72cam.mod.model.obj.OBJModel;
import cam72cam.mod.render.StandardModel;
import cam72cam.mod.render.obj.OBJRender;
import cam72cam.mod.render.opengl.RenderState;
import cam72cam.mod.resource.Identifier;
import net.landofrails.landofsignals.LandOfSignals;
import net.landofrails.landofsignals.tile.TileSignalLever;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("java:S3878")
public class TileSignalLeverRender {

    private TileSignalLeverRender() {

    }

    private static OBJModel model;
    private static final List<String> groupNames = Arrays.asList("Base01_B01", "Hebelwerk01_H01", "Hebelwerk02_H02");

    public static StandardModel render(TileSignalLever ts) {
        return new StandardModel().addCustom((state, partialTicks) -> renderStuff(ts, state));
    }

    @SuppressWarnings("java:S1172")
    private static void renderStuff(TileSignalLever ts, RenderState state) {
        try {
            if (model == null) {
                model = new OBJModel(
                        new Identifier(LandOfSignals.MODID, "models/block/landofsignals/signalslever/signalslever.obj"),
                        0);
            }
            state.translate(0.5, 0.6, 0.5);
            state.rotate(ts.getBlockRotate(), 0, 1, 0);
            try (OBJRender.Binding vbo = model.binder().bind(state)) {

                vbo.draw(Collections.singleton(groupNames.get(0)));

                // Animation
                vbo.draw(groupNames.subList(1, groupNames.size() - 1), stateConsumer ->
                        stateConsumer.rotate((ts.getLeverRotate() * 2), 1, 0, 0)
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
