package net.landofrails.landofsignals.render.block;

import cam72cam.mod.block.BlockEntity;
import cam72cam.mod.model.common.ModelLoader;
import cam72cam.mod.model.common.mesh.Model;
import cam72cam.mod.render.StandardModel;
import cam72cam.mod.render.common.ModelRenderer;
import cam72cam.mod.render.opengl.RenderState;
import cam72cam.mod.resource.Identifier;
import net.landofrails.landofsignals.LandOfSignals;
import net.landofrails.landofsignals.utils.Static;

public class TileMissingRender {

    private TileMissingRender() {

    }
    private static Model model;

    public static StandardModel render(BlockEntity ignored) {
        return new StandardModel().addCustom((state, _) -> renderStuff(state));
    }

    @SuppressWarnings("java:S1172")
    private static void renderStuff(RenderState state) {
        try {
            if (model == null) {
                model = ModelLoader.load(
                        new Identifier(LandOfSignals.MODID, Static.MISSING_OBJ));
            }
            state.translate(0.5, 0.5, 0.5);
            try (ModelRenderer.Binding bound = ModelRenderer.getRendererFor(model).bind(state)) {

                bound.enqueueOpaque();
            }
        } catch (Exception e) {
            throw new BlockRenderException("Error rendering TileMissingRender", e);
        }
    }

}
