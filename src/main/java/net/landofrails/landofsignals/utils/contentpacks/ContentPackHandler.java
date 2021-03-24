package net.landofrails.landofsignals.utils.contentpacks;

import cam72cam.mod.ModCore;
import net.landofrails.landofsignals.LOSBlocks;
import net.landofrails.stellwand.utils.StellwandUtils;
import net.landofrails.stellwand.utils.exceptions.ContentPackException;

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

public class ContentPackHandler {

    public static void init() {

        Optional<File> opt = StellwandUtils.getModFolder();

        if (opt.isPresent()) {
            File file = opt.get();
            String path = file.getPath();
            ModCore.Mod.info("Mod Folder: %s", path);
            loadAssets(file);
        } else {
            ModCore.Mod.warn("Couldn't get Mod folder. Can't load assets.");
        }

    }

    public static void loadAssets(File modFolder) {

        if (modFolder == null) {
            ModCore.Mod.warn("Couldn't get Mod folder. Can't load assets.");
            return;
        }

        File assetFolder = new File("./config/landofsignals");
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
                ModCore.Mod.warn("Couldn't create asset folder: %s", assetFolder.getPath());

        }
    }

    private static void loadAsset(File asset) {
        ModCore.Mod.info("Loading Asset: %s", asset.getAbsolutePath());

        try (ZipFile zip = new ZipFile(asset)) {

            List<ZipEntry> files = zip.stream().filter(not(ZipEntry::isDirectory)).collect(Collectors.toList());
            Optional<ZipEntry> stellwandJson = files.stream().filter(f -> f.getName().endsWith("landofsignals.json"))
                    .findFirst();
            if (stellwandJson.isPresent()) {
                load(zip, stellwandJson.get());
            } else {
                throw new ContentPackException("[" + asset.getName() + "] Missing landofsignals.json");
            }

        } catch (ZipException zipException) {
            ModCore.Mod.error("Couldn't load asset: %s", asset.getName());
            ModCore.Mod.error("Error: %s", zipException.getMessage());
        } catch (IOException e) {
            ModCore.Mod.error("Couldn't load asset: %s", asset.getName());
            ModCore.Mod.error("Error: %s", e.getMessage());
        }
    }

    private static void load(ZipFile zip, ZipEntry landofsignalsJson) {

        try {
            ContentPackHead contentPack = ContentPackHead.fromJson(zip.getInputStream(zip.getEntry(landofsignalsJson.getName())));
            // @formatter:off
            List<ZipEntry> files = zip.stream().
                    filter(not(ZipEntry::isDirectory)).
                    filter(f -> f.getName().endsWith(".json") && !f.getName().endsWith("landofsignals.json")).collect(Collectors.toList());
            // @formatter:on

            ModCore.info("Content for %s:", contentPack.getId());
            for (String pathToContentPackSignalSet : contentPack.getSignals()) {
                for (ZipEntry zipEntry : files) {
                    if (zipEntry.getName().equalsIgnoreCase(pathToContentPackSignalSet)) {
                        ContentPackSignalSet contentPackSignalSet = ContentPackSignalSet.fromJson(zip.getInputStream(zipEntry));
                        ModCore.info("SignalSet: %s", contentPackSignalSet.getName());
                        for (String pathToContentPackSignalPart : contentPackSignalSet.getSignalparts()) {
                            for (ZipEntry zipEntry1 : files) {
                                if (zipEntry1.getName().equalsIgnoreCase(pathToContentPackSignalPart)) {
                                    ContentPackSignalPart contentPackSignalPart = ContentPackSignalPart.fromJson(zip.getInputStream(zipEntry1));
                                    ModCore.info("SignalPart: %s", contentPackSignalPart.getName());
                                    List<String> states = contentPackSignalPart.getStates();
                                    if (states == null) {
                                        states = new ArrayList<>();
                                    }
                                    states.add(0, null);
                                    contentPackSignalPart.setStates(states);
                                    LOSBlocks.BLOCK_SIGNAL_PART.add(contentPackSignalPart);
                                    break;
                                }
                            }
                        }
                        break;
                    }
                }
            }
        } catch (IOException e) {
            ModCore.Mod.error("Error while loading Contentpack: %s", e.getMessage());
        }
    }

    // For method references
    private static <T> Predicate<T> not(Predicate<T> t) {
        return t.negate();
    }

}
