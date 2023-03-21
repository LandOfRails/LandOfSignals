package net.landofrails.landofsignals.serialization;

import cam72cam.mod.serialization.TagCompound;
import cam72cam.mod.serialization.TagField;
import cam72cam.mod.serialization.TagMapper;

import java.util.Arrays;

public class StringArrayMapper implements TagMapper<String[]> {

    private static final String KEYNAME = "entry";

    @Override
    public TagAccessor<String[]> apply(Class<String[]> type, String fieldName, TagField tag) {
        return new TagAccessor<>((nbt, array) ->
                writeArrayToTag(fieldName, nbt, array)
                , nbt ->
                readArrayFromTag(fieldName, nbt)
        );
    }

    public static void writeArrayToTag(String fieldName, TagCompound tag, String[] array) {
        if (array != null) {
            tag.setList(fieldName, Arrays.asList(array), element -> new TagCompound().setString(KEYNAME, EmptyStringMapper.toNullString(element)));
        }
    }

    public static String[] readArrayFromTag(String fieldName, TagCompound tag) {
        if (tag.hasKey(fieldName)) {
            return tag.getList(fieldName, strTag -> EmptyStringMapper.fromNullString(strTag.getString(KEYNAME))).toArray(new String[0]);
        }
        return null;
    }

}
