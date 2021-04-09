package net.landofrails.stellwand.utils.mapper;

import cam72cam.mod.serialization.SerializationException;
import cam72cam.mod.serialization.TagField;
import cam72cam.mod.serialization.TagMapper;
import net.landofrails.stellwand.content.messages.EMessage;

public class EMessageTagMapper implements TagMapper<EMessage> {

	@Override
	public TagAccessor<EMessage> apply(Class<EMessage> type, String fieldName, TagField tag) throws SerializationException {
		return new TagAccessor<>((nbt, emessage) -> {
			// From EMessage to Tag
			if (emessage != null)
				nbt.setString(fieldName, emessage.name());
		},
				// From Tag to EMessage
				nbt -> EMessage.valueOf(nbt.getString(fieldName)));
	}

}