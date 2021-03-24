package net.landofrails.stellwand.content.entities.storage;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import cam72cam.mod.ModCore;
import cam72cam.mod.model.obj.OBJModel;
import cam72cam.mod.render.obj.OBJRender;
import cam72cam.mod.resource.Identifier;
import cam72cam.mod.serialization.TagField;
import net.landofrails.stellwand.Stellwand;
import net.landofrails.stellwand.content.entities.function.BlockFillerFunctionEntity;
import net.landofrails.stellwand.content.entities.rendering.BlockFillerRenderEntity;
import net.landofrails.stellwand.content.loader.Content;
import net.landofrails.stellwand.content.loader.ContentPackEntry;
import net.landofrails.stellwand.content.loader.ContentPackEntry.ContentPackEntryBlock;

public class BlockFillerStorageEntity extends BlockFillerFunctionEntity {

	// Statics
	public static final String MISSING = "missing";

	protected static Map<String, OBJModel> models = new HashMap<>();
	protected static Map<String, OBJRender> renderers = new HashMap<>();
	protected static Map<String, float[]> rotations = new HashMap<>();
	protected static Map<String, float[]> translations = new HashMap<>();

	// TagFields
	@TagField("contentPackBlockId")
	public String contentPackBlockId = MISSING;

	@TagField("blockRotation")
	public float blockRotation = 0f;

	// Subclasses
	public BlockFillerRenderEntity renderEntity;

	public BlockFillerStorageEntity() {
		renderEntity = new BlockFillerRenderEntity(this);
	}

	public String getContentPackBlockId() {
		return contentPackBlockId;
	}

	public void setContentBlockId(String contentPackBlockId) {
		this.contentPackBlockId = contentPackBlockId;
	}

	public static void prepare(boolean isClient) {

		ModCore.Mod.info("Preparing Filler..");

		try {
			Identifier id = new Identifier(Stellwand.DOMAIN, "models/block/others/blocknotfound/blocknotfound.obj");
			OBJModel m = new OBJModel(id, 0);
			models.put(MISSING, m);
			rotations.put(MISSING, new float[]{0, 0, 0});
			translations.put(MISSING, new float[]{0.5f, 0.5f, 0.5f});
			if (isClient)
				renderers.put(MISSING, new OBJRender(m));
		} catch (Exception e) {
			ModCore.Mod.error("Error while loading blocknotfound.obj: %s", e.getMessage());
		}
		// Add contentpack stuff
		for (Entry<ContentPackEntry, String> entry : Content.getBlockFillers().entrySet()) {
			try {
				ContentPackEntry cpe = entry.getKey();
				String packId = entry.getValue();
				String blockId = cpe.getBlockId(packId);
				ContentPackEntryBlock block = cpe.getBlock();
				String objPath = cpe.getModel();
				Identifier id = new Identifier("stellwand", objPath);
				OBJModel m = new OBJModel(id, 0);
				models.put(blockId, m);
				rotations.put(blockId, block.getRotation());
				translations.put(blockId, block.getTranslation());
				if (isClient)
					renderers.put(blockId, new OBJRender(m));
			} catch (Exception e) {
				ModCore.Mod.error("Error while loading contentpack blocks: %s", e.getMessage());
			}
		}

	}

	// Getters
	public static Map<String, OBJModel> getModels() {
		return models;
	}

	public static Map<String, OBJRender> getRenderers() {
		return renderers;
	}

	public static Map<String, float[]> getRotations() {
		return rotations;
	}

	public static Map<String, float[]> getTranslations() {
		return translations;
	}

}
