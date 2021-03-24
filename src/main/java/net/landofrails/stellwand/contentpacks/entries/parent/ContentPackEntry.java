package net.landofrails.stellwand.contentpacks.entries.parent;

import java.io.IOException;
import java.io.InputStream;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.landofrails.stellwand.contentpacks.entries.filler.BlockFillerEntry;
import net.landofrails.stellwand.contentpacks.entries.sender.BlockSenderEntry;
import net.landofrails.stellwand.contentpacks.entries.signal.BlockSignalEntry;
import net.landofrails.stellwand.contentpacks.types.EntryType;
import net.landofrails.stellwand.utils.exceptions.ContentPackException;

public abstract class ContentPackEntry {

	protected String name;
	protected String model;

	protected ContentPackEntryBlock block;
	protected ContentPackEntryItem item;

	public ContentPackEntry(String name, String model, ContentPackEntryBlock block, ContentPackEntryItem item) {
		this.name = name;
		this.model = model;
		this.block = block;
		this.item = item;
	}

	public String getBlockId(String packId) {
		return packId + ":" + name;
	}

	public String getName() {
		return name;
	}

	public String getModel() {
		return model;
	}

	public ContentPackEntryBlock getBlock() {
		return block;
	}

	
	@SuppressWarnings("unchecked")
	public <T extends ContentPackEntryBlock> T getBlock(Class<T> cls){
		if (cls.isInstance(block))
			return (T) block;
		return null;
	}

	public ContentPackEntryItem getItem() {
		return item;
	}

	@SuppressWarnings("unchecked")
	public <T extends ContentPackEntryItem> T getItem(Class<T> cls) {
		if (cls.isInstance(item))
			return (T) item;
		return null;
	}

	public abstract EntryType getType();

	public boolean isType(EntryType type) {
		return type.equals(getType());
	}

	// Statics
	public static ContentPackEntry fromJson(InputStream inputStream, EntryType type) {
		StringBuilder s = new StringBuilder();
		byte[] buffer = new byte[1024];
		int read = 0;

		try {
			while ((read = inputStream.read(buffer, 0, 1024)) >= 0) {
				s.append(new String(buffer, 0, read));
			}
		} catch (IOException e) {
			throw new ContentPackException("Cant read ContentPackEntry of type " + type.toString() + ": " + e.getMessage());
		}

		String json = s.toString();
		Gson gson = new GsonBuilder().create();

		return gson.fromJson(json, getTypeClass(type));
	}

	private static Class<? extends ContentPackEntry> getTypeClass(EntryType type) {

		switch (type) {
			case BLOCKFILLER :
				return BlockFillerEntry.class;
			case BLOCKSENDER :
				return BlockSenderEntry.class;
			case BLOCKSIGNAL :
				return BlockSignalEntry.class;
			default :
				return ContentPackEntry.class;
		}
	}

}
