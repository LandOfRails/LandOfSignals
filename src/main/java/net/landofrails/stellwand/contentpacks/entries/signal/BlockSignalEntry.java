package net.landofrails.stellwand.contentpacks.entries.signal;

import net.landofrails.stellwand.contentpacks.entries.parent.ContentPackEntry;
import net.landofrails.stellwand.contentpacks.types.EntryType;

public class BlockSignalEntry extends ContentPackEntry {

	@Override
	public EntryType getType() {
		return EntryType.BLOCKSIGNAL;
	}

	// @formatter:off
	public BlockSignalEntry(
			String name, 
			String model, 
			BlockSignalEntryBlock block, 
			BlockSignalEntryItem item
	) {
		super(name, model, block, item);
	}
	// @formatter:on


}
