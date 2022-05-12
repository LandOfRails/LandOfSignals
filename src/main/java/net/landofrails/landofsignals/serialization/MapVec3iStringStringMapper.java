package net.landofrails.landofsignals.serialization;

import cam72cam.mod.math.Vec3i;
import cam72cam.mod.serialization.SerializationException;
import cam72cam.mod.serialization.TagCompound;
import cam72cam.mod.serialization.TagField;
import cam72cam.mod.serialization.TagMapper;

import java.util.AbstractMap;
import java.util.Map;

public class MapVec3iStringStringMapper implements TagMapper<Map<Vec3i, Map.Entry<String, String>>> {

    private static final String GROUPID = "groupid";
    private static final String GROUPSTATE = "groupstate";

    @Override
    public TagAccessor<Map<Vec3i, Map.Entry<String, String>>> apply(Class<Map<Vec3i, Map.Entry<String, String>>> type, String fieldName, TagField tag) throws SerializationException {
        return new TagAccessor<>(
                (nbt, map) -> {
                    if (map != null)
                        nbt.setMap(fieldName, map, MapVec3iStringStringMapper::posToString, value -> new TagCompound().setString(GROUPID, value.getKey()).setString(GROUPSTATE, value.getValue()));
                },
                nbt -> nbt.getMap(fieldName, MapVec3iStringStringMapper::stringToPos, valueTag -> new AbstractMap.SimpleEntry<>(valueTag.getString(GROUPID), valueTag.getString(GROUPSTATE)))
        );
    }

    private static String posToString(Vec3i pos) {
        return pos.x + ";" + pos.y + ";" + pos.z;
    }

    private static Vec3i stringToPos(String pos) {
        String[] xyz = pos.split(";");
        return new Vec3i(Integer.parseInt(xyz[0]), Integer.parseInt(xyz[1]), Integer.parseInt(xyz[2]));
    }

}
