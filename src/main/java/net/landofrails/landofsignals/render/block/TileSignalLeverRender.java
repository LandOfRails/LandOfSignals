package net.landofrails.landofsignals.render.block;

import cam72cam.mod.model.common.ModelLoader;
import cam72cam.mod.model.common.mesh.Model;
import cam72cam.mod.render.StandardModel;
import cam72cam.mod.render.common.ModelRenderer;
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

    private static Model model;
    private static final List<String> groupNames = Arrays.asList("Base01_B01", "Hebelwerk01_H01", "Hebelwerk02_H02");

    public static StandardModel render(final TileSignalLever ts) {
        return new StandardModel().addCustom((state, _) -> renderStuff(ts, state));
    }

    @SuppressWarnings("java:S1172")
    private static void renderStuff(final TileSignalLever ts, RenderState state) {
        try {
            if (model == null) {
                model = ModelLoader.load(
                        new Identifier(LandOfSignals.MODID, "models/block/landofsignals/signalslever/signalslever.obj"));
            }

            state.translate(0.5, 0.6, 0.5);
            state.rotate(ts.getBlockRotate(), 0,1, 0);

            try (ModelRenderer.Binding bound = ModelRenderer.getRendererFor(model).bind(state)) {
                bound.enqueueOpaque(Collections.singleton(groupNames.getFirst()));
            }
            // Animation
            state.rotate((ts.getLeverRotate() * 2), 1, 0, 0);
            try (ModelRenderer.Binding bound = ModelRenderer.getRendererFor(model).bind(state)) {
                bound.enqueueOpaque(groupNames.subList(1, groupNames.size() - 1));
            }
        } catch (final Exception e) {
            throw new BlockRenderException("Error rendering TileSignalLeverRender", e);
        }
    }

}
