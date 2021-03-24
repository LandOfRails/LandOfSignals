package net.landofrails.stellwand.contentpacks.entries.signal;

import java.util.LinkedHashMap;

import net.landofrails.stellwand.contentpacks.entries.parent.ContentPackEntryBlock;

@SuppressWarnings("java:S1319")
public class BlockSignalEntryBlock extends ContentPackEntryBlock {

	private LinkedHashMap<String, String> modes;

	public BlockSignalEntryBlock(float[] rotation, float[] translation, LinkedHashMap<String, String> modes) {
		super(rotation, translation);
		this.modes = modes;
	}

	public LinkedHashMap<String, String> getModes() {
		return modes;
	}

}
