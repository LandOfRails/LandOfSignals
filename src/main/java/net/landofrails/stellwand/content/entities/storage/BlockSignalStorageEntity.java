package net.landofrails.stellwand.content.entities.storage;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import cam72cam.mod.ModCore;
import cam72cam.mod.math.Vec3i;
import cam72cam.mod.model.obj.OBJModel;
import cam72cam.mod.render.obj.OBJRender;
import cam72cam.mod.resource.Identifier;
import cam72cam.mod.serialization.TagField;
import net.landofrails.stellwand.Stellwand;
import net.landofrails.stellwand.content.entities.function.BlockSignalFunctionEntity;
import net.landofrails.stellwand.content.entities.rendering.BlockSignalRenderEntity;
import net.landofrails.stellwand.contentpacks.Content;
import net.landofrails.stellwand.contentpacks.entries.parent.ContentPackEntry;
import net.landofrails.stellwand.contentpacks.entries.signal.BlockSignalEntryBlock;

public class BlockSignalStorageEntity extends BlockSignalFunctionEntity {
	
	// Statics
	public static final String MISSING = "missing";
	protected static Map<String, OBJModel> models = new HashMap<>();
	protected static Map<String, OBJRender> renderers = new HashMap<>();
	protected static Map<String, float[]> rotations = new HashMap<>();
	protected static Map<String, float[]> translations = new HashMap<>();
	protected static Map<String, Map<String, String>> possibleModes = new HashMap<>();

	// TagFields
	@TagField("contentPackBlockId")
	public String contentPackBlockId = MISSING;

	@TagField("blockRotation")
	public float blockRotation = 0;

	@TagField("displayMode")
	public String displayMode;

	// Variables
	public Map<Vec3i, String> senderModes = new HashMap<>();

	private boolean marked = false;

	// Subclasses
	public BlockSignalRenderEntity renderEntity;

	public BlockSignalStorageEntity() {
		renderEntity = new BlockSignalRenderEntity(this);
	}

	public static void prepare() {

		Stellwand.debug("Preparing Signals..");

		try {
			Identifier id = new Identifier(Stellwand.DOMAIN, "models/block/others/blocknotfound/blocknotfound.obj");
			OBJModel m = new OBJModel(id, 0);
			models.put(MISSING, m);
			rotations.put(MISSING, new float[]{0, 0, 0});
			translations.put(MISSING, new float[]{0.5f, 0.5f, 0.5f});
		} catch (Exception e) {
			ModCore.Mod.error("Error while loading blocknotfound.obj: %s", e.getMessage());
		}
		// Add contentpack stuff
		for (Entry<ContentPackEntry, String> entry : Content.getBlockSignals().entrySet()) {
			try {
				ContentPackEntry cpe = entry.getKey();
				String packId = entry.getValue();
				String blockId = cpe.getBlockId(packId);
				BlockSignalEntryBlock block = cpe.getBlock(BlockSignalEntryBlock.class);
				String objPath = cpe.getModel();
				Identifier id = new Identifier("stellwand", objPath);
				OBJModel m = new OBJModel(id, 0);
				models.put(blockId, m);
				rotations.put(blockId, block.getRotation());
				translations.put(blockId, block.getTranslation());
				possibleModes.put(blockId, block.getModes());
			} catch (Exception e) {
				ModCore.Mod.error("Error while loading contentpack blocks: %s", e.toString());
				e.printStackTrace();
			}
		}
	}

	public String getContentPackBlockId() {
		return contentPackBlockId;
	}

	public void setContentBlockId(String contentPackBlockId) {
		this.contentPackBlockId = contentPackBlockId;
	}

	public Map<String, String> getPossibleModes() {
		if (possibleModes.containsKey(contentPackBlockId))
			return possibleModes.get(contentPackBlockId);
		return new HashMap<>();
	}

	public void setMode(String mode) {
		this.displayMode = mode;
	}

	public String getDisplayMode() {

		if (displayMode == null && possibleModes.containsKey(contentPackBlockId)) {

			Iterator<Entry<String, String>> it = getPossibleModes().entrySet().iterator();
			if (it.hasNext())
				displayMode = it.next().getValue();

		}

		return displayMode;
	}

	@SuppressWarnings("java:S1751")
	public void updateSignalMode() {

		String actualMode = "";
		
		for (Entry<String, String> entry : getPossibleModes().entrySet()) {
			actualMode = entry.getValue();
			break;
		}

		for (String possibleMode : getPossibleModes().values())
			if (senderModes.containsValue(possibleMode))
				actualMode = possibleMode;

		this.displayMode = actualMode;
		
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

	public boolean isMarked() {
		return marked;
	}

	public void setMarked(boolean marked) {
		this.marked = marked;
	}

}
