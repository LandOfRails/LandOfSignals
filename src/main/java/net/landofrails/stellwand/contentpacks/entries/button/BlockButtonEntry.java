package net.landofrails.stellwand.contentpacks.entries.button;

import net.landofrails.stellwand.contentpacks.entries.parent.ContentPackEntry;
import net.landofrails.stellwand.contentpacks.entries.parent.ContentPackEntryBlock;
import net.landofrails.stellwand.contentpacks.entries.parent.ContentPackEntryItem;
import net.landofrails.stellwand.contentpacks.types.EntryType;

public class BlockButtonEntry extends ContentPackEntry {

    private BlockButtonEntryBlock block;
    private BlockButtonEntryItem item;

    public BlockButtonEntry() {

    }

    public BlockButtonEntry(
            String name,
            String model,
            BlockButtonEntryBlock block,
            BlockButtonEntryItem item
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
        return EntryType.BLOCKBUTTON;
    }
}
