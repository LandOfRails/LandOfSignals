package net.landofrails.stellwand.contentpacks.entries.signal;

import java.util.Map;

import net.landofrails.stellwand.contentpacks.entries.parent.ContentPackEntryBlock;

@SuppressWarnings("java:S1319")
public class BlockSignalEntryBlock extends ContentPackEntryBlock {

	private Map<String, String> modes;

	public BlockSignalEntryBlock() {

	}

	public BlockSignalEntryBlock(float[] rotation, float[] translation, Map<String, String> modes) {
		super(rotation, translation);
		this.modes = modes;
	}

	public Map<String, String> getModes() {
		return modes;
	}

}
