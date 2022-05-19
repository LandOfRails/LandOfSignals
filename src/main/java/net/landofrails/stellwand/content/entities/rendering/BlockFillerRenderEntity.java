package net.landofrails.stellwand.content.entities.rendering;

import cam72cam.mod.model.obj.OBJModel;
import cam72cam.mod.render.StandardModel;
import cam72cam.mod.render.obj.OBJRender;
import cam72cam.mod.render.opengl.RenderState;
import net.landofrails.stellwand.content.entities.storage.BlockFillerStorageEntity;
import net.landofrails.stellwand.utils.compact.IRotatableBlockEntity;

public class BlockFillerRenderEntity implements IRotatableBlockEntity {

    // Rendering
    private OBJModel model;
    private float[] defaultRotation;
    private float[] defaultTranslation;

    private BlockFillerStorageEntity entity;

    public BlockFillerRenderEntity(BlockFillerStorageEntity entity) {
        this.entity = entity;
    }

    @Override
    public void setRotation(float rotation) {
        entity.blockRotation = -Math.round(rotation / 90) * 90f;
    }

    // Rendering

    public OBJModel getModel() {

        if (model == null) {
            if (entity.contentPackBlockId != null && BlockFillerStorageEntity.getModels().containsKey(entity.contentPackBlockId))
                model = BlockFillerStorageEntity.getModels().get(entity.contentPackBlockId);
            else
                model = BlockFillerStorageEntity.getModels().get(BlockFillerStorageEntity.MISSING);
        }

        return model;
    }

    public float[] getTranslation() {
        if (defaultTranslation == null) {
            if (entity.contentPackBlockId != null && BlockFillerStorageEntity.getTranslations().containsKey(entity.contentPackBlockId))
                defaultTranslation = BlockFillerStorageEntity.getTranslations().get(entity.contentPackBlockId);
            else
                defaultTranslation = new float[]{0.5f, 0, 0.5f};
        }
        return defaultTranslation;
    }

    public float[] getRotation() {
        if (defaultRotation == null) {
            if (entity.contentPackBlockId != null && BlockFillerStorageEntity.getRotations().containsKey(entity.contentPackBlockId))
                defaultRotation = BlockFillerStorageEntity.getRotations().get(entity.contentPackBlockId);
            else
                defaultRotation = new float[]{0, 0, 0};
        }
        return defaultRotation;
    }

    public static StandardModel render(BlockFillerStorageEntity entity) {
        return new StandardModel().addCustom((state, partialTicks) -> renderStuff(entity, state));
    }

    private static void renderStuff(BlockFillerStorageEntity entity, RenderState state) {

        OBJModel model = entity.renderEntity.getModel();
        float[] translation = entity.renderEntity.getTranslation();
        float[] rotation = entity.renderEntity.getRotation();

        state.translate(translation[0], translation[1], translation[2]);

        state.rotate(rotation[0], 1, 0, 0);
        state.rotate(entity.blockRotation + rotation[1], 0, 1, 0);
        state.rotate(rotation[2], 0, 0, 1);

        try {
            try (OBJRender.Binding vbo = model.vbo.bind(state)) {
                vbo.draw();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
