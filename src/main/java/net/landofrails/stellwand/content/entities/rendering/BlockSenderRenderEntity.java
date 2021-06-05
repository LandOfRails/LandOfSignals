package net.landofrails.stellwand.content.entities.rendering;

import org.lwjgl.opengl.GL11;

import cam72cam.mod.model.obj.OBJModel;
import cam72cam.mod.render.OpenGL;
import cam72cam.mod.render.StandardModel;
import cam72cam.mod.render.obj.OBJRender;
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

	public OBJRender getRenderer() {
		if (renderer == null) {
			if (entity.contentPackBlockId != null && BlockSenderStorageEntity.getModels().containsKey(entity.contentPackBlockId)) {
				if (!BlockSenderStorageEntity.getRenderers().containsKey(entity.contentPackBlockId)) {
					OBJModel m = BlockSenderStorageEntity.getModels().get(entity.contentPackBlockId);
					BlockSenderStorageEntity.getRenderers().put(entity.contentPackBlockId, new OBJRender(m));
				}
				renderer = BlockSenderStorageEntity.getRenderers().get(entity.contentPackBlockId);
			} else {
				if (BlockSenderStorageEntity.getRenderers().containsKey(BlockSenderStorageEntity.MISSING)) {
					OBJModel m = BlockSenderStorageEntity.getModels().get(BlockSenderStorageEntity.MISSING);
					BlockSenderStorageEntity.getRenderers().put(BlockSenderStorageEntity.MISSING, new OBJRender(m));
				}
				renderer = BlockSenderStorageEntity.getRenderers().get(BlockSenderStorageEntity.MISSING);
			}
		}
		return renderer;
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
		return new StandardModel().addCustom(partialTicks -> renderStuff(entity, partialTicks));
	}

	private static void renderStuff(BlockSenderStorageEntity entity, float partialTicks) {

		OBJRender renderer = entity.renderEntity.getRenderer();
		float[] translation = entity.renderEntity.getTranslation();
		float[] rotation = entity.renderEntity.getRotation();

		try {
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
