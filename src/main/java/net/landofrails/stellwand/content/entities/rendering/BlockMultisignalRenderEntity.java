package net.landofrails.stellwand.content.entities.rendering;

import cam72cam.mod.model.obj.OBJModel;
import cam72cam.mod.render.StandardModel;
import cam72cam.mod.render.obj.OBJRender;
import cam72cam.mod.render.opengl.RenderState;
import net.landofrails.stellwand.content.entities.storage.BlockMultisignalStorageEntity;
import net.landofrails.stellwand.utils.compact.IRotatableBlockEntity;

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
        // TODO Check if possible
    }

}
