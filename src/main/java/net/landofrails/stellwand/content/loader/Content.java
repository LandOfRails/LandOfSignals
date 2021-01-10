package net.landofrails.stellwand.content.loader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.landofrails.landofsignals.LandOfSignals;
import net.landofrails.stellwand.utils.exceptions.ContentPackException;

public class Content {

	private Content() {

	}

	private static List<ContentPack> contentPacks = new ArrayList<>();

	public static void addContentPack(ContentPack pack) {

		if (contentPacks.contains(pack))
			throw new ContentPackException("Pack already exists: " + pack.packName);

		for (ContentPack p : contentPacks) {
			if (p.packName.equalsIgnoreCase(pack.getPackName()))
				throw new ContentPackException("Pack with the same name  already exists: " + pack.packName);
		}

		// @formatter:off
		if (!LandOfSignals.VERSION.equalsIgnoreCase(pack.modVersion))
			throw new ContentPackException("[" + pack.getPackName() + "] Excepted ModVersion: " + LandOfSignals.VERSION + ", but found:" + pack.modVersion);
		// @formatter:on

		contentPacks.add(pack);
	}

	public static class ContentPack {

		private String modVersion = "";

		private String packName = "";
		private String packVersion = "";
		private String author = "";

		private Map<String, String> blockSignals = new HashMap<>();

		public ContentPack(String modVersion, String packName, String packVersion, String author) {
			this.modVersion = modVersion;
			this.packName = packName;
			this.packVersion = packVersion;
			this.author = author;
		}

		/**
		 * 
		 * Entry: Name, Texture (OBJ)
		 * 
		 * @param blockSignals
		 */
		public void setBlockSignals(Map<String, String> blockSignals) {
			this.blockSignals = blockSignals;
		}

		public String getModVersion() {
			return modVersion;
		}

		public String getPackName() {
			return packName;
		}

		public String getPackVersion() {
			return packVersion;
		}

		public String getAuthor() {
			return author;
		}

		public Map<String, String> getBlockSignals() {
			return blockSignals;
		}

	}

}
