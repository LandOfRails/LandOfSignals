package net.landofrails.stellwand.content.network;

import cam72cam.mod.math.Vec3i;
import cam72cam.mod.net.Packet;
import cam72cam.mod.serialization.TagCompound;
import cam72cam.mod.serialization.TagField;
import cam72cam.mod.serialization.TagMapper;
import net.landofrails.stellwand.utils.compact.SignalContainer;
import net.landofrails.stellwand.utils.mapper.EmptyStringMapper;

import java.util.Map;
import java.util.stream.Stream;

public class ChangeSignalModes extends Packet {

    @TagField("pos")
    private Vec3i pos;

    @TagField(value = "modes", mapper = MapVec3iMapStringStringMapper.class)
    private Map<Vec3i, Map<String, String>> modes;

    public ChangeSignalModes() {

    }

    public ChangeSignalModes(Vec3i signalPos, Map<Vec3i, Map<String, String>> senderModes) {
        this.pos = signalPos;
        this.modes = senderModes;
    }

    @Override
    protected void handle() {
        getWorld().keepLoaded(pos);

        if (SignalContainer.isSignal(getWorld(), pos)) {
            SignalContainer<?> signalContainer = SignalContainer.of(getWorld(), pos);
            signalContainer.setSenderModes(modes);
            signalContainer.updateSignalModes();
        }
    }

    // @formatter:off
	public static class MapVec3iMapStringStringMapper implements TagMapper<Map<Vec3i, Map<String, String>>> {
		
		private static final String ENTRYKEY = "ENTRY";

		@Override
		public TagAccessor<Map<Vec3i, Map<String, String>>> apply(Class<Map<Vec3i, Map<String, String>>> type, String fieldName, TagField tag) {
			return new TagAccessor<>((nbt, map) -> {
			    if(map != null)
                    nbt.setMap(fieldName, map, MapVec3iMapStringStringMapper::vec3iToString, value ->
                        new TagCompound().setMap(ENTRYKEY, value, EmptyStringMapper::toNullString, val -> new TagCompound().setString(ENTRYKEY, val))
                    );
            }
			, nbt ->
				nbt.getMap(fieldName, MapVec3iMapStringStringMapper::stringToVec3i, mapTag ->
						mapTag.getMap(ENTRYKEY, EmptyStringMapper::fromNullString, valTag -> valTag.getString(ENTRYKEY))
				)
			);
		}

		public static String vec3iToString(Vec3i vec3i){
			return String.format("%d;%d;%d", vec3i.x, vec3i.y, vec3i.z);
		}

		public static Vec3i stringToVec3i(String vec){
			Integer[] coordinates = Stream.of(vec.split(";")).map(Integer::new).toArray(Integer[]::new);
			return new Vec3i(coordinates[0], coordinates[1], coordinates[2]);
		}

	}
	// @formatter:on

}
