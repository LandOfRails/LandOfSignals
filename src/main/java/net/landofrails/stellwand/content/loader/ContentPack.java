package net.landofrails.stellwand.content.loader;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.landofrails.stellwand.utils.exceptions.ContentPackException;

public class ContentPack {

	private String modversion = "";
	private String name = "";
	private String packversion = "";
	private String author = "";

	private List<ContentPackEntry> content = new ArrayList<>();

	public ContentPack(String modversion, String name, String packversion, String author,
			List<ContentPackEntry> content) {
		this.modversion = modversion;
		this.name = name;
		this.packversion = packversion;
		this.author = author;
	}

	public String getModversion() {
		return modversion;
	}

	public String getName() {
		return name;
	}

	public String getPackversion() {
		return packversion;
	}

	public String getAuthor() {
		return author;
	}

	public List<ContentPackEntry> getContent() {
		return content;
	}

	public String getId() {
		return name + "@" + author;
	}

	public static ContentPack fromJson(InputStream stellwandJsonStream) {

		StringBuilder s = new StringBuilder();
		byte[] buffer = new byte[1024];
		int read = 0;

		try {
			while ((read = stellwandJsonStream.read(buffer, 0, 1024)) >= 0) {
				s.append(new String(buffer, 0, read));
			}
		} catch (IOException e) {
			throw new ContentPackException("Cant read stellwand.json: " + e.getMessage());
		}

		String json = s.toString();
		Gson gson = new GsonBuilder().create();

		return gson.fromJson(json, ContentPack.class);

	}

	public static class ContentPackEntry {

		private String type;
		private String name;
		private String directionFrom;
		private String directionTo;
		private String model;

		private ContentPackEntryBlock block;
		private ContentPackEntryItem item;

		public ContentPackEntry(String type, String name, String directionFrom, String directionTo, String model,
				ContentPackEntryBlock block, ContentPackEntryItem item) {
			this.type = type;
			this.name = name;
			this.directionFrom = directionFrom;
			this.directionTo = directionTo;
			this.model = model;
			this.block = block;
			this.item = item;
		}

		public String getType() {
			return type;
		}

		public String getName() {
			return name;
		}

		public String getDirectionFrom() {
			return directionFrom;
		}

		public String getDirectionTo() {
			return directionTo;
		}

		public String getModel() {
			return model;
		}

		public ContentPackEntryBlock getBlock() {
			return block;
		}

		public ContentPackEntryItem getItem() {
			return item;
		}

		public String getBlockId(String packId) {
			return packId + ":" + name;
		}

		public static class ContentPackEntryBlock {

			private float[] rotation;
			private float[] translation;

			private Map<String, String> modes;

			public ContentPackEntryBlock(float[] rotation, float[] translation, Map<String, String> modes) {
				super();
				this.rotation = rotation;
				this.translation = translation;
				this.modes = modes;
			}

			public float[] getRotation() {
				return rotation;
			}

			public float[] getTranslation() {
				return translation;
			}

			public Map<String, String> getModes() {
				return modes;
			}

		}

		public static class ContentPackEntryItem {

			private float[] rotation;
			private float[] translation;
			private String model;
			private String mode;

			public ContentPackEntryItem(float[] rotation, float[] translation, String model, String mode) {
				this.rotation = rotation;
				this.translation = translation;
				this.model = model;
				this.mode = mode;
			}

			public float[] getRotation() {
				return rotation;
			}

			public float[] getTranslation() {
				return translation;
			}

			public String getModel() {
				return model;
			}

			public String getMode() {
				return mode;
			}

		}

	}

}
