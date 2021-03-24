package net.landofrails.stellwand.content.loader;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import cam72cam.mod.ModCore;
import net.landofrails.stellwand.Stellwand;
import net.landofrails.stellwand.content.items.ItemBlockSignal;
import net.landofrails.stellwand.utils.exceptions.ContentPackException;

public class Content {

	private Content() {

	}

	private static List<ContentPack> contentPacks = new LinkedList<>();

	public static void addContentPack(ContentPack pack) {

		if (contentPacks.contains(pack))
			throw new ContentPackException("Pack already exists: " + pack.getName());

		for (ContentPack p : contentPacks) {
			if (p.getName().equalsIgnoreCase(pack.getName()) && p.getAuthor().equalsIgnoreCase(pack.getAuthor()))
				throw new ContentPackException("Pack with the same name  already exists: " + pack.getName());
		}

		// @formatter:off
		if (!Stellwand.ADDON_VERSION.equalsIgnoreCase(pack.getAddonVersion()))
			throw new ContentPackException("[" + pack.getName() + "] Excepted ModVersion: " + Stellwand.ADDON_VERSION
					+ ", but found:" + pack.getAddonVersion());
		// @formatter:on

		contentPacks.add(pack);

		ModCore.Mod.info("Content Pack loaded: %s v%s", pack.getName(), pack.getPackversion());

	}

	public static Map<ContentPackEntry, String> getBlockSignals() {
		Map<ContentPackEntry, String> entries = new LinkedHashMap<>();
		for (ContentPack pack : contentPacks) {
			for (ContentPackEntry entry : pack.getEntries())
				if (entry.getType().equalsIgnoreCase(ContentPackEntryType.BLOCKSIGNAL.name()))
					entries.put(entry, pack.getId());
		}
		return entries;
	}

	public static Map<ContentPackEntry, String> getBlockSenders() {
		Map<ContentPackEntry, String> entries = new LinkedHashMap<>();
		for (ContentPack pack : contentPacks) {
			for (ContentPackEntry entry : pack.getEntries())
				if (entry.getType().equalsIgnoreCase(ContentPackEntryType.BLOCKSENDER.name()))
					entries.put(entry, pack.getId());
		}
		return entries;
	}

	public static Map<ContentPackEntry, String> getBlockFillers() {
		Map<ContentPackEntry, String> entries = new LinkedHashMap<>();
		for (ContentPack pack : contentPacks) {
			for (ContentPackEntry entry : pack.getEntries())
				if (entry.getType().equalsIgnoreCase(ContentPackEntryType.BLOCKFILLER.name()))
					entries.put(entry, pack.getId());
		}
		return entries;
	}

	public static String getNameForId(String itemId) {
		if (itemId == null || !itemId.contains(":"))
			return ItemBlockSignal.MISSING;
		return itemId.split(":")[1];
	}

}
