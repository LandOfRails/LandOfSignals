package net.landofrails.stellwand.content.entities.rendering;

import java.util.ArrayList;
import java.util.stream.Collectors;

import org.lwjgl.opengl.GL11;

import cam72cam.mod.ModCore;
import cam72cam.mod.model.obj.OBJModel;
import cam72cam.mod.render.OpenGL;
import cam72cam.mod.render.StandardModel;
import cam72cam.mod.render.obj.OBJRender;
import net.landofrails.stellwand.content.entities.storage.BlockSignalStorageEntity;
import net.landofrails.stellwand.utils.compact.IRotatableBlockEntity;

public class BlockSignalRenderEntity implements IRotatableBlockEntity {

	private BlockSignalStorageEntity entity;

	private OBJModel model;
	private OBJRender renderer;
	private float[] defaultRotation;
	private float[] defaultTranslation;

	public BlockSignalRenderEntity(BlockSignalStorageEntity entity) {
		this.entity = entity;
	}

	@Override
	public void setRotation(float rotation) {
		entity.blockRotation = -Math.round(rotation / 90) * 90f;
	}

	// Rendering

	public OBJModel getModel() {

		if (model == null) {
			if (entity.contentPackBlockId != null && BlockSignalStorageEntity.getModels().containsKey(entity.contentPackBlockId))
				model = BlockSignalStorageEntity.getModels().get(entity.contentPackBlockId);
			else
				model = BlockSignalStorageEntity.getModels().get(BlockSignalStorageEntity.MISSING);
		}

		return model;
	}

	public OBJRender getRenderer() {
		if (renderer == null) {
			if (entity.contentPackBlockId != null && BlockSignalStorageEntity.getRenderers().containsKey(entity.contentPackBlockId))
				renderer = BlockSignalStorageEntity.getRenderers().get(entity.contentPackBlockId);
			else
				renderer = BlockSignalStorageEntity.getRenderers().get(BlockSignalStorageEntity.MISSING);
		}
		return renderer;
	}

	public float[] getTranslation() {
		if (defaultTranslation == null) {
			if (entity.contentPackBlockId != null && BlockSignalStorageEntity.getTranslations().containsKey(entity.contentPackBlockId))
				defaultTranslation = BlockSignalStorageEntity.getTranslations().get(entity.contentPackBlockId);
			else
				defaultTranslation = new float[]{0.5f, 0, 0.5f};
		}
		return defaultTranslation;
	}

	public float[] getRotation() {
		if (defaultRotation == null) {
			if (entity.contentPackBlockId != null && BlockSignalStorageEntity.getRotations().containsKey(entity.contentPackBlockId))
				defaultRotation = BlockSignalStorageEntity.getRotations().get(entity.contentPackBlockId);
			else
				defaultRotation = new float[]{0, 0, 0};
		}
		return defaultRotation;
	}

	public static StandardModel render(BlockSignalStorageEntity entity) {
		return new StandardModel().addCustom(partialTicks -> renderStuff(entity, partialTicks));
	}

	@SuppressWarnings("java:S1172")
	private static void renderStuff(BlockSignalStorageEntity entity, float partialTicks) {

		OBJModel model = entity.renderEntity.getModel();
		OBJRender renderer = entity.renderEntity.getRenderer();
		float[] translation = entity.renderEntity.getTranslation();
		float[] rotation = entity.renderEntity.getRotation();
		String mode = entity.getDisplayMode();

		try {
			if (renderer == null || model == null) {
				ModCore.warn("Block has no renderer: %s!", entity.getPos().toString());
				return;
			}
			try (OpenGL.With matrix = OpenGL.matrix(); OpenGL.With tex = renderer.bindTexture()) {
				GL11.glTranslated(translation[0], translation[1], translation[2]);

				GL11.glRotated(rotation[0], 1, 0, 0);
				GL11.glRotated(entity.blockRotation + rotation[1], 0, 1, 0);
				GL11.glRotated(rotation[2], 0, 0, 1);

				if (mode == null) {
					renderer.draw();
				} else {

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

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
