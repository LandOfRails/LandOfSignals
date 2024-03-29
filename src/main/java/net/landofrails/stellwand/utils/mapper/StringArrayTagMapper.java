package net.landofrails.stellwand.utils.mapper;

import cam72cam.mod.serialization.TagCompound;
import cam72cam.mod.serialization.TagField;
import cam72cam.mod.serialization.TagMapper;

import java.util.Arrays;

public class StringArrayTagMapper implements TagMapper<String[]> {

    private static final String KEYNAME = "entry";

    @Override
    public TagAccessor<String[]> apply(Class<String[]> type, String fieldName, TagField tag) {
        return new TagAccessor<>((nbt, array) -> {
            if (array != null)
                nbt.setList(fieldName, Arrays.asList(array), element -> new TagCompound().setString(KEYNAME, element));
        }, nbt -> {
            if (nbt.hasKey(fieldName))
                return nbt.getList(fieldName, strTag -> strTag.getString(KEYNAME)).toArray(new String[0]);
            return new String[0];
        });
    }

}
