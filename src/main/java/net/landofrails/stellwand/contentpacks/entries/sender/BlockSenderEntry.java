package net.landofrails.stellwand.contentpacks.entries.sender;

import net.landofrails.stellwand.contentpacks.entries.parent.ContentPackEntry;
import net.landofrails.stellwand.contentpacks.entries.parent.ContentPackEntryBlock;
import net.landofrails.stellwand.contentpacks.entries.parent.ContentPackEntryItem;
import net.landofrails.stellwand.contentpacks.types.DirectionType;
import net.landofrails.stellwand.contentpacks.types.EntryType;

public class BlockSenderEntry extends ContentPackEntry {

	private BlockSenderEntryBlock block;
	private BlockSenderEntryItem item;

	private DirectionType[] directionFrom;
	private DirectionType[] directionTo;

	public BlockSenderEntry() {

	}

	// @formatter:off
	public BlockSenderEntry(
			String name,
			DirectionType[] directionFrom,
			DirectionType[] directionTo,
			String model,
			BlockSenderEntryBlock block,
			BlockSenderEntryItem item
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
			if (directionTo != null && directionTo.length > 0)
				return true;
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
		return EntryType.BLOCKSENDER;
	}

}
