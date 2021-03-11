package net.landofrails.stellwand.utils.mapper;

import java.util.List;

import cam72cam.mod.math.Vec3i;
import cam72cam.mod.serialization.SerializationException;
import cam72cam.mod.serialization.TagCompound;
import cam72cam.mod.serialization.TagField;
import cam72cam.mod.serialization.TagMapper;

public class Vec3iListMapper implements TagMapper<List<Vec3i>> {

	private static final String KEYNAME = "entry";

	@SuppressWarnings("java:S2293")
	@Override
	public TagAccessor<List<Vec3i>> apply(Class<List<Vec3i>> type,
			String fieldName, TagField tag) throws SerializationException {
		// @formatter:off
		return new TagAccessor<>(
				(nbt, list) -> {
					// From List to Tag
					if (list != null)
						nbt.setList(fieldName, list, vec -> new TagCompound().setVec3i(KEYNAME, vec));
				}, 
				    // From Tag to List
					nbt -> nbt.getList(fieldName, vecTag -> vecTag.getVec3i(KEYNAME))
		);
		// @formatter:on
	}

}