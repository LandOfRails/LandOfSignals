package net.landofrails.stellwand.contentpacks.entries.signal;

import net.landofrails.stellwand.contentpacks.entries.parent.ContentPackEntry;
import net.landofrails.stellwand.contentpacks.entries.parent.ContentPackEntryBlock;
import net.landofrails.stellwand.contentpacks.entries.parent.ContentPackEntryItem;
import net.landofrails.stellwand.contentpacks.types.EntryType;

public class BlockSignalEntry extends ContentPackEntry {

	private BlockSignalEntryBlock block;
	private BlockSignalEntryItem item;

	public BlockSignalEntry() {

	}

	// @formatter:off
	public BlockSignalEntry(
			String name, 
			String model, 
			BlockSignalEntryBlock block, 
			BlockSignalEntryItem item
	) {
		super(name, model);
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
		return EntryType.BLOCKSIGNAL;
	}

}
