package net.landofrails.stellwand.contentpacks;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import cam72cam.mod.ModCore;
import net.landofrails.stellwand.Stellwand;
import net.landofrails.stellwand.content.items.ItemBlockSignal;
import net.landofrails.stellwand.contentpacks.entries.ContentPack;
import net.landofrails.stellwand.contentpacks.entries.parent.ContentPackEntry;
import net.landofrails.stellwand.contentpacks.types.EntryType;
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
		if (!Stellwand.ADDON_VERSION.equalsIgnoreCase(pack.getAddonversion()))
			throw new ContentPackException("[" + pack.getName() + "] Excepted ModVersion: " + Stellwand.ADDON_VERSION
					+ ", but found:" + pack.getAddonversion());
		// @formatter:on

		contentPacks.add(pack);

		ModCore.Mod.info("Content Pack loaded: %s v%s", pack.getName(), pack.getPackversion());

	}

	public static Map<ContentPackEntry, String> getBlockSignals() {
		Map<ContentPackEntry, String> entries = new LinkedHashMap<>();
		for (ContentPack pack : contentPacks) {
			for (ContentPackEntry entry : pack.getEntries())
				if (entry.isType(EntryType.BLOCKSIGNAL))
					entries.put(entry, pack.getId());
		}
		return entries;
	}

	public static Map<ContentPackEntry, String> getBlockSenders() {
		Map<ContentPackEntry, String> entries = new LinkedHashMap<>();
		for (ContentPack pack : contentPacks) {
			for (ContentPackEntry entry : pack.getEntries())
				if (entry.isType(EntryType.BLOCKSENDER))
					entries.put(entry, pack.getId());
		}
		return entries;
	}

	public static Map<ContentPackEntry, String> getBlockFillers() {
		Map<ContentPackEntry, String> entries = new LinkedHashMap<>();
		for (ContentPack pack : contentPacks) {
			for (ContentPackEntry entry : pack.getEntries())
				if (entry.isType(EntryType.BLOCKFILLER))
					entries.put(entry, pack.getId());
		}
		return entries;
	}

	public static String getNameForId(String itemId) {
		if (itemId == null || !itemId.contains(":"))
			return ItemBlockSignal.MISSING;
		return itemId.split(":")[1];
	}

	public static void testOutput() {

		contentPacks.forEach(cp -> {
			ModCore.info("Pack: " + cp.getName());
			cp.getEntries().forEach(e -> ModCore.info("Block: %s, Type: %s", e.getName(), e.getType().name()));
		});

	}

}
