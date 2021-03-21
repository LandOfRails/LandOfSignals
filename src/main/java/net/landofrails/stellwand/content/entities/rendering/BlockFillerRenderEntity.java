package net.landofrails.stellwand.content.entities.rendering;

import org.lwjgl.opengl.GL11;

import cam72cam.mod.ModCore;
import cam72cam.mod.model.obj.OBJModel;
import cam72cam.mod.render.OpenGL;
import cam72cam.mod.render.StandardModel;
import cam72cam.mod.render.obj.OBJRender;
import net.landofrails.stellwand.content.entities.storage.BlockFillerStorageEntity;
import net.landofrails.stellwand.utils.compact.IRotatableBlockEntity;

public class BlockFillerRenderEntity implements IRotatableBlockEntity {

	// Rendering
	private OBJModel model;
	private OBJRender renderer;
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
			if (entity.contentPackBlockId != null && BlockFillerStorageEntity.models.containsKey(entity.contentPackBlockId))
				model = BlockFillerStorageEntity.models.get(entity.contentPackBlockId);
			else
				model = BlockFillerStorageEntity.models.get(BlockFillerStorageEntity.MISSING);
		}

		return model;
	}

	public OBJRender getRenderer() {
		if (renderer == null) {
			if (entity.contentPackBlockId != null && BlockFillerStorageEntity.renderers.containsKey(entity.contentPackBlockId))
				renderer = BlockFillerStorageEntity.renderers.get(entity.contentPackBlockId);
			else
				renderer = BlockFillerStorageEntity.renderers.get(BlockFillerStorageEntity.MISSING);
		}
		return renderer;
	}

	public float[] getTranslation() {
		if (defaultTranslation == null) {
			if (entity.contentPackBlockId != null && BlockFillerStorageEntity.translations.containsKey(entity.contentPackBlockId))
				defaultTranslation = BlockFillerStorageEntity.translations.get(entity.contentPackBlockId);
			else
				defaultTranslation = new float[]{0.5f, 0, 0.5f};
		}
		return defaultTranslation;
	}

	public float[] getRotation() {
		if (defaultRotation == null) {
			if (entity.contentPackBlockId != null && BlockFillerStorageEntity.rotations.containsKey(entity.contentPackBlockId))
				defaultRotation = BlockFillerStorageEntity.rotations.get(entity.contentPackBlockId);
			else
				defaultRotation = new float[]{0, 0, 0};
		}
		return defaultRotation;
	}

	public static StandardModel render(BlockFillerStorageEntity entity) {
		return new StandardModel().addCustom(partialTicks -> renderStuff(entity, partialTicks));
	}

	private static void renderStuff(BlockFillerStorageEntity entity, float partialTicks) {

		OBJModel model = entity.renderEntity.getModel();
		OBJRender renderer = entity.renderEntity.getRenderer();
		float[] translation = entity.renderEntity.getTranslation();
		float[] rotation = entity.renderEntity.getRotation();

		try {
			if (renderer == null || model == null) {
				ModCore.warn("Block has no renderer!!");
				return;
			}
			try (OpenGL.With matrix = OpenGL.matrix(); OpenGL.With tex = renderer.bindTexture()) {
				GL11.glTranslated(translation[0], translation[1], translation[2]);

				GL11.glRotated(rotation[0], 1, 0, 0);
				GL11.glRotated(entity.blockRotation + rotation[1], 0, 1, 0);
				GL11.glRotated(rotation[2], 0, 0, 1);

				renderer.draw();

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
