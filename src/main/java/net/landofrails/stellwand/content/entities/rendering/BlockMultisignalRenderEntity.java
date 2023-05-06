package net.landofrails.stellwand.content.entities.rendering;

import cam72cam.mod.MinecraftClient;
import cam72cam.mod.math.Vec3d;
import cam72cam.mod.model.obj.OBJModel;
import cam72cam.mod.render.StandardModel;
import cam72cam.mod.render.obj.OBJRender;
import cam72cam.mod.render.opengl.RenderState;
import net.landofrails.stellwand.content.entities.storage.BlockMultisignalStorageEntity;
import net.landofrails.stellwand.utils.compact.IRotatableBlockEntity;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

public class BlockMultisignalRenderEntity implements IRotatableBlockEntity {

    private BlockMultisignalStorageEntity entity;

    private OBJModel model;
    private float[] defaultRotation;
    private float[] defaultTranslation;

    public BlockMultisignalRenderEntity(BlockMultisignalStorageEntity entity) {
        this.entity = entity;
    }

    @Override
    public void setRotation(float rotation) {
        entity.blockRotation = -Math.round(rotation / 90) * 90f;
    }

    // Rendering

    public OBJModel getModel() {

        if (model == null) {
            if (entity.contentPackBlockId != null && BlockMultisignalStorageEntity.getModels().containsKey(entity.contentPackBlockId))
                model = BlockMultisignalStorageEntity.getModels().get(entity.contentPackBlockId);
            else
                model = BlockMultisignalStorageEntity.getModels().get(BlockMultisignalStorageEntity.MISSING);
        }

        return model;
    }

    public float[] getTranslation() {
        if (defaultTranslation == null) {
            if (entity.contentPackBlockId != null && BlockMultisignalStorageEntity.getTranslations().containsKey(entity.contentPackBlockId))
                defaultTranslation = BlockMultisignalStorageEntity.getTranslations().get(entity.contentPackBlockId);
            else
                defaultTranslation = new float[]{0.5f, 0, 0.5f};
        }
        return defaultTranslation;
    }

    public float[] getRotation() {
        if (defaultRotation == null) {
            if (entity.contentPackBlockId != null && BlockMultisignalStorageEntity.getRotations().containsKey(entity.contentPackBlockId))
                defaultRotation = BlockMultisignalStorageEntity.getRotations().get(entity.contentPackBlockId);
            else
                defaultRotation = new float[]{0, 0, 0};
        }
        return defaultRotation;
    }

    public static StandardModel render(BlockMultisignalStorageEntity entity) {
        return new StandardModel().addCustom((state, partialTicks) -> renderStuff(entity, state));
    }

    @SuppressWarnings("java:S1172")
    private static void renderStuff(BlockMultisignalStorageEntity entity, RenderState state) {

        OBJModel model = entity.getRenderEntity().getModel();
        float[] translation = entity.getRenderEntity().getTranslation();
        float[] rotation = entity.getRenderEntity().getRotation();
        Map<String, String> displayModes = entity.getDisplayModes();

        state.translate(translation[0], translation[1], translation[2]);

        state.rotate(rotation[0], 1, 0, 0);
        state.rotate(entity.blockRotation + rotation[1], 0, 1, 0);
        state.rotate(rotation[2], 0, 0, 1);

        try {
            try (OBJRender.Binding vbo = model.binder().bind(state)) {



                if (displayModes == null || displayModes.isEmpty()) {
                    vbo.draw();
                } else {

                    ArrayList<String> modes = new ArrayList<>();

                    displayModes.forEach((signalGroup, mode) -> model.groups().stream().filter(s -> s.startsWith(mode)).forEach(modes::add));

                    if (!modes.isEmpty()) {
                        ArrayList<String> generals = model.groups().stream().filter(s -> s.startsWith("general"))
                                .collect(Collectors.toCollection(ArrayList::new));
                        modes.addAll(generals);
                        vbo.draw(modes);
                    } else {
                        vbo.draw(model.groups());
                    }

                }

                // Muss erst translated werden.
                if (entity.isMarked())
                    renderMarking(entity);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void renderMarking(BlockMultisignalStorageEntity entity) {
        // TODO Unclear if this still wortks
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

        float[] translation = entity.getRenderEntity().getTranslation();
        GL11.glTranslated(0.0, 0.5 - translation[1], 0.0);

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
