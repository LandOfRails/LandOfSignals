package net.landofrails.stellwand.content.entities.rendering;

import cam72cam.mod.model.obj.OBJModel;
import cam72cam.mod.render.StandardModel;
import cam72cam.mod.render.obj.OBJRender;
import cam72cam.mod.render.opengl.RenderState;
import net.landofrails.stellwand.content.entities.storage.BlockSenderStorageEntity;
import net.landofrails.stellwand.utils.compact.IRotatableBlockEntity;

public class BlockSenderRenderEntity implements IRotatableBlockEntity {

	// Variables
	private OBJModel model;
	private OBJRender renderer;
	private float[] defaultRotation;
	private float[] defaultTranslation;

	// Entity
	private BlockSenderStorageEntity entity;

	public BlockSenderRenderEntity(BlockSenderStorageEntity entity) {
		this.entity = entity;
	}

	@Override
	public void setRotation(float rotation) {
		entity.blockRotation = -Math.round(rotation / 90) * 90f;
	}

	// Rendering

	public OBJModel getModel() {

		if (model == null) {
			if (entity.contentPackBlockId != null && BlockSenderStorageEntity.getModels().containsKey(entity.contentPackBlockId))
				model = BlockSenderStorageEntity.getModels().get(entity.contentPackBlockId);
			else
				model = BlockSenderStorageEntity.getModels().get(BlockSenderStorageEntity.MISSING);
		}

		return model;
	}

	public float[] getTranslation() {
		if (defaultTranslation == null) {
			if (entity.contentPackBlockId != null && BlockSenderStorageEntity.getTranslations().containsKey(entity.contentPackBlockId))
				defaultTranslation = BlockSenderStorageEntity.getTranslations().get(entity.contentPackBlockId);
			else
				defaultTranslation = new float[]{0.5f, 0, 0.5f};
		}
		return defaultTranslation;
	}

	public float[] getRotation() {
		if (defaultRotation == null) {
			if (entity.contentPackBlockId != null && BlockSenderStorageEntity.getRotations().containsKey(entity.contentPackBlockId))
				defaultRotation = BlockSenderStorageEntity.getRotations().get(entity.contentPackBlockId);
			else
				defaultRotation = new float[]{0, 0, 0};
		}
		return defaultRotation;
	}

	public static StandardModel render(BlockSenderStorageEntity entity) {
		return new StandardModel().addCustom((state, partialTicks) -> renderStuff(entity, state));
	}

	private static void renderStuff(BlockSenderStorageEntity entity, RenderState state) {

		OBJModel model = entity.renderEntity.getModel();
		float[] translation = entity.renderEntity.getTranslation();
		float[] rotation = entity.renderEntity.getRotation();

		state.translate(translation[0], translation[1], translation[2]);

		state.rotate(rotation[0], 1, 0, 0);
		state.rotate(entity.blockRotation + rotation[1], 0, 1, 0);
		state.rotate(rotation[2], 0, 0, 1);

		try {
			try (OBJRender.Binding vbo = model.binder().bind(state)) {
				vbo.draw();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
