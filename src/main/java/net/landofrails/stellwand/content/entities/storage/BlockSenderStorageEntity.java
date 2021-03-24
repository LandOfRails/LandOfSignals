package net.landofrails.stellwand.content.entities.storage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import cam72cam.mod.ModCore;
import cam72cam.mod.math.Vec3i;
import cam72cam.mod.model.obj.OBJModel;
import cam72cam.mod.render.obj.OBJRender;
import cam72cam.mod.resource.Identifier;
import cam72cam.mod.serialization.TagField;
import net.landofrails.stellwand.Stellwand;
import net.landofrails.stellwand.content.entities.function.BlockSenderFunctionEntity;
import net.landofrails.stellwand.content.entities.rendering.BlockSenderRenderEntity;
import net.landofrails.stellwand.contentpacks.Content;
import net.landofrails.stellwand.contentpacks.entries.parent.ContentPackEntry;
import net.landofrails.stellwand.contentpacks.entries.parent.ContentPackEntryBlock;
import net.landofrails.stellwand.utils.mapper.Vec3iListMapper;

public class BlockSenderStorageEntity extends BlockSenderFunctionEntity {

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
	public float blockRotation = 0;

	@TagField(value = "signals", typeHint = Vec3i.class, mapper = Vec3iListMapper.class)
	public List<Vec3i> signals = new ArrayList<>();

	@TagField("modePowerOff")
	public String modePowerOff;

	@TagField("modePowerOn")
	public String modePowerOn;

	@TagField("hasPower")
	public boolean hasPower = false;

	// Variables
	private BlockSignalStorageEntity signalEntity;

	// Subclasses
	public BlockSenderRenderEntity renderEntity;

	public BlockSenderStorageEntity() {
		renderEntity = new BlockSenderRenderEntity(this);
	}

	public static void prepare(boolean isClient) {

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
		for (Entry<ContentPackEntry, String> entry : Content.getBlockSenders().entrySet()) {
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
	public String getContentPackBlockId() {
		return contentPackBlockId;
	}

	public void setContentBlockId(String contentPackBlockId) {
		this.contentPackBlockId = contentPackBlockId;
	}

	public boolean isCompatible(BlockSignalStorageEntity otherSignal) {
		if (signals.isEmpty())
			return true;

		Iterator<Vec3i> signal = signals.iterator();
		BlockSignalStorageEntity signalTile = getWorld().getBlockEntity(signal.next(), BlockSignalStorageEntity.class);
		return signalTile.getContentPackBlockId().equals(otherSignal.getContentPackBlockId());
	}

	public void setSignal(BlockSignalStorageEntity signalEntity) {
		this.signalEntity = signalEntity;
	}

	public BlockSignalStorageEntity getSignal() {
		return this.signalEntity;
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
