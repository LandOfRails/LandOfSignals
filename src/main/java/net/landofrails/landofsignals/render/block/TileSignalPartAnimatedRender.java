package net.landofrails.landofsignals.render.block;

import cam72cam.mod.math.Vec3d;
import cam72cam.mod.model.obj.OBJModel;
import cam72cam.mod.render.StandardModel;
import cam72cam.mod.render.obj.OBJRender;
import cam72cam.mod.render.opengl.RenderState;
import cam72cam.mod.resource.Identifier;
import net.landofrails.landofsignals.LOSBlocks;
import net.landofrails.landofsignals.LandOfSignals;
import net.landofrails.landofsignals.tile.TileSignalPartAnimated;
import org.lwjgl.opengl.GL11;

import java.util.*;

public class TileSignalPartAnimatedRender {

    private TileSignalPartAnimatedRender() {

    }

    private static final Map<String, OBJModel> cache = new HashMap<>();
    private static final List<String> groupNames = Collections.singletonList("wing");

    public static StandardModel render(TileSignalPartAnimated tsp) {
        return new StandardModel().addCustom((state, partialTicks) -> renderStuff(tsp, state));
    }

    private static void renderStuff(TileSignalPartAnimated tsp, RenderState state) {
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
                List<String> states = new ArrayList<>(LOSBlocks.BLOCK_SIGNAL_PART_ANIMATED.getStates(id));
                // TODO Remove if UMC fixes this issue
                states.add("");
                states.remove(null);
                OBJModel model = new OBJModel(new Identifier(LandOfSignals.MODID, LOSBlocks.BLOCK_SIGNAL_PART_ANIMATED.getPath(id)), 0, states);
                cache.put(id, model);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        OBJModel model = cache.get(id);
        List<String> groupsWithoutWing = new ArrayList<>(model.groups());
        boolean wingsExist = groupsWithoutWing.containsAll(groupNames);
        groupsWithoutWing.removeAll(groupNames);

        state.scale(LOSBlocks.BLOCK_SIGNAL_PART.getScaling(id));
        state.translate(LOSBlocks.BLOCK_SIGNAL_PART.getTranslation(id).add(tsp.getOffset()));
        state.rotate(tsp.getBlockRotate(), 0, 1, 0);
        try (OBJRender.Binding vbo = model.binder().texture(tsp.getAnimationOrTextureName()).bind(state)) {

            vbo.draw(groupsWithoutWing);

            if (wingsExist) {
                Vec3d center = model.centerOfGroups(groupNames);
                center = new Vec3d(-center.x, -center.y, -center.z);
                Vec3d rotateYaw = center.rotateYaw(tsp.getPartRotate());

                GL11.glTranslated(0, -center.y, 0);
                GL11.glRotatef(tsp.getPartRotate(), 1, 0, 0);
                GL11.glTranslated(0, rotateYaw.y, 0);

                vbo.draw(groupNames);
            }
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