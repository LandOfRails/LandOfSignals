package net.landofrails.stellwand.contentpacks.entries.buttonreceiver;

import net.landofrails.stellwand.contentpacks.entries.parent.ContentPackEntry;
import net.landofrails.stellwand.contentpacks.entries.parent.ContentPackEntryBlock;
import net.landofrails.stellwand.contentpacks.entries.parent.ContentPackEntryItem;
import net.landofrails.stellwand.contentpacks.types.EntryType;

public class BlockButtonReceiverEntry extends ContentPackEntry {

    private BlockButtonReceiverEntryBlock block;
    private BlockButtonReceiverEntryItem item;

    public BlockButtonReceiverEntry() {

    }

    public BlockButtonReceiverEntry(
            String name,
            String model,
            BlockButtonReceiverEntryBlock block,
            BlockButtonReceiverEntryItem item
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
        return EntryType.BLOCKBUTTONRECEIVER;
    }
}
