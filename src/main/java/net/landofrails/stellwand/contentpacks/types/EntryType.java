package net.landofrails.stellwand.contentpacks.types;

import cam72cam.mod.item.CustomItem;
import cam72cam.mod.item.ItemStack;
import net.landofrails.stellwand.content.items.CustomItems;
import net.landofrails.stellwand.contentpacks.entries.button.BlockButtonEntry;
import net.landofrails.stellwand.contentpacks.entries.buttonreceiver.BlockButtonReceiverEntry;
import net.landofrails.stellwand.contentpacks.entries.filler.BlockFillerEntry;
import net.landofrails.stellwand.contentpacks.entries.multisignal.BlockMultisignalEntry;
import net.landofrails.stellwand.contentpacks.entries.parent.ContentPackEntry;
import net.landofrails.stellwand.contentpacks.entries.sender.BlockSenderEntry;
import net.landofrails.stellwand.contentpacks.entries.signal.BlockSignalEntry;

import java.util.function.Supplier;

public enum EntryType {

    // @formatter:off
    BLOCKSIGNAL(BlockSignalEntry.class, () -> CustomItems.ITEMBLOCKSIGNAL),
    BLOCKSENDER(BlockSenderEntry.class, () -> CustomItems.ITEMBLOCKSENDER),
    BLOCKFILLER(BlockFillerEntry.class, () -> CustomItems.ITEMBLOCKFILLER),
    BLOCKMULTISIGNAL(BlockMultisignalEntry.class, () -> CustomItems.ITEMBLOCKMULTISIGNAL),
    BLOCKBUTTON(BlockButtonEntry.class, () -> CustomItems.ITEMBLOCKBUTTON),
    BLOCKBUTTONRECEIVER(BlockButtonReceiverEntry.class, () -> CustomItems.ITEMBLOCKBUTTONRECEIVER);
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
