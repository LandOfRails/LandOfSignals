package net.landofrails.stellwand.content.entities.storage;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import cam72cam.mod.serialization.TagField;
import net.landofrails.stellwand.content.entities.function.BlockSignalFunctionEntity;
import net.landofrails.stellwand.content.entities.rendering.BlockSignalRenderEntity;
import net.landofrails.stellwand.storage.RunTimeStorage;

public class BlockSignalStorageEntity extends BlockSignalFunctionEntity {

	// Global final variables
	public static final String MISSING = "missing";

	// TagFields
	@TagField
	public String contentPackBlockId = MISSING;
	@TagField
	public float rot = 0;
	@TagField
	public UUID signalId = UUID.randomUUID();

	// Modes
	public Map<UUID, String> modes = new HashMap<>();
	
	// Subclasses
	public BlockSignalRenderEntity renderEntity;

	public BlockSignalStorageEntity() {
		renderEntity = new BlockSignalRenderEntity(this);
		RunTimeStorage.register(signalId, this);
	}

	public void setContentBlockId(String id) {
		contentPackBlockId = id;
	}

	public String getContentBlockId() {
		return contentPackBlockId;
	}

}
