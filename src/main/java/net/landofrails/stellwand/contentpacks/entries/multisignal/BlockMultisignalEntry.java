package net.landofrails.stellwand.contentpacks.entries.multisignal;

import net.landofrails.stellwand.contentpacks.entries.parent.ContentPackEntry;
import net.landofrails.stellwand.contentpacks.entries.parent.ContentPackEntryBlock;
import net.landofrails.stellwand.contentpacks.entries.parent.ContentPackEntryItem;
import net.landofrails.stellwand.contentpacks.types.DirectionType;
import net.landofrails.stellwand.contentpacks.types.EntryType;

public class BlockMultisignalEntry extends ContentPackEntry {

    private BlockMultisignalEntryBlock block;
    private BlockMultisignalEntryItem item;

    private DirectionType[] directionFrom;
    private DirectionType[] directionTo;

    public BlockMultisignalEntry() {

    }

    // @formatter:off
    public BlockMultisignalEntry(
            String name,
            DirectionType[] directionFrom,
            DirectionType[] directionTo,
            String model,
            BlockMultisignalEntryBlock block,
            BlockMultisignalEntryItem item
    ) {
        super(name, model);
        this.directionFrom = directionFrom;
        this.directionTo = directionTo;
        this.block = block;
        this.item = item;
    }
    // @formatter:on

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
        return EntryType.BLOCKMULTISIGNAL;
    }

    public DirectionType[] getDirectionFrom() {
        return directionFrom;
    }

    public DirectionType[] getDirectionTo() {
        return directionTo;
    }
}
