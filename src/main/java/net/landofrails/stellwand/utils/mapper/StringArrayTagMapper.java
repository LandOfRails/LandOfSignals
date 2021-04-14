package net.landofrails.stellwand.utils.mapper;

import java.util.Arrays;

import cam72cam.mod.serialization.SerializationException;
import cam72cam.mod.serialization.TagCompound;
import cam72cam.mod.serialization.TagField;
import cam72cam.mod.serialization.TagMapper;

public class StringArrayTagMapper implements TagMapper<String[]> {

	private static final String KEYNAME = "entry";

	@Override
	public TagAccessor<String[]> apply(Class<String[]> type, String fieldName, TagField tag) throws SerializationException {
		return new TagAccessor<String[]>((nbt, array) -> {
			nbt.setList(fieldName, Arrays.asList(array), (element) -> new TagCompound().setString(KEYNAME, element));
		}, (nbt) -> {
			return nbt.getList(fieldName, strTag -> strTag.getString(KEYNAME)).toArray(new String[0]);
		});
	}

}
