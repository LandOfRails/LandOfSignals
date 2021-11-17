package net.landofrails.stellwand.contentpacks.entries.buttonreceiver;

import net.landofrails.stellwand.contentpacks.entries.parent.ContentPackEntry;
import net.landofrails.stellwand.contentpacks.entries.parent.ContentPackEntryBlock;
import net.landofrails.stellwand.contentpacks.entries.parent.ContentPackEntryItem;
import net.landofrails.stellwand.contentpacks.types.EntryType;

public class BlockReceiverEntry extends ContentPackEntry {

    private BlockReceiverEntryBlock block;
    private BlockReceiverEntryItem item;

    public BlockReceiverEntry() {

    }

    public BlockReceiverEntry(
            String name,
            String model,
            BlockReceiverEntryBlock block,
            BlockReceiverEntryItem item
    ) {
        super(name, model);
        this.block = block;
        this.item = item;
    }

    @Override
    public ContentPackEntryBlock getBlock() {
        return block;
    }

    @Override
    public ContentPackEntryItem getItem() {
        return item;
    }

    @Override
    public EntryType getType() {
        return EntryType.BLOCKRECEIVER;
    }
}
