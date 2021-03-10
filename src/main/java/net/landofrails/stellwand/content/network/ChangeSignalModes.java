package net.landofrails.stellwand.content.network;

import java.util.Map;
import java.util.UUID;

import cam72cam.mod.math.Vec3i;
import cam72cam.mod.net.Packet;
import cam72cam.mod.serialization.SerializationException;
import cam72cam.mod.serialization.TagCompound;
import cam72cam.mod.serialization.TagField;
import cam72cam.mod.serialization.TagMapper;
import net.landofrails.stellwand.content.entities.storage.BlockSignalStorageEntity;

public class ChangeSignalModes extends Packet {

	@TagField("pos")
	private Vec3i pos;

	@TagField(value = "modes", mapper = UUIDStringMapMapper.class)
	private Map<UUID, String> modes;

	public ChangeSignalModes() {

	}

	public ChangeSignalModes(Vec3i pos, Map<UUID, String> modes) {
		this.pos = pos;
		this.modes = modes;
	}

	@Override
	protected void handle() {
		BlockSignalStorageEntity signalEntity = getWorld().getBlockEntity(pos,
				BlockSignalStorageEntity.class);
		signalEntity.modes = modes;
		signalEntity.update();
	}

	// @formatter:off
	public static class UUIDStringMapMapper implements TagMapper<Map<UUID, String>> {
		
		private static final String ENTRYKEY = "ENTRY";

		@Override
		public TagAccessor<Map<UUID, String>> apply(Class<Map<UUID, String>> type, String fieldName, TagField tag) throws SerializationException {
			return new TagAccessor<>((nbt, map) -> 
				nbt.setMap(fieldName, map, UUID::toString, string -> new TagCompound().setString(ENTRYKEY, string))
			, nbt -> 
				nbt.getMap(fieldName, UUID::fromString, stringNBT -> stringNBT.getString(ENTRYKEY))
			);
		}

	}
	// @formatter:on

}
