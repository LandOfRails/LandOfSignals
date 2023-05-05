package net.landofrails.landofsignals.serialization;

import cam72cam.mod.math.Vec3i;
import cam72cam.mod.serialization.TagCompound;
import cam72cam.mod.serialization.TagField;
import cam72cam.mod.serialization.TagMapper;

import java.util.Map;

public class MapVec3iStringMapper implements TagMapper<Map<Vec3i, String>> {

    public static final String KEYNAME = "entry";

    @Override
    public TagAccessor<Map<Vec3i, String>> apply(Class<Map<Vec3i, String>> type, String fieldName, TagField tag) {

        return new TagAccessor<>(
                (nbt, map) -> {
                    if (map != null)
                        nbt.setMap(fieldName, map, MapVec3iStringMapper::posToString, value -> new TagCompound().setString(KEYNAME, value));
                },
                nbt -> nbt.getMap(fieldName, MapVec3iStringMapper::stringToPos, valueTag -> valueTag.getString(KEYNAME))
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
