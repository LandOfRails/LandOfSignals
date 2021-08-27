package net.landofrails.stellwand.content.entities.rendering;

import cam72cam.mod.model.obj.OBJModel;
import cam72cam.mod.render.OpenGL;
import cam72cam.mod.render.StandardModel;
import cam72cam.mod.render.obj.OBJRender;
import net.landofrails.stellwand.content.entities.storage.BlockMultisignalStorageEntity;
import net.landofrails.stellwand.utils.compact.IRotatableBlockEntity;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

public class BlockMultisignalRenderEntity implements IRotatableBlockEntity {

    private BlockMultisignalStorageEntity entity;

    private OBJModel model;
    private OBJRender renderer;
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

    public OBJRender getRenderer() {
        if (renderer == null) {
            if (entity.contentPackBlockId != null && BlockMultisignalStorageEntity.getModels().containsKey(entity.contentPackBlockId)) {
                if (!BlockMultisignalStorageEntity.getRenderers().containsKey(entity.contentPackBlockId)) {
                    OBJModel m = BlockMultisignalStorageEntity.getModels().get(entity.contentPackBlockId);
                    BlockMultisignalStorageEntity.getRenderers().put(entity.contentPackBlockId, new OBJRender(m));
                }
                renderer = BlockMultisignalStorageEntity.getRenderers().get(entity.contentPackBlockId);
            } else {
                if (BlockMultisignalStorageEntity.getRenderers().containsKey(BlockMultisignalStorageEntity.MISSING)) {
                    OBJModel m = BlockMultisignalStorageEntity.getModels().get(BlockMultisignalStorageEntity.MISSING);
                    BlockMultisignalStorageEntity.getRenderers().put(BlockMultisignalStorageEntity.MISSING, new OBJRender(m));
                }
                renderer = BlockMultisignalStorageEntity.getRenderers().get(BlockMultisignalStorageEntity.MISSING);
            }
        }
        return renderer;
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
        return new StandardModel().addCustom(partialTicks -> renderStuff(entity, partialTicks));
    }

    @SuppressWarnings("java:S1172")
    private static void renderStuff(BlockMultisignalStorageEntity entity, float partialTicks) {

        OBJModel model = entity.getRenderEntity().getModel();
        OBJRender renderer = entity.getRenderEntity().getRenderer();
        float[] translation = entity.getRenderEntity().getTranslation();
        float[] rotation = entity.getRenderEntity().getRotation();
        Map<String, String> displayModes = entity.getDisplayModes();

        try {
            try (OpenGL.With matrix = OpenGL.matrix(); OpenGL.With tex = renderer.bindTexture()) {

                GL11.glTranslated(translation[0], translation[1], translation[2]);

                GL11.glRotated(rotation[0], 1, 0, 0);
                GL11.glRotated(entity.blockRotation + rotation[1], 0, 1, 0);
                GL11.glRotated(rotation[2], 0, 0, 1);

                if (displayModes == null || displayModes.isEmpty()) {
                    renderer.draw();
                } else {

                    ArrayList<String> modes = new ArrayList<>();

                    displayModes.forEach((signalGroup, mode) -> model.groups().stream().filter(s -> s.startsWith(mode)).forEach(modes::add));

                    if (!modes.isEmpty()) {
                        ArrayList<String> generals = model.groups().stream().filter(s -> s.startsWith("general"))
                                .collect(Collectors.toCollection(ArrayList::new));
                        modes.addAll(generals);
                        renderer.drawGroups(modes);
                    } else {
                        renderer.drawGroups(model.groups());
                    }

                }

                // Muss erst translated werden.
                if (entity.isMarked())
                    renderMarking(entity.getMarkedColor());

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void renderMarking(float[] color) {

        // 0.5 is edge of block
        final float margin = 0.51f;

        int[][] points = new int[][]{{+1, +1, +1}, {-1, -1, 1}, {-1, +1, -1}, {+1, -1, -1}};

        GL11.glDisable(GL11.GL_TEXTURE_2D);

        for (int[] point : points) {
            for (short o = 0; o < 3; o++) {

                GL11.glColor3f(1, 1, 1);
                GL11.glColor3f(color[0], color[1], color[2]);
                GL11.glLineWidth(10f);

                GL11.glBegin(GL11.GL_LINE_STRIP);

                GL11.glVertex3f(point[0] * margin, point[1] * margin, point[2] * margin);
                int x = point[0] * (o == 0 ? -1 : 1);
                int y = point[1] * (o == 1 ? -1 : 1);
                int z = point[2] * (o == 2 ? -1 : 1);
                GL11.glVertex3f(x * margin, y * margin, z * margin);

                GL11.glEnd();

            }
        }

        GL11.glEnable(GL11.GL_TEXTURE_2D);

    }

}
