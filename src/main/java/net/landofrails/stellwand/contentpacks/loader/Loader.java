package net.landofrails.stellwand.contentpacks.loader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import cam72cam.mod.ModCore;
import net.landofrails.stellwand.Stellwand;
import net.landofrails.stellwand.contentpacks.Content;
import net.landofrails.stellwand.contentpacks.entries.ContentPack;
import net.landofrails.stellwand.contentpacks.entries.parent.ContentPackEntry;
import net.landofrails.stellwand.contentpacks.types.EntryType;
import net.landofrails.stellwand.utils.exceptions.ContentPackException;

public class Loader {

	private Loader() {

	}

	public static void init() {

		StaticLoader.init();

		loadAssets();

		Content.testOutput();

	}

	private static void loadAssets() {

		File assetFolder = new File("./config/stellwand");
		if (assetFolder.exists()) {
			ModCore.Mod.info("Searching for assets..");

			File[] assets = assetFolder.listFiles((dir, name) -> name.endsWith(".zip"));

			if (assets == null || assets.length == 0) {
				ModCore.Mod.info("No assets found.");
			} else {
				for (File asset : assets)
					loadAsset(asset);
			}

		} else {
			boolean result = assetFolder.mkdirs();
			if (result)
				ModCore.Mod.info("Asset folder created.");
			else
				ModCore.Mod.error("Couldn't create asset folder: %s", assetFolder.getPath());

		}

	}

	// Run: 4
	private static void loadAsset(File asset) {
		ModCore.Mod.info("Loading Asset: %s", asset.getAbsolutePath());

		try (ZipFile zip = new ZipFile(asset)) {

			List<ZipEntry> files = zip.stream().filter(not(ZipEntry::isDirectory)).collect(Collectors.toList());
			Optional<ZipEntry> stellwandJson = files.stream().filter(f -> f.getName().endsWith("stellwand.json"))
					.findFirst();
			if (stellwandJson.isPresent()) {
				load(zip, stellwandJson.get());
			} else {
				throw new ContentPackException("[" + asset.getName() + "] Missing stellwand.json");
			}

		} catch (ZipException zipException) {
			ModCore.Mod.error("Couldn't load asset: %s", asset.getName());
			ModCore.Mod.error("Error: %s", zipException.getMessage());
		} catch (IOException e) {
			ModCore.Mod.error("Couldn't load asset: %s", asset.getName());
			ModCore.Mod.error("Error: %s", e.getMessage());
		}

	}

	private static void load(ZipFile zip, ZipEntry stellwandJson) {

		try {
			ContentPack contentPack = ContentPack.fromJson(zip.getInputStream(zip.getEntry(stellwandJson.getName())));
			// @formatter:off
			List<ZipEntry> files = zip.stream().
					filter(not(ZipEntry::isDirectory)).
					filter(f -> f.getName().endsWith(".json") && !f.getName().endsWith("stellwand.json")).collect(Collectors.toList());
			// @formatter:on

			List<ContentPackEntry> contentPackEntries = new ArrayList<>();

			Stellwand.debug("Content for %s: ", contentPack.getId());
			for (Entry<String, EntryType> content : contentPack.getContent().entrySet()) {

				String contentName = content.getKey();
				EntryType type = content.getValue();

				for (ZipEntry entry : files) {
					if (entry.getName().equalsIgnoreCase(contentName)) {

						// New: type
						ContentPackEntry contentPackEntry = ContentPackEntry.fromJson(zip.getInputStream(entry), type);
						Stellwand.debug("Block: %s, Type: %s", contentPackEntry.getName(), type.name());

						contentPackEntries.add(contentPackEntry);

					}
				}
			}
			contentPack.setEntries(contentPackEntries);

			Content.addContentPack(contentPack);
		} catch (IOException e) {
			ModCore.Mod.error("Error while loading Contentpack: %s", e.getMessage());
		}
	}

	// For method references
	private static <T> Predicate<T> not(Predicate<T> t) {
		return t.negate();
	}

}
