package net.landofrails.stellwand.utils.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import cam72cam.mod.ModCore;
import cam72cam.mod.serialization.SerializationException;
import cam72cam.mod.serialization.TagCompound;
import cam72cam.mod.serialization.TagField;
import cam72cam.mod.serialization.TagMapper;

public class UUIDListMapper implements TagMapper<List<UUID>> {

	private static final String COUNTNAME = "count";
	private static final String LISTPREFIX = "uuids";
	private static final String KEYPREFIX = "entry";

	@SuppressWarnings("java:S2293")
	@Override
	public TagAccessor<List<UUID>> apply(Class<List<UUID>> type,
			String fieldName, TagField tag) throws SerializationException {

		return new TagAccessor<>((nbt, list) -> {
			// From List to Tag
			ModCore.Mod.warn("List to Tag");
			if (list != null && !list.isEmpty()) {
				TagCompound listTag = new TagCompound();
				listTag.setInteger(COUNTNAME, list.size());
				for (int i = 0; i < listTag.getInteger(COUNTNAME); i++) {
					String key = KEYPREFIX + i;
					listTag.setUUID(key, list.get(i));
				}
				nbt.set(LISTPREFIX, listTag);
				ModCore.Mod.warn("List to Tag finished");
			}
		}, (nbt, world) -> {
			// From Tag to List
			ModCore.Mod.warn("Tag to List");
			List<UUID> uuids = new ArrayList<>();
			if (nbt.hasKey(LISTPREFIX)) {
				TagCompound listTag = nbt.get(LISTPREFIX);
				int count = listTag.getInteger(COUNTNAME);
				for (int i = 0; i < count; i++) {
					uuids.add(listTag.getUUID(KEYPREFIX + i));
				}
				ModCore.Mod.warn("Tag to List finished");
			}
			return uuids;
			}
		);

	}

}
