package net.landofrails.landofsignals.serialization;

import cam72cam.mod.serialization.SerializationException;
import cam72cam.mod.serialization.TagCompound;
import cam72cam.mod.serialization.TagField;
import cam72cam.mod.serialization.TagMapper;

import java.util.Map;

public class MapStringStringArrayMapper implements TagMapper<Map<String, String[]>> {

    private static final String ARRAYID = "array";

    @Override
    public TagAccessor<Map<String, String[]>> apply(Class<Map<String, String[]>> type, String fieldName, TagField tag) throws SerializationException {
        return new TagAccessor<>(
                (nbt, map) -> writeMapStringStringArrayToTagCompound(nbt, fieldName, map),
                nbt -> readMapStringStringArrayFromTagCompound(nbt, fieldName)
        );
    }

    public static void writeMapStringStringArrayToTagCompound(TagCompound nbt, String fieldName, Map<String, String[]> map) {
        if (map != null) {
            nbt.setMap(fieldName, map, key -> key, value -> {
                TagCompound arrayTag = new TagCompound();
                StringArrayMapper.writeArrayToTag(ARRAYID, arrayTag, value);
                return arrayTag;
            });
        }
    }

    @SuppressWarnings("java:S1168")
    public static Map<String, String[]> readMapStringStringArrayFromTagCompound(TagCompound nbt, String fieldName) {
        if (nbt.hasKey(fieldName)) {
            return nbt.getMap(fieldName, key -> key, arrayTag ->
                    StringArrayMapper.readArrayFromTag(ARRAYID, arrayTag)
            );
        }
        return null;
    }

}
