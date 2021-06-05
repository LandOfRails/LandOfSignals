package net.landofrails.stellwand.contentpacks.entries.parent;

import java.io.IOException;
import java.io.InputStream;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import cam72cam.mod.ModCore;
import net.landofrails.stellwand.contentpacks.entries.filler.BlockFillerEntry;
import net.landofrails.stellwand.contentpacks.entries.sender.BlockSenderEntry;
import net.landofrails.stellwand.contentpacks.entries.signal.BlockSignalEntry;
import net.landofrails.stellwand.contentpacks.types.EntryType;
import net.landofrails.stellwand.utils.exceptions.ContentPackException;

public abstract class ContentPackEntry {

	protected String name;
	protected String model;


	public ContentPackEntry() {

	}

	public ContentPackEntry(String name, String model) {
		this.name = name;
		this.model = model;
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

	public abstract ContentPackEntryBlock getBlock();

	
	@SuppressWarnings("unchecked")
	public <T extends ContentPackEntryBlock> T getBlock(Class<T> cls) {

		try {
			return (T) getBlock();
		} catch (ClassCastException e) {
			// If it fails: try next

		}

		try {
			return cls.cast(getBlock());
		} catch (ClassCastException e2) {
			ModCore.error("Tried to cast blockclass: %s",
					getBlock() != null && getBlock().getClass() != null ? getBlock().getClass().getName() : "null");
			ModCore.error("Tried to cast to: %s", cls.getName());

			throw new ContentPackException("Weird cast exception above.");
		}

	}

	public abstract ContentPackEntryItem getItem();

	@SuppressWarnings("unchecked")
	public <T extends ContentPackEntryItem> T getItem(Class<T> cls) {
		if (cls.isInstance(getItem()))
			return (T) getItem();
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

		switch (type) {
			case BLOCKFILLER :
				return gson.fromJson(json, BlockFillerEntry.class);
			case BLOCKSENDER :
				return gson.fromJson(json, BlockSenderEntry.class);
			case BLOCKSIGNAL :
				return gson.fromJson(json, BlockSignalEntry.class);
			default :
				throw new ContentPackException("Type-switch failed unnormaly");
		}

	}

}
