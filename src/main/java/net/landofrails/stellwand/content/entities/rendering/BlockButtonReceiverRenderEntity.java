package net.landofrails.stellwand.content.entities.rendering;

import cam72cam.mod.model.obj.OBJModel;
import cam72cam.mod.render.StandardModel;
import cam72cam.mod.render.obj.OBJRender;
import cam72cam.mod.render.opengl.RenderState;
import net.landofrails.stellwand.content.entities.storage.BlockButtonReceiverStorageEntity;
import net.landofrails.stellwand.utils.compact.IRotatableBlockEntity;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class BlockButtonReceiverRenderEntity implements IRotatableBlockEntity {

    private BlockButtonReceiverStorageEntity entity;

    private OBJModel model;
    private float[] defaultRotation;
    private float[] defaultTranslation;

    public BlockButtonReceiverRenderEntity(BlockButtonReceiverStorageEntity entity) {
        this.entity = entity;
    }

    @Override
    public void setRotation(float rotation) {
        entity.blockRotation = -Math.round(rotation / 90) * 90f;
    }

    // Rendering

    public OBJModel getModel() {

        if (model == null) {
            if (entity.contentPackBlockId != null && BlockButtonReceiverStorageEntity.getModels().containsKey(entity.contentPackBlockId))
                model = BlockButtonReceiverStorageEntity.getModels().get(entity.contentPackBlockId);
            else
                model = BlockButtonReceiverStorageEntity.getModels().get(BlockButtonReceiverStorageEntity.MISSING);
        }

        return model;
    }

    public float[] getTranslation() {
        if (defaultTranslation == null) {
            if (entity.contentPackBlockId != null && BlockButtonReceiverStorageEntity.getTranslations().containsKey(entity.contentPackBlockId))
                defaultTranslation = BlockButtonReceiverStorageEntity.getTranslations().get(entity.contentPackBlockId);
            else
                defaultTranslation = new float[]{0.5f, 0, 0.5f};
        }
        return defaultTranslation;
    }

    public float[] getRotation() {
        if (defaultRotation == null) {
            if (entity.contentPackBlockId != null && BlockButtonReceiverStorageEntity.getRotations().containsKey(entity.contentPackBlockId))
                defaultRotation = BlockButtonReceiverStorageEntity.getRotations().get(entity.contentPackBlockId);
            else
                defaultRotation = new float[]{0, 0, 0};
        }
        return defaultRotation;
    }

    public static StandardModel render(BlockButtonReceiverStorageEntity entity) {
        return new StandardModel().addCustom((state, partialTicks) -> renderStuff(entity, state));
    }

    @SuppressWarnings("java:S1172")
    private static void renderStuff(BlockButtonReceiverStorageEntity entity, RenderState state) {

        OBJModel model = entity.renderEntity.getModel();
        float[] translation = entity.renderEntity.getTranslation();
        float[] rotation = entity.renderEntity.getRotation();
        String mode = entity.getActive() ? "on" : "off";
        boolean wall = entity.getWallMounted();

        state.translate(translation[0], (wall ? .5 : 0) + translation[1], translation[2]);
        state.rotate(entity.blockRotation + rotation[1], 0, 1, 0);
        state.rotate((wall ? -90 : 0) + rotation[0], 1, 0, 0);
        state.rotate(rotation[2], 0, 0, 1);
        state.translate(0, (wall ? -.5 : 0), 0);

        try {
            try (OBJRender.Binding vbo = model.vbo.bind(state)) {

                ArrayList<String> modes = model.groups().stream().filter(s -> s.startsWith(mode))
                        .collect(Collectors.toCollection(ArrayList::new));

                if (!modes.isEmpty()) {
                    ArrayList<String> generals = model.groups().stream().filter(s -> s.startsWith("general"))
                            .collect(Collectors.toCollection(ArrayList::new));
                    modes.addAll(generals);
                    vbo.draw(modes);
                } else {
                    vbo.draw(model.groups());
                }

                // Muss erst translated werden.
                if (entity.isMarked())
                    renderMarking(entity);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void renderMarking(BlockButtonReceiverStorageEntity entity) {

        // FIXME Implement marking system with new rendering engine
        // GL11 is not supported anymore, what now?

    }

}
