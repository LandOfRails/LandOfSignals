package net.landofrails.stellwand.contentpacks.entries.signal;

import net.landofrails.stellwand.contentpacks.entries.parent.ContentPackEntry;
import net.landofrails.stellwand.contentpacks.entries.parent.ContentPackEntryBlock;
import net.landofrails.stellwand.contentpacks.entries.parent.ContentPackEntryItem;
import net.landofrails.stellwand.contentpacks.types.DirectionType;
import net.landofrails.stellwand.contentpacks.types.EntryType;

public class BlockSignalEntry extends ContentPackEntry {

    private BlockSignalEntryBlock block;
    private BlockSignalEntryItem item;

    private DirectionType[] directionFrom;
    private DirectionType[] directionTo;

    public BlockSignalEntry() {

    }

    // @formatter:off
	public BlockSignalEntry(
			String name,
			DirectionType[] directionFrom,
			DirectionType[] directionTo,
			String model,
			BlockSignalEntryBlock block, 
			BlockSignalEntryItem item
	) {
		super(name, model);
		this.directionFrom = directionFrom;
		this.directionTo = directionTo;
		this.block = block;
		this.item = item;
	}
	// @formatter:on

    @SuppressWarnings("java:S1066")
    public boolean hasDirections() {
        if (directionFrom != null && directionFrom.length > 0)
            return directionTo != null && directionTo.length > 0;
        return false;
    }

    public DirectionType[] getDirectionFrom() {
        return directionFrom;
    }

    public DirectionType[] getDirectionTo() {
        return directionTo;
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
        return EntryType.BLOCKSIGNAL;
    }

}
