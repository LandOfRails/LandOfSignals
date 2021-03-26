package net.landofrails.landofsignals.render.block;

import cam72cam.mod.math.Vec3d;
import cam72cam.mod.model.obj.OBJModel;
import cam72cam.mod.render.OpenGL;
import cam72cam.mod.render.StandardModel;
import cam72cam.mod.render.obj.OBJRender;
import cam72cam.mod.resource.Identifier;
import net.landofrails.landofsignals.LOSBlocks;
import net.landofrails.landofsignals.LandOfSignals;
import net.landofrails.landofsignals.tile.TileSignalAnimatedPart;
import org.apache.commons.lang3.tuple.Pair;
import org.lwjgl.opengl.GL11;

import java.util.*;

public class TileSignalPartAnimatedRender {

    private TileSignalPartAnimatedRender() {

    }

    private static final Map<String, Pair<OBJModel, OBJRender>> cache = new HashMap<>();
    private static final List<String> groupNames = Arrays.asList("wing");

    public static StandardModel render(TileSignalAnimatedPart tsp) {
        return new StandardModel().addCustom(partialTicks -> renderStuff(tsp, partialTicks));
    }

    private static void renderStuff(TileSignalAnimatedPart tsp, float partialTicks) {
        String id = tsp.getId();
        if (!cache.containsKey("flare")) {
            try {
                OBJModel flareModel = new OBJModel(new Identifier(LandOfSignals.MODID, "models/block/landofsignals/lamp/flare.obj"), 0);
                OBJRender flareRenderer = new OBJRender(flareModel);
                cache.put("flare", Pair.of(flareModel, flareRenderer));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (!cache.containsKey(id)) {
            try {
                OBJModel model = new OBJModel(new Identifier(LandOfSignals.MODID, LOSBlocks.BLOCK_SIGNAL_PART.getPath(id)), 0);
                OBJRender renderer = new OBJRender(model, LOSBlocks.BLOCK_SIGNAL_PART.getStates(id));
                cache.put(id, Pair.of(model, renderer));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        OBJRender renderer = cache.get(id).getRight();
        List<String> groupsWithoutWing = new ArrayList<>();
        for (String s : renderer.model.groups()) groupsWithoutWing.add(s);
        groupsWithoutWing.removeAll(groupNames);
        try (OpenGL.With matrix = OpenGL.matrix(); OpenGL.With tex = renderer.bindTexture(tsp.getTexturePath())) {
            Vec3d scale = LOSBlocks.BLOCK_SIGNAL_PART.getScaling(id);
            GL11.glScaled(scale.x, scale.y, scale.z);
            Vec3d trans = LOSBlocks.BLOCK_SIGNAL_PART.getTranslation(id).add(tsp.getOffset());
            GL11.glTranslated(trans.x, trans.y, trans.z);
            GL11.glRotated(tsp.getBlockRotate(), 0, 1, 0);
            renderer.drawGroups(groupsWithoutWing);

            Vec3d center = renderer.model.centerOfGroups(groupNames);
            center = new Vec3d(-center.x, -center.y, -center.z);
            Vec3d rotateYaw = center.rotateYaw(tsp.getPartRotate());

            GL11.glTranslated(0, -center.y, 0);
            GL11.glRotatef(tsp.getPartRotate(), 1, 0, 0);
            GL11.glTranslated(0, rotateYaw.y, 0);

            renderer.drawGroups(groupNames);
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

//    private Vec3d angleToDirection(Vec3d angle) {
//// Convert angle to radians
//        angle.x = angle.x * 3.14159265 / 180;
//        angle.y = angle.y * 3.14159265 / 180;
//
//        double sinYaw = Math.sin(angle.y);
//        double cosYaw = Math.cos(angle.y);
//
//        double sinPitch = Math.sin(angle.x);
//        double cosPitch = Math.cos(angle.x);
//
//        return new Vec3d(cosPitch * cosYaw, cosPitch * sinYaw, -sinPitch);
//    }
}