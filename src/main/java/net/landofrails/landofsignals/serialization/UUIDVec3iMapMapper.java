package net.landofrails.landofsignals.serialization;

import cam72cam.mod.math.Vec3i;
import cam72cam.mod.serialization.SerializationException;
import cam72cam.mod.serialization.TagCompound;
import cam72cam.mod.serialization.TagField;
import cam72cam.mod.serialization.TagMapper;

import java.util.Map;
import java.util.UUID;

public class UUIDVec3iMapMapper implements TagMapper<Map<UUID, Vec3i>> {

    private static final String KEYNAME = "entry";

    @Override
    public TagAccessor<Map<UUID, Vec3i>> apply(final Class<Map<UUID, Vec3i>> type, final String fieldName, final TagField tag) throws SerializationException {
        return new TagAccessor<>(
                (nbt, map) -> {
                    // From Map to Tag
                    if (map != null)
                        nbt.setMap(fieldName, map, UUID::toString, pos -> new TagCompound().setVec3i(KEYNAME, pos));
                },
                // From Tag to Map
                nbt -> nbt.getMap(fieldName, UUID::fromString, pos -> pos.getVec3i(KEYNAME))
        );
    }
}
