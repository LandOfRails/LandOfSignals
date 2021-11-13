package net.landofrails.stellwand.contentpacks.types;

import net.landofrails.stellwand.contentpacks.entries.filler.BlockFillerEntry;
import net.landofrails.stellwand.contentpacks.entries.multisignal.BlockMultisignalEntry;
import net.landofrails.stellwand.contentpacks.entries.parent.ContentPackEntry;
import net.landofrails.stellwand.contentpacks.entries.buttonreceiver.BlockReceiverEntry;
import net.landofrails.stellwand.contentpacks.entries.sender.BlockSenderEntry;
import net.landofrails.stellwand.contentpacks.entries.signal.BlockSignalEntry;

public enum EntryType {
    BLOCKSIGNAL(BlockSignalEntry.class), BLOCKSENDER(BlockSenderEntry.class), BLOCKFILLER(BlockFillerEntry.class), BLOCKMULTISIGNAL(BlockMultisignalEntry.class), BLOCKRECEIVER(BlockReceiverEntry.class);

    private Class<? extends ContentPackEntry> typeClass;

    EntryType(Class<? extends ContentPackEntry> typeClass) {
        this.typeClass = typeClass;
    }

    public Class<? extends ContentPackEntry> getTypeClass() {
        return typeClass;
    }

}
