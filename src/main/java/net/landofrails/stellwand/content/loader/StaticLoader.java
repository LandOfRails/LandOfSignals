package net.landofrails.stellwand.content.loader;

import static net.landofrails.stellwand.content.loader.ContentPackEntryDirectionType.LEFT;
import static net.landofrails.stellwand.content.loader.ContentPackEntryDirectionType.RIGHT;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import net.landofrails.landofsignals.LandOfSignals;
import net.landofrails.stellwand.content.loader.ContentPackEntry.ContentPackEntryBlock;
import net.landofrails.stellwand.content.loader.ContentPackEntry.ContentPackEntryItem;

public class StaticLoader {

	private StaticLoader() {

	}

	public static void init() {
		ContentPack contentPack = new ContentPack(LandOfSignals.VERSION, "LandOfSignals", "1.0.0", "SeltixSub", new ArrayList<>());
		contentPack.setEntries(getEntries());
		Content.addContentPack(contentPack);
	}

	private static List<ContentPackEntry> getEntries() {

		List<ContentPackEntry> contentPackEntries = new ArrayList<>();
		
		//// Signals

		// Signal Straight Track
		Properties prop = new Properties().setName("Signal Straight Track");
		prop.setType(ContentPackEntryType.BLOCKSIGNAL);
		prop.setModel("models/block/blocksignal/blocktrackstraight/blocksignal.obj");
		prop.setModes(modes("Off", "off", "White", "white", "Red", "red"));
		prop.setFromDir(LEFT).setToDir(RIGHT);
		prop.setItemMode("white");
		contentPackEntries.add(prop.toEntry());
		
		//// Fillers
		
		// Block Filler
		prop = new Properties().setName("Block Filler");
		prop.setType(ContentPackEntryType.BLOCKFILLER);
		prop.setModel("models/block/blockfiller/blockfiller/blockfiller.obj");
		prop.setBlockTranslation(0.5f, 0f, 0.5f);
		prop.setItemTranslation(0.5f, 0.1625f, 0.5f);
		contentPackEntries.add(prop.toEntry());

		return contentPackEntries;
	}

	private static LinkedHashMap<String, String> modes(String... modes) {
		LinkedHashMap<String, String> map = new LinkedHashMap<>();

		for (int i = 0; i < modes.length; i += 2) {
			String key = modes[i];
			String value = modes[i + 1];
			map.put(key, value);
		}

		return map;
	}

	@SuppressWarnings("unused")
	private static class Properties {

		private ContentPackEntryType type;
		private String name;
		private ContentPackEntryDirectionType fromDir;
		private ContentPackEntryDirectionType toDir;
		private String model;
		private LinkedHashMap<String, String> modes = new LinkedHashMap<>();
		private String itemMode;
		private float[] blockRotation = new float[]{0f, 0f, 0f};
		private float[] blockTranslation = new float[]{0.5f, 0.5f, 0.5f};
		private float[] itemRotation = new float[]{15f, 195f, 0};
		private float[] itemTranslation = new float[]{0.5f, 0.5f, 0.5f};
		private float scale = 0.7f;

		public Properties setType(ContentPackEntryType type) {
			this.type = type;
			return this;
		}

		public Properties setName(String name) {
			this.name = name;
			return this;
		}

		public Properties setFromDir(ContentPackEntryDirectionType fromDir) {
			this.fromDir = fromDir;
			return this;
		}

		public Properties setToDir(ContentPackEntryDirectionType toDir) {
			this.toDir = toDir;
			return this;
		}

		public Properties setModel(String model) {
			this.model = model;
			return this;
		}

		@SuppressWarnings("java:S1319")
		public Properties setModes(LinkedHashMap<String, String> modes) {
			this.modes = modes;
			return this;
		}

		public Properties setItemMode(String itemMode) {
			this.itemMode = itemMode;
			return this;
		}

		public Properties setBlockRotation(float... blockRotation) {
			this.blockRotation = blockRotation;
			return this;
		}

		public Properties setBlockTranslation(float... blockTranslation) {
			this.blockTranslation = blockTranslation;
			return this;
		}

		public Properties setItemRotation(float... itemRotation) {
			this.itemRotation = itemRotation;
			return this;
		}

		public Properties setItemTranslation(float... itemTranslation) {
			this.itemTranslation = itemTranslation;
			return this;
		}

		public Properties setScale(float scale) {
			this.scale = scale;
			return this;
		}

		public ContentPackEntry toEntry() {
			ContentPackEntryBlock block = new ContentPackEntryBlock(blockRotation, blockTranslation, modes);
			ContentPackEntryItem item = new ContentPackEntryItem(itemRotation, itemTranslation, scale, model, itemMode);

			String from = fromDir != null ? fromDir.name() : null;
			String to = toDir != null ? toDir.name() : null;
			return new ContentPackEntry(type.name(), name, from, to, model, block, item);
		}

	}

}
