package net.landofrails.stellwand.content.entities.rendering;

import java.util.ArrayList;
import java.util.stream.Collectors;

import org.lwjgl.opengl.GL11;

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
			if (entity.contentPackBlockId != null && BlockSignalStorageEntity.getModels().containsKey(entity.contentPackBlockId)) {
				if (!BlockSignalStorageEntity.getRenderers().containsKey(entity.contentPackBlockId)) {
					OBJModel m = BlockSignalStorageEntity.getModels().get(entity.contentPackBlockId);
					BlockSignalStorageEntity.getRenderers().put(entity.contentPackBlockId, new OBJRender(m));
				}
				renderer = BlockSignalStorageEntity.getRenderers().get(entity.contentPackBlockId);
			} else {
				if (BlockSignalStorageEntity.getRenderers().containsKey(BlockSignalStorageEntity.MISSING)) {
					OBJModel m = BlockSignalStorageEntity.getModels().get(BlockSignalStorageEntity.MISSING);
					BlockSignalStorageEntity.getRenderers().put(BlockSignalStorageEntity.MISSING, new OBJRender(m));
				}
				renderer = BlockSignalStorageEntity.getRenderers().get(BlockSignalStorageEntity.MISSING);
			}
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

				// Muss erst translated werden.
				if (entity.isMarked())
					renderMarking();

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void renderMarking() {

		// 0.5 ist Blockkante
		final float margin = 0.51f;

		int[][] points = new int[][]{{+1, +1, +1}, {-1, -1, 1}, {-1, +1, -1}, {+1, -1, -1}};

		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_DEPTH_TEST);

		for (int i = 0; i < points.length; i++) {
			for (int o = 0; o < 3; o++) {

				GL11.glColor3f(1.0f, 1.0f, 0.2f);
				GL11.glLineWidth(10f);

				GL11.glBegin(GL11.GL_LINE_STRIP);

				GL11.glVertex3f(points[i][0] * margin, points[i][1] * margin, points[i][2] * margin);
				int x = points[i][0] * (o == 0 ? -1 : 1);
				int y = points[i][1] * (o == 1 ? -1 : 1);
				int z = points[i][2] * (o == 2 ? -1 : 1);
				GL11.glVertex3f(x * margin, y * margin, z * margin);

				GL11.glEnd();

			}
		}

		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glEnable(GL11.GL_TEXTURE_2D);


	}

}
