package net.landofrails.landofsignals.serialization;

import cam72cam.mod.serialization.SerializationException;
import cam72cam.mod.serialization.TagField;
import cam72cam.mod.serialization.TagMapper;

public class CustomTagMapper implements TagMapper {
    @Override
    public TagAccessor apply(Class type, String fieldName, TagField tag) throws SerializationException {
//        return new TagAccessor<>((d, o) -> d.setList(fieldName, o, o1 -> {
//            return new TagCompound()
//        }), d -> d.getList(fieldName));
        return null;
    }
}
