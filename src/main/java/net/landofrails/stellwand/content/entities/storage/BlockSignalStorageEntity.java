package net.landofrails.stellwand.content.entities.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import cam72cam.mod.serialization.TagField;
import net.landofrails.stellwand.content.entities.function.BlockSignalFunctionEntity;
import net.landofrails.stellwand.content.entities.rendering.BlockSignalRenderEntity;
import net.landofrails.stellwand.utils.mapper.UUIDListMapper;

public class BlockSignalStorageEntity extends BlockSignalFunctionEntity {

	// Global final variables
	public static final String MISSING = "missing";

	// TagFields
	@TagField
	public String contentPackBlockId = MISSING;
	@TagField
	public float rot = 0;
	@TagField(value = "senders", typeHint = UUID.class, mapper = UUIDListMapper.class)
	public List<UUID> senders = new ArrayList<>();

	// Subclasses
	public BlockSignalRenderEntity renderEntity;

	public BlockSignalStorageEntity() {
		renderEntity = new BlockSignalRenderEntity(this);
	}

}
