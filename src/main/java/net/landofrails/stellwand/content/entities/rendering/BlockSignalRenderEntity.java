package net.landofrails.stellwand.content.entities.rendering;

import static net.landofrails.stellwand.content.entities.storage.BlockSignalStorageEntity.MISSING;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import org.lwjgl.opengl.GL11;

import cam72cam.mod.ModCore;
import cam72cam.mod.model.obj.OBJModel;
import cam72cam.mod.render.OpenGL;
import cam72cam.mod.render.StandardModel;
import cam72cam.mod.render.obj.OBJRender;
import cam72cam.mod.resource.Identifier;
import net.landofrails.landofsignals.LandOfSignals;
import net.landofrails.stellwand.content.entities.storage.BlockSignalStorageEntity;
import net.landofrails.stellwand.content.loader.Content;
import net.landofrails.stellwand.content.loader.ContentPackEntry;
import net.landofrails.stellwand.content.loader.ContentPackEntry.ContentPackEntryBlock;
import net.landofrails.stellwand.utils.compact.IRotatableBlockEntity;

public class BlockSignalRenderEntity implements IRotatableBlockEntity {

	private BlockSignalStorageEntity entity;

	public BlockSignalRenderEntity(BlockSignalStorageEntity entity) {
		this.entity = entity;
	}

	// Static values
	private static Map<String, OBJModel> models = new HashMap<>();
	private static Map<String, OBJRender> renderers = new HashMap<>();
	private static Map<String, float[]> rotations = new HashMap<>();
	private static Map<String, float[]> translations = new HashMap<>();
	protected static Map<String, Map<String, String>> modes = new HashMap<>();

	// Rendering
	private OBJModel model;
	private OBJRender renderer;
	private float[] rotation;
	private float[] translation;
	@Nullable
	private String mode;

	public OBJModel getModel() {

		if (model == null) {
			if (entity.contentPackBlockId != null
					&& models.containsKey(entity.contentPackBlockId))
				model = models.get(entity.contentPackBlockId);
			else
				model = models.get(MISSING);
		}

		return model;
	}

	public OBJRender getRenderer() {
		if (renderer == null) {
			if (entity.contentPackBlockId != null
					&& renderers.containsKey(entity.contentPackBlockId))
				renderer = renderers.get(entity.contentPackBlockId);
			else
				renderer = renderers.get(MISSING);
		}
		return renderer;
	}

	public float[] getTranslation() {
		if (translation == null) {
			if (entity.contentPackBlockId != null
					&& translations.containsKey(entity.contentPackBlockId))
				translation = translations.get(entity.contentPackBlockId);
			else
				translation = new float[] { 0.5f, 0, 0.5f };
		}
		return translation;
	}

	public float[] getRotation() {
		if (rotation == null) {
			if (entity.contentPackBlockId != null
					&& rotations.containsKey(entity.contentPackBlockId))
				rotation = rotations.get(entity.contentPackBlockId);
			else
				rotation = new float[] { 0, 0, 0 };
		}
		return rotation;
	}

	/**
	 * 
	 * Return the group to render. If everything should be rendered its null.
	 * 
	 * @return
	 */
	public String getMode() {

		if (mode == null) {
			Map<String, String> m = modes.get(entity.contentPackBlockId);
			if (m != null && !m.isEmpty()) {
				mode = m.values().iterator().next();
			}
		}
		return mode;
	}

	public Map<String, String> getModes() {

		return modes.get(entity.contentPackBlockId);

	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	// Rendering
	public static StandardModel render(BlockSignalStorageEntity entity) {
		return new StandardModel().addCustom(partialTicks -> renderStuff(entity, partialTicks));
	}

	public static void check() {
		if (models.isEmpty() && renderers.isEmpty()) {
			try {
				Identifier id = new Identifier(LandOfSignals.MODID, "models/block/stellwand/blocknotfound.obj");
				OBJModel m = new OBJModel(id, 0);
				models.put(MISSING, m);
				renderers.put(MISSING, new OBJRender(m));
				rotations.put(MISSING, new float[] { 0, 0, 0 });
				translations.put(MISSING, new float[] { 0.5f, 0.5f, 0.5f });
			} catch (Exception e) {
				ModCore.Mod.error(e.getMessage());
			}
			// Add contentpack stuff
			for (Entry<ContentPackEntry, String> entry : Content.getBlockSignals().entrySet()) {
				try {
					ContentPackEntry cpe = entry.getKey();
					String packId = entry.getValue();
					String blockId = cpe.getBlockId(packId);
					ContentPackEntryBlock block = cpe.getBlock();
					String objPath = cpe.getModel();
					Identifier id = new Identifier("stellwand", objPath);
					OBJModel m = new OBJModel(id, 0);
					models.put(blockId, m);
					renderers.put(blockId, new OBJRender(m));
					rotations.put(blockId, block.getRotation());
					translations.put(blockId, block.getTranslation());
					modes.put(blockId, block.getModes());
				} catch (Exception e) {
					ModCore.Mod.error(e.getMessage());
				}
			}
		}
	}

	@SuppressWarnings("java:S1172")
	private static void renderStuff(BlockSignalStorageEntity entity,
			float partialTicks) {

		check();

		OBJModel model = entity.renderEntity.getModel();
		OBJRender renderer = entity.renderEntity.getRenderer();
		float[] translation = entity.renderEntity.getTranslation();
		float[] rotation = entity.renderEntity.getRotation();
		String mode = entity.renderEntity.getMode();

		try {
			if (renderer == null || model == null) {
				ModCore.warn("Block has no renderer!!");
				return;
			}
			try (OpenGL.With matrix = OpenGL.matrix(); OpenGL.With tex = renderer.bindTexture()) {
				GL11.glTranslated(translation[0], translation[1], translation[2]);

				GL11.glRotated(rotation[0], 1, 0, 0);
				GL11.glRotated(entity.renderEntity.getRot() + rotation[1], 0, 1,
						0);
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

	public float getRot() {
		return entity.rot;
	}

	@Override
	public void setRotation(float rotationYawHead) {
		entity.rot = -Math.round(rotationYawHead / 90) * 90f;
	}

}
