package net.landofrails.stellwand.utils.mapper;

import cam72cam.mod.serialization.TagField;
import cam72cam.mod.serialization.TagMapper;
import net.landofrails.stellwand.utils.compact.SignalContainer;

public class SignalContainerMapper implements TagMapper<SignalContainer<?>> {

    @Override
    public TagAccessor<SignalContainer<?>> apply(Class<SignalContainer<?>> type, String fieldName, TagField tag) {
        return new TagAccessor<>(
                (nbt, container) -> {
                    // From Container to Tag
                    if (container != null)
                        nbt.set(fieldName, container.toTagCompound());
                },
                // From Tag to Container
                nbt -> SignalContainer.of(nbt.get(fieldName))
        );
    }
}
