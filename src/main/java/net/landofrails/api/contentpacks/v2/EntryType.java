package net.landofrails.api.contentpacks.v2;

import cam72cam.mod.item.CustomItem;
import net.landofrails.landofsignals.LOSItems;

import java.util.function.Supplier;

public enum EntryType {

    // @formatter:off
    BLOCKSIGNAL(() -> LOSItems.ITEM_SIGNAL_PART),
    BLOCKSIGN(() -> LOSItems.ITEM_SIGN_PART),
    BLOCKSIGNALBOX(() -> LOSItems.ITEM_SIGNAL_BOX),
    // TODO implement
    BLOCKDECO(() -> null);
    // @formatter:on
    
    private final Supplier<CustomItem> itemSupplier;

    EntryType(Supplier<CustomItem> itemSupplier) {
        this.itemSupplier = itemSupplier;
    }

    public CustomItem getCustomItem() {
        return itemSupplier.get();
    }


}
