package net.landofrails.stellwand.utils.mapper;

import cam72cam.mod.serialization.TagCompound;
import cam72cam.mod.serialization.TagField;
import cam72cam.mod.serialization.TagMapper;

import java.util.Map;

public class MapStringStringMapper implements TagMapper<Map<String, String>> {

    private static final String KEYNAME = "entry";

    @Override
    public TagAccessor<Map<String, String>> apply(Class<Map<String, String>> type, String fieldName, TagField tag) {
        return new TagAccessor<>(
                (nbt, map) -> {
                    if (map != null)
                        nbt.setMap(fieldName, map, String::toString, value -> new TagCompound().setString(KEYNAME, value));
                },
                nbt -> nbt.getMap(fieldName, String::toString, valueTag -> valueTag.getString(KEYNAME))
        );
    }
}
