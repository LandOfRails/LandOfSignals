package net.landofrails.landofsignals.render.block;

import cam72cam.mod.math.Vec3d;
import cam72cam.mod.model.obj.OBJModel;
import cam72cam.mod.render.OpenGL;
import cam72cam.mod.render.StandardModel;
import cam72cam.mod.render.obj.OBJRender;
import cam72cam.mod.resource.Identifier;
import net.landofrails.landofsignals.LandOfSignals;
import net.landofrails.landofsignals.blocks.BlockSignalPart;
import net.landofrails.landofsignals.tile.TileSignalPart;
import org.apache.commons.lang3.tuple.Pair;
import org.lwjgl.opengl.GL11;

import java.util.HashMap;
import java.util.Map;

public class TileSignalPartRender {

    private TileSignalPartRender() {

    }

    private static final Map<String, Pair<OBJModel, OBJRender>> cache = new HashMap<>();

    public static StandardModel render(TileSignalPart tsp) {
        return new StandardModel().addCustom(() -> renderStuff(tsp));
    }

    private static void renderStuff(TileSignalPart tsp) {
        if (!cache.containsKey("flare")) {
            try {
                OBJModel flareModel = new OBJModel(new Identifier(LandOfSignals.MODID, "models/block/landofsignals/lamp/flare.obj"), 0);
                OBJRender flareRenderer = new OBJRender(flareModel);
                cache.put("flare", Pair.of(flareModel, flareRenderer));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        BlockSignalPart block = tsp.getBlock();
        if (!cache.containsKey(block.getName())) {
            try {
                OBJModel model = new OBJModel(new Identifier(LandOfSignals.MODID, block.getPath()), 0);
                OBJRender renderer = new OBJRender(model, block.getStates());
                cache.put(block.getName(), Pair.of(model, renderer));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        OBJRender renderer = cache.get(block.getName()).getRight();
        try (OpenGL.With matrix = OpenGL.matrix(); OpenGL.With tex = renderer.bindTexture(tsp.getTexturePath())) {
            Vec3d scale = block.getScaling();
            GL11.glScaled(scale.x, scale.y, scale.z);
            Vec3d trans = block.getTranslation().add(tsp.getOffset());
            Vec3d offset = tsp.getOffset();
            GL11.glTranslated(trans.x, trans.y, trans.z);
            GL11.glRotated(tsp.getBlockRotate(), 0, 1, 0);
            renderer.draw();
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