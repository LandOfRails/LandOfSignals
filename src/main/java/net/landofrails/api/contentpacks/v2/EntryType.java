package net.landofrails.api.contentpacks.v2;

import cam72cam.mod.item.CustomItem;
import cam72cam.mod.item.ItemStack;
import net.landofrails.api.contentpacks.v2.sender.ContentPackSender;
import net.landofrails.api.contentpacks.v2.sign.ContentPackSign;
import net.landofrails.api.contentpacks.v2.signal.ContentPackSignal;
import net.landofrails.landofsignals.LOSItems;

import java.util.function.Supplier;

public enum EntryType {

    // @formatter:off
    BLOCKSIGNAL(ContentPackSignal.class, () -> LOSItems.ITEM_SIGNAL_PART),
    BLOCKSIGN(ContentPackSign.class, () -> LOSItems.ITEM_SIGN_PART),
    // TODO implement
    BLOCKSENDER(ContentPackSender.class, () -> LOSItems.ITEM_SIGNAL_BOX),
    BLOCKDECO(null, () -> null);
    // @formatter:on
    private final Class<?> typeClass;
    private final Supplier<CustomItem> itemSupplier;

    EntryType(Class<?> typeClass, Supplier<CustomItem> itemSupplier) {
        this.typeClass = typeClass;
        this.itemSupplier = itemSupplier;
    }

    public Class<?> getTypeClass() {
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
