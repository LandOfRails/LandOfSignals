package net.landofrails.stellwand.content.network;

import java.util.Map;

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

	@TagField(value = "modes", mapper = Vec3iStringMapMapper.class)
	private Map<Vec3i, String> modes;

	public ChangeSignalModes() {

	}

	public ChangeSignalModes(Vec3i pos, Map<Vec3i, String> modes) {
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
	public static class Vec3iStringMapMapper implements TagMapper<Map<Vec3i, String>> {
		
		private static final String ENTRYKEY = "ENTRY";

		@Override
		public TagAccessor<Map<Vec3i, String>> apply(Class<Map<Vec3i, String>> type, String fieldName, TagField tag) throws SerializationException {
			return new TagAccessor<>((nbt, map) -> 
				nbt.setMap(fieldName, map, Vec3iStringMapper::toString, string -> new TagCompound().setString(ENTRYKEY, string))
			, nbt -> 
				nbt.getMap(fieldName, Vec3iStringMapper::fromString, stringNBT -> stringNBT.getString(ENTRYKEY))
			);
		}

	}
	// @formatter:on

	public static class Vec3iStringMapper {

		private Vec3iStringMapper() {

		}

		public static String toString(Vec3i vec) {
			return vec.x + "," + vec.y + "," + vec.z;
		}

		public static Vec3i fromString(String vec) {
			Integer[] coords = new Integer[3];
			for (int i = 0; i < 3; i++)
				coords[i] = Integer.parseInt(vec.split(",")[i]);
			return new Vec3i(coords[0], coords[1], coords[2]);
		}

	}
}
