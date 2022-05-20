package net.landofrails.landofsignals.render.block;

import cam72cam.mod.model.obj.OBJModel;
import cam72cam.mod.render.StandardModel;
import cam72cam.mod.render.obj.OBJRender;
import cam72cam.mod.render.opengl.RenderState;
import cam72cam.mod.resource.Identifier;
import net.landofrails.landofsignals.LOSBlocks;
import net.landofrails.landofsignals.LandOfSignals;
import net.landofrails.landofsignals.tile.TileSignalPart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TileSignalPartRender {

    private TileSignalPartRender() {

    }

    private static final Map<String, OBJModel> cache = new HashMap<>();

    public static StandardModel render(TileSignalPart tsp) {
        return new StandardModel().addCustom((state, partialTicks) -> renderStuff(tsp, state));
    }

    private static void renderStuff(TileSignalPart tsp, RenderState state) {
        String id = tsp.getId();
        if (!cache.containsKey("flare")) {
            try {
                OBJModel flareModel = new OBJModel(new Identifier(LandOfSignals.MODID, "models/block/landofsignals/lamp/flare.obj"), 0);
                cache.put("flare", flareModel);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (!cache.containsKey(id)) {
            try {
                List<String> states = new ArrayList<>(LOSBlocks.BLOCK_SIGNAL_PART.getStates(id));
                // TODO Remove if UMC fixes this issue
                states.add("");
                states.remove(null);
                OBJModel model = new OBJModel(new Identifier(LandOfSignals.MODID, LOSBlocks.BLOCK_SIGNAL_PART.getPath(id)), 0, states);
                cache.put(id, model);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        OBJModel model = cache.get(id);
        state.scale(LOSBlocks.BLOCK_SIGNAL_PART.getScaling(id));
        state.translate(LOSBlocks.BLOCK_SIGNAL_PART.getTranslation(id).add(tsp.getOffset()));
        state.rotate(tsp.getBlockRotate(), 0, 1, 0);
        try (OBJRender.Binding vbo = model.binder().texture(tsp.getTexturePath()).bind(state)) {
            vbo.draw();
        }

//        OBJRender flareRenderer = cache.get("flare").getRight();
//
//        GL11.glPushMatrix();
//        {
//            GL11.glPushAttrib(GL11.GL_ALPHA_TEST_FUNC);
//            GL11.glPushAttrib(GL11.GL_ALPHA_TEST_REF);
//            GL11.glPushAttrib(GL11.GL_LIGHTING);
//            GL11.glPushAttrib(GL11.GL_BLEND);
//
//            RenderUtil.lightmapPush();
//            {
//                RenderUtil.lightmapBright();
//
//                // Disable lighting & enable alpha blending.
//                GL11.glAlphaFunc(GL11.GL_GREATER, 0.01F);
//                GL11.glDisable(GL11.GL_LIGHTING);
//                GL11.glEnable(GL11.GL_BLEND);
//
//                OpenGL.With color = OpenGL.color(0.5F, 1.0F, 0.0F, 0.0F);
//
//                GL11.glRotatef(90F, 0.0F, 1.0F, 0.0F);
//
//                flareRenderer.draw();
//            }
//            RenderUtil.lightmapPop();
//
//            GL11.glPopAttrib(); /* GL11.GL_BLEND */
//            GL11.glPopAttrib(); /* GL11.GL_LIGHTING */
//            GL11.glPopAttrib(); /* GL11.GL_ALPHA_TEST_REF */
//            GL11.glPopAttrib(); /* GL11.GL_ALPHA_TEST_FUNC */
//        }
//        GL11.glPopMatrix();
    }
}