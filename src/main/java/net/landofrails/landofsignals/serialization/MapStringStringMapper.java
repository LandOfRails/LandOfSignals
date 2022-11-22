package net.landofrails.landofsignals.serialization;

import cam72cam.mod.serialization.TagCompound;
import cam72cam.mod.serialization.TagField;
import cam72cam.mod.serialization.TagMapper;

import java.util.Map;

public class MapStringStringMapper implements TagMapper<Map<String, String>> {

    public static final String KEYNAME = "entry";

    @Override
    public TagAccessor<Map<String, String>> apply(Class<Map<String, String>> type, String fieldName, TagField tag) {

        return new TagAccessor<>(
                (nbt, map) -> {
                    if (map != null)
                        nbt.setMap(fieldName, map, EmptyStringMapper::toNullString, value -> new TagCompound().setString(KEYNAME, value));
                },
                nbt -> nbt.getMap(fieldName, EmptyStringMapper::fromNullString, valueTag -> valueTag.getString(KEYNAME))
        );
    }


}
