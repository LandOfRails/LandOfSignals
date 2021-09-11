package net.landofrails.stellwand.utils.mapper;

import cam72cam.mod.serialization.TagField;
import cam72cam.mod.serialization.TagMapper;

public class EmptyStringMapper implements TagMapper<String> {
    public static final String NULLSTRING = "nullstring";

    @Override
    public TagAccessor<String> apply(Class<String> type, String fieldName, TagField tag) {

        return new TagAccessor<>(
                (nbt, text) ->
                        nbt.setString(fieldName, EmptyStringMapper.toNullString(text))
                ,
                nbt -> EmptyStringMapper.fromNullString(nbt.getString(fieldName))
        );
    }

    public static String toNullString(String from) {
        return from != null ? from : NULLSTRING;
    }

    public static String fromNullString(String from) {
        return from.equalsIgnoreCase(NULLSTRING) ? null : from;
    }
}
