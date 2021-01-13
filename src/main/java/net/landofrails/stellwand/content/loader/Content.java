package net.landofrails.stellwand.content.loader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cam72cam.mod.ModCore;
import net.landofrails.landofsignals.LandOfSignals;
import net.landofrails.stellwand.content.loader.ContentPack.ContentPackEntry;
import net.landofrails.stellwand.utils.exceptions.ContentPackException;

public class Content {

	private Content() {

	}

	private static List<ContentPack> contentPacks = new ArrayList<>();

	// model = new OBJModel(new Identifier(LandOfSignals.MODID,
	// this.getPath(BlockItemType.BLOCK)), 0);
	// renderer = new OBJRender(model);

	public static void addContentPack(ContentPack pack) {

		if (contentPacks.contains(pack))
			throw new ContentPackException("Pack already exists: " + pack.getName());

		for (ContentPack p : contentPacks) {
			if (p.getName().equalsIgnoreCase(pack.getName()) && p.getAuthor().equalsIgnoreCase(pack.getAuthor()))
				throw new ContentPackException("Pack with the same name  already exists: " + pack.getName());
		}

		// @formatter:off
		if (!LandOfSignals.VERSION.equalsIgnoreCase(pack.getModversion()))
			throw new ContentPackException("[" + pack.getName() + "] Excepted ModVersion: " + LandOfSignals.VERSION + ", but found:" + pack.getModversion());
		// @formatter:on

		contentPacks.add(pack);

		ModCore.Mod.info("Content Pack loaded: " + pack.getName() + " v" + pack.getPackversion());

	}

	public static Map<ContentPackEntry, String> getEntries() {
		Map<ContentPackEntry, String> entries = new HashMap<>();
		for (ContentPack pack : contentPacks) {
			for (ContentPackEntry entry : pack.getContent())
				entries.put(entry, pack.getId());
		}
		return entries;
	}

}
