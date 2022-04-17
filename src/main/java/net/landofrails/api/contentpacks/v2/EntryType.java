package net.landofrails.api.contentpacks.v2;

import cam72cam.mod.item.CustomItem;
import cam72cam.mod.item.ItemStack;
import net.landofrails.landofsignals.LOSItems;
import net.landofrails.stellwand.contentpacks.entries.parent.ContentPackEntry;
import net.landofrails.stellwand.contentpacks.entries.sender.BlockSenderEntry;
import net.landofrails.stellwand.contentpacks.entries.signal.BlockSignalEntry;

import java.util.function.Supplier;

public enum EntryType {

    // @formatter:off
    BLOCKSIGNAL(BlockSignalEntry.class, () -> LOSItems.ITEM_SIGNAL_PART),
    BLOCKSENDER(BlockSenderEntry.class, () -> LOSItems.ITEM_SIGNAL_BOX),
    // TODO implement
    BLOCKSIGN(null, () -> null),
    BLOCKDECO(null, () -> null);
    // @formatter:on
    private final Class<? extends ContentPackEntry> typeClass;
    private final Supplier<CustomItem> itemSupplier;

    EntryType(Class<? extends ContentPackEntry> typeClass, Supplier<CustomItem> itemSupplier) {
        this.typeClass = typeClass;
        this.itemSupplier = itemSupplier;
    }

    public Class<? extends ContentPackEntry> getTypeClass() {
        return typeClass;
    }

    public CustomItem getCustomItem() {
        return itemSupplier.get();
    }

    public static EntryType getEntryTypeFromItem(ItemStack itemStack) {

        for (EntryType type : values()) {
            if (itemStack.is(type.getCustomItem()))
                return type;
        }

        return null;
    }

}
