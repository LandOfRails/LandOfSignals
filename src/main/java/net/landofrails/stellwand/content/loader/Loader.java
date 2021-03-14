package net.landofrails.stellwand.content.loader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import cam72cam.mod.ModCore;
import net.landofrails.stellwand.utils.StellwandUtils;
import net.landofrails.stellwand.utils.exceptions.ContentPackException;

public class Loader {

	private Loader() {

	}

	public static void init() {

		Optional<File> opt = StellwandUtils.getModFolder();

		StaticLoader.init();

		if (opt.isPresent()) {
			File file = opt.get();
			String path = file.getPath();
			ModCore.Mod.info("Mod Folder: " + path, path);
			loadAssets(file);
		} else {
			ModCore.Mod.warn("Couldn't get Mod folder. Can't load assets.", "warning1");
		}

	}

	private static void loadAssets(File modFolder) {

		if (modFolder == null) {
			ModCore.Mod.warn("Couldn't get Mod folder. Can't load assets.", "warning2");
			return;
		}

		File assetFolder = new File("./config/stellwand");
		if (assetFolder.exists()) {
			ModCore.Mod.info("Searching for assets..", "information1");

			File[] assets = assetFolder.listFiles((dir, name) -> name.endsWith(".zip"));

			if (assets == null || assets.length == 0) {
				ModCore.Mod.info("No assets found.", "information2");
			} else {
				for (File asset : assets)
					loadAsset(asset);
			}

		} else {
			boolean result = assetFolder.mkdirs();
			if (result)
				ModCore.Mod.info("Asset folder created.", "information3");
			else
				ModCore.Mod.warn("Couldn't create asset folder: " + assetFolder.getPath(), "warning3");

		}

	}

	// Run: 4
	private static void loadAsset(File asset) {
		ModCore.Mod.info("Loading Asset: " + asset.getAbsolutePath(), "information4");

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
			ModCore.Mod.error("Couldn't load asset: " + asset.getName(), "error2");
			ModCore.Mod.error("Error: " + zipException.getMessage(), "error2");
		} catch (IOException e) {
			ModCore.Mod.error("Couldn't load asset: " + asset.getName(), "error3");
			ModCore.Mod.error("Error: " + e.getMessage(), "error3");
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

			ModCore.info("Content for " + contentPack.getId() + ": ");
			for (String contentName : contentPack.getContent()) {
				for (ZipEntry entry : files) {
					if (entry.getName().equalsIgnoreCase(contentName)) {
						ContentPackEntry contentPackEntry = ContentPackEntry.fromJson(zip.getInputStream(entry));
						ModCore.info("Block: " + contentPackEntry.getName());
						contentPackEntries.add(contentPackEntry);
					}
				}
			}
			contentPack.setEntries(contentPackEntries);

			Content.addContentPack(contentPack);
		} catch (IOException e) {
			ModCore.Mod.error(e.getMessage());
		}
	}

	// For method references
	private static <T> Predicate<T> not(Predicate<T> t) {
		return t.negate();
	}

}
