package net.landofrails.stellwand.utils.mapper;

import java.util.List;
import java.util.UUID;

import cam72cam.mod.serialization.SerializationException;
import cam72cam.mod.serialization.TagCompound;
import cam72cam.mod.serialization.TagField;
import cam72cam.mod.serialization.TagMapper;

public class UUIDListMapper implements TagMapper<List<UUID>> {

	private static final String KEYNAME = "entry";

	@SuppressWarnings("java:S2293")
	@Override
	public TagAccessor<List<UUID>> apply(Class<List<UUID>> type,
			String fieldName, TagField tag) throws SerializationException {
		// @formatter:off
		return new TagAccessor<>(
				(nbt, list) -> {
					// From List to Tag
					if (list != null)
						nbt.setList(fieldName, list, uuid -> new TagCompound().setUUID(KEYNAME, uuid));
				}, 
				    // From Tag to List
					nbt -> nbt.getList(fieldName, uuidTag -> uuidTag.getUUID(KEYNAME))
		);
		// @formatter:on
	}

}
