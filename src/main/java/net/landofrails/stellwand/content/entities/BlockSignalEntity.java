package net.landofrails.stellwand.content.entities;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Nullable;

import org.lwjgl.opengl.GL11;

import cam72cam.mod.ModCore;
import cam72cam.mod.block.BlockEntity;
import cam72cam.mod.item.ItemStack;
import cam72cam.mod.model.obj.OBJModel;
import cam72cam.mod.render.OpenGL;
import cam72cam.mod.render.StandardModel;
import cam72cam.mod.render.obj.OBJRender;
import cam72cam.mod.resource.Identifier;
import cam72cam.mod.serialization.TagField;
import net.landofrails.landofsignals.LandOfSignals;
import net.landofrails.stellwand.content.loader.Content;
import net.landofrails.stellwand.content.loader.ContentPack.ContentPackEntry;
import net.landofrails.stellwand.content.loader.ContentPack.ContentPackEntry.ContentPackEntryBlock;

public class BlockSignalEntity extends BlockEntity {

	// Static values
	public static final String MISSING = "missing";
	private static Map<String, OBJModel> models = new HashMap<>();
	private static Map<String, OBJRender> renderers = new HashMap<>();
	private static Map<String, float[]> rotations = new HashMap<>();
	private static Map<String, float[]> translations = new HashMap<>();
	private static Map<String, Map<String, String>> modes = new HashMap<>();

	// Storing data
	@TagField
	private String contentPackBlockId;
	@Nullable
	private String mode;

	// Rendering
	private OBJModel model;
	private OBJRender renderer;
	private float[] rotation;
	private float[] translation;

	public BlockSignalEntity() {
		if (models.isEmpty() && renderers.isEmpty()) {
			try {
				Identifier id = new Identifier(LandOfSignals.MODID, "");
				OBJModel m = new OBJModel(id, 0);
				models.put(MISSING, m);
				renderers.put(MISSING, new OBJRender(m));
				rotations.put(MISSING, new float[] { 0, 0, 0 });
				translations.put(MISSING, new float[] { 0, 0, 0 });
			} catch (Exception e) {
				ModCore.Mod.error(e.getMessage());
			}
			// Add contentpack stuff
			for (Entry<ContentPackEntry, String> entry : Content.getEntries().entrySet()) {
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

	public OBJModel getModel() {

		if (model == null) {
			if (contentPackBlockId != null && models.containsKey(contentPackBlockId))
				model = models.get(contentPackBlockId);
			else
				model = models.get(MISSING);
		}

		return model;
	}

	public OBJRender getRenderer() {
		if (renderer == null) {
			if (contentPackBlockId != null && renderers.containsKey(contentPackBlockId))
				renderer = renderers.get(contentPackBlockId);
			else
				renderer = renderers.get(MISSING);
		}
		return renderer;
	}

	public float[] getTranslation() {
		if (translation == null) {
			if (contentPackBlockId != null && translations.containsKey(contentPackBlockId))
				translation = translations.get(contentPackBlockId);
			else
				translation = new float[] { 0.5f, 0, 0.5f };
		}
		return translation;
	}

	public float[] getRotation() {
		if (rotation == null) {
			if (contentPackBlockId != null && rotations.containsKey(contentPackBlockId))
				rotation = rotations.get(contentPackBlockId);
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
		return mode;
	}

	@Override
	public ItemStack onPick() {

		return null;
	}

	// Rendering
	public static StandardModel render(BlockSignalEntity entity) {
		return new StandardModel().addCustom(partialTicks -> renderStuff(entity, partialTicks));
	}

	@SuppressWarnings("java:S1172")
	private static void renderStuff(BlockSignalEntity entity, float partialTicks) {

		OBJModel model = entity.getModel();
		OBJRender renderer = entity.getRenderer();
		float[] translation = entity.getTranslation();
		float[] rotation = entity.getRotation();
		String mode = entity.mode;

		try {
			if (renderer == null || model == null) {
				return;
			}
			try (OpenGL.With matrix = OpenGL.matrix(); OpenGL.With tex = renderer.bindTexture()) {
				GL11.glTranslated(translation[0], translation[1], translation[2]);
				GL11.glRotated(1, rotation[0], rotation[1], rotation[2]);

				if (mode == null) {
					renderer.draw();
				} else {
					renderer.drawGroups(Collections.singleton("general"));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
