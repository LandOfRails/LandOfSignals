package net.landofrails.stellwand.content.entities.storage;

import java.util.ArrayList;
import java.util.List;

import cam72cam.mod.math.Vec3i;
import cam72cam.mod.serialization.SerializationException;
import cam72cam.mod.serialization.TagCompound;
import cam72cam.mod.serialization.TagField;
import net.landofrails.stellwand.content.entities.function.BlockSenderFunctionEntity;
import net.landofrails.stellwand.content.entities.rendering.BlockSenderRenderEntity;
import net.landofrails.stellwand.storage.RunTimeStorage;
import net.landofrails.stellwand.utils.mapper.Vec3iListMapper;

public class BlockSenderStorageEntity extends BlockSenderFunctionEntity {

	// Global final variables
	public static final String MISSING = "missing";

	// TagFields
	@TagField
	public String contentPackBlockId = MISSING;
	@TagField
	public float rot = 0;
	@TagField(value = "senders", typeHint = Vec3i.class, mapper = Vec3iListMapper.class)
	public List<Vec3i> signals = new ArrayList<>();
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
	}

	@Override
	public void load(TagCompound nbt) throws SerializationException {
		super.load(nbt);
		RunTimeStorage.register(getPos(), this);
	}

	public void setContentBlockId(String id) {
		contentPackBlockId = id;
	}

	public String getContentBlockId() {
		return contentPackBlockId;
	}

	public boolean isCompatible(BlockSignalStorageEntity entity) {
		if (signals.isEmpty())
			return true;
		BlockSignalStorageEntity signal = getSignal(signals.get(0));
		return signal.contentPackBlockId.equals(entity.contentPackBlockId);
	}

	public BlockSignalStorageEntity getSignal(Vec3i pos) {
		return getWorld().getBlockEntity(pos, BlockSignalStorageEntity.class);
	}

}
