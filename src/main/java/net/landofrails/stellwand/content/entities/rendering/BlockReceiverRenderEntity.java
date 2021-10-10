package net.landofrails.stellwand.content.entities.rendering;

import cam72cam.mod.MinecraftClient;
import cam72cam.mod.math.Vec3d;
import cam72cam.mod.model.obj.OBJModel;
import cam72cam.mod.render.OpenGL;
import cam72cam.mod.render.StandardModel;
import cam72cam.mod.render.obj.OBJRender;
import net.landofrails.stellwand.content.entities.storage.BlockReceiverStorageEntity;
import net.landofrails.stellwand.utils.compact.IRotatableBlockEntity;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class BlockReceiverRenderEntity implements IRotatableBlockEntity {

    private BlockReceiverStorageEntity entity;

    private OBJModel model;
    private OBJRender renderer;
    private float[] defaultRotation;
    private float[] defaultTranslation;

    public BlockReceiverRenderEntity(BlockReceiverStorageEntity entity) {
        this.entity = entity;
    }

    @Override
    public void setRotation(float rotation) {
        entity.blockRotation = -Math.round(rotation / 90) * 90f;
    }

    // Rendering

    public OBJModel getModel() {

        if (model == null) {
            if (entity.contentPackBlockId != null && BlockReceiverStorageEntity.getModels().containsKey(entity.contentPackBlockId))
                model = BlockReceiverStorageEntity.getModels().get(entity.contentPackBlockId);
            else
                model = BlockReceiverStorageEntity.getModels().get(BlockReceiverStorageEntity.MISSING);
        }

        return model;
    }

    public OBJRender getRenderer() {
        if (renderer == null) {
            if (entity.contentPackBlockId != null && BlockReceiverStorageEntity.getModels().containsKey(entity.contentPackBlockId)) {
                if (!BlockReceiverStorageEntity.getRenderers().containsKey(entity.contentPackBlockId)) {
                    OBJModel m = BlockReceiverStorageEntity.getModels().get(entity.contentPackBlockId);
                    BlockReceiverStorageEntity.getRenderers().put(entity.contentPackBlockId, new OBJRender(m));
                }
                renderer = BlockReceiverStorageEntity.getRenderers().get(entity.contentPackBlockId);
            } else {
                if (BlockReceiverStorageEntity.getRenderers().containsKey(BlockReceiverStorageEntity.MISSING)) {
                    OBJModel m = BlockReceiverStorageEntity.getModels().get(BlockReceiverStorageEntity.MISSING);
                    BlockReceiverStorageEntity.getRenderers().put(BlockReceiverStorageEntity.MISSING, new OBJRender(m));
                }
                renderer = BlockReceiverStorageEntity.getRenderers().get(BlockReceiverStorageEntity.MISSING);
            }
        }
        return renderer;
    }

    public float[] getTranslation() {
        if (defaultTranslation == null) {
            if (entity.contentPackBlockId != null && BlockReceiverStorageEntity.getTranslations().containsKey(entity.contentPackBlockId))
                defaultTranslation = BlockReceiverStorageEntity.getTranslations().get(entity.contentPackBlockId);
            else
                defaultTranslation = new float[]{0.5f, 0, 0.5f};
        }
        return defaultTranslation;
    }

    public float[] getRotation() {
        if (defaultRotation == null) {
            if (entity.contentPackBlockId != null && BlockReceiverStorageEntity.getRotations().containsKey(entity.contentPackBlockId))
                defaultRotation = BlockReceiverStorageEntity.getRotations().get(entity.contentPackBlockId);
            else
                defaultRotation = new float[]{0, 0, 0};
        }
        return defaultRotation;
    }

    public static StandardModel render(BlockReceiverStorageEntity entity) {
        return new StandardModel().addCustom(partialTicks -> renderStuff(entity, partialTicks));
    }

    @SuppressWarnings("java:S1172")
    private static void renderStuff(BlockReceiverStorageEntity entity, float partialTicks) {

        OBJModel model = entity.renderEntity.getModel();
        OBJRender renderer = entity.renderEntity.getRenderer();
        float[] translation = entity.renderEntity.getTranslation();
        float[] rotation = entity.renderEntity.getRotation();
        String mode = entity.getActive() ? "on" : "off";
        boolean wall = entity.getWallMounted();

        try {
            try (OpenGL.With matrix = OpenGL.matrix(); OpenGL.With tex = renderer.bindTexture()) {

                GL11.glTranslated(translation[0], (wall ? .5 : 0) + translation[1], translation[2]);
                GL11.glRotated(entity.blockRotation + rotation[1], 0, 1, 0);
                GL11.glRotated((wall ? -90 : 0) + rotation[0], 1, 0, 0);
                GL11.glRotated(rotation[2], 0, 0, 1);
                GL11.glTranslated(0, (wall ? -.5 : 0), 0);


                ArrayList<String> modes = model.groups().stream().filter(s -> s.startsWith(mode))
                        .collect(Collectors.toCollection(ArrayList::new));

                if (!modes.isEmpty()) {
                    ArrayList<String> generals = model.groups().stream().filter(s -> s.startsWith("general"))
                            .collect(Collectors.toCollection(ArrayList::new));
                    modes.addAll(generals);
                    renderer.drawGroups(modes);
                } else {
                    renderer.drawGroups(model.groups());
                }

                // Muss erst translated werden.
                if (entity.isMarked())
                    renderMarking(entity);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void renderMarking(BlockReceiverStorageEntity entity) {
        float[] color = entity.getMarkedColor();

        // 0.5 is edge of block
        final float margin = 0.51f;

        int[][] points = new int[][]{{+1, +1, +1}, {-1, -1, 1}, {-1, +1, -1}, {+1, -1, -1}};

        GL11.glPushAttrib(GL11.GL_ENABLE_BIT);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glDisable(GL11.GL_TEXTURE_2D);

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        double distance = MinecraftClient.getPlayer().getPosition().distanceTo(new Vec3d(entity.getPos()));
        float width = (10f / (float) distance) + 1;
        if (width < 0.01f)
            width = 0.01f;
        else if (width > 10f)
            width = 10f;
        GL11.glLineWidth(width);
        GL11.glDepthMask(false);

        for (int[] point : points) {
            for (short o = 0; o < 3; o++) {

                GL11.glColor3f(1, 1, 1);
                GL11.glColor3f(color[0], color[1], color[2]);

                GL11.glBegin(GL11.GL_LINE_STRIP);

                GL11.glVertex3f(point[0] * margin, point[1] * margin, point[2] * margin);
                int x = point[0] * (o == 0 ? -1 : 1);
                int y = point[1] * (o == 1 ? -1 : 1);
                int z = point[2] * (o == 2 ? -1 : 1);
                GL11.glVertex3f(x * margin, y * margin, z * margin);

                GL11.glEnd();

            }
        }

        GL11.glDepthMask(true);
        GL11.glPopAttrib();

    }

}
