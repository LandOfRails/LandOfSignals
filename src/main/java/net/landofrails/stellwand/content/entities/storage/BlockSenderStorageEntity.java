package net.landofrails.stellwand.content.entities.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import cam72cam.mod.serialization.TagField;
import net.landofrails.stellwand.content.entities.function.BlockSenderFunctionEntity;
import net.landofrails.stellwand.content.entities.rendering.BlockSenderRenderEntity;
import net.landofrails.stellwand.storage.RunTimeStorage;
import net.landofrails.stellwand.utils.mapper.UUIDListMapper;

public class BlockSenderStorageEntity extends BlockSenderFunctionEntity {

	// Global final variables
	public static final String MISSING = "missing";

	// TagFields
	@TagField
	public String contentPackBlockId = MISSING;
	@TagField
	public float rot = 0;
	@TagField
	public UUID senderId = UUID.randomUUID();
	@TagField(value = "senders", typeHint = UUID.class, mapper = UUIDListMapper.class)
	public List<UUID> signals = new ArrayList<>();
	@TagField
	public String modePowerOff;
	@TagField
	public String modePowerOn;

	// Variables
	public boolean hasPower = false;

	// Subclasses
	public BlockSenderRenderEntity renderEntity;

	public BlockSenderStorageEntity() {
		renderEntity = new BlockSenderRenderEntity(this);
		RunTimeStorage.register(senderId, this);
	}

	public void setContentBlockId(String id) {
		contentPackBlockId = id;
	}

	public String getContentBlockId() {
		return contentPackBlockId;
	}

}
