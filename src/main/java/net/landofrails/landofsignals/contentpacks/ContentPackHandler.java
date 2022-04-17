package net.landofrails.landofsignals.contentpacks;

import cam72cam.mod.ModCore;
import net.landofrails.api.contentpacks.GenericContentPack;
import net.landofrails.api.contentpacks.v1.*;
import net.landofrails.api.contentpacks.v2.ContentPack;
import net.landofrails.landofsignals.LOSBlocks;
import net.landofrails.stellwand.utils.exceptions.ContentPackException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ContentPackHandler {

    private ContentPackHandler() {
        
    }

    public static void init() {
        loadAssets();
    }

    public static void loadAssets() {

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
            // File.getName() returns full qualified name
            Optional<ZipEntry> landofsignalsJson = files.stream().filter(f -> f.getName().endsWith("landofsignals.json"))
                    .findFirst();
            if (landofsignalsJson.isPresent()) {
                load(zip, landofsignalsJson.get());
            } else {
                throw new ContentPackException("[" + asset.getName() + "] Missing landofsignals.json");
            }

        } catch (IOException zipException) {
            ModCore.Mod.error("Couldn't load asset: %s", asset.getName());
            ModCore.Mod.error("Error: %s", zipException.getMessage());
        }
    }

    private static void load(ZipFile zip, ZipEntry landofsignalsJson) {

        try {
            GenericContentPack genericContentPack = GenericContentPack.fromJson(
                    zip.getInputStream(zip.getEntry(landofsignalsJson.getName())));

            if (!genericContentPack.isValid()) {
                ModCore.error("Failed loading ZIP named %s!\nNot all required fields have been set.", zip.getName());
                return;
            }

            String addonversion = genericContentPack.getAddonversion();

            ModCore.info("Name: %s, Author: %s, Version: %s, Addonversion: %s",
                    genericContentPack.getName(),
                    genericContentPack.getAuthor(),
                    genericContentPack.getPackversion(),
                    addonversion);

            if ("1".equals(addonversion)) {
                ContentPackHead contentPack = ContentPackHead.fromJson(zip.getInputStream(zip.getEntry(landofsignalsJson.getName())));
                // @formatter:off
                List<ZipEntry> files = zip.stream().
                        filter(not(ZipEntry::isDirectory)).
                        filter(f -> f.getName().endsWith(".json") && !f.getName().endsWith("landofsignals.json")).collect(Collectors.toList());
                // @formatter:on

                ModCore.info("Content for %s:", contentPack.getId());
                addSignals(contentPack, files, zip);
                addSigns(contentPack, files, zip);

            } else if ("2".equals(addonversion)) {
                ContentPack contentPack = ContentPack.fromJson(zip.getInputStream(zip.getEntry(landofsignalsJson.getName())));
                ModCore.info("Content for %s:", contentPack.getId());

                // TODO: Impement addonversion 2
                // TODO: Make addonversion 2 default
                // TODO: Make Adapter for addonversion 1

            } else {
                ModCore.error("Failed loading Contentpack named %s!\nUnsupported addonversion: %s." +
                        " Either your version of LandOfSignals is not up-to-date" +
                        " or the author used an invalid addonversion.", addonversion);
            }


        } catch (IOException e) {
            ModCore.Mod.error("Error while loading Contentpack: %s", e.getMessage());
        }
    }

    private static void addSignals(ContentPackHead contentPack, List<ZipEntry> files, ZipFile zip) throws IOException {
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
                                if (contentPackSignalPart.getAnimations() != null)
                                    LOSBlocks.BLOCK_SIGNAL_PART_ANIMATED.add(contentPackSignalPart);
                                else
                                    LOSBlocks.BLOCK_SIGNAL_PART.add(contentPackSignalPart);
                                break;
                            }
                        }
                    }
                    break;
                }
            }
        }
    }

    private static void addSigns(ContentPackHead contentPack, List<ZipEntry> files, ZipFile zip) throws IOException {
        for (String pathToContentPackSignSet : contentPack.getSigns()) {
            for (ZipEntry zipEntry : files) {
                if (zipEntry.getName().equalsIgnoreCase(pathToContentPackSignSet)) {
                    ContentPackSignSet contentPackSignSet = ContentPackSignSet.fromJson(zip.getInputStream(zipEntry));
                    ModCore.info("SignSet: %s", contentPackSignSet.getName());
                    for (String pathToContentPackSignalPart : contentPackSignSet.getSignparts()) {
                        for (ZipEntry zipEntry1 : files) {
                            if (zipEntry1.getName().equalsIgnoreCase(pathToContentPackSignalPart)) {
                                ContentPackSignPart contentPackSignPart = ContentPackSignPart.fromJson(zip.getInputStream(zipEntry1));
                                ModCore.info("SignPart: %s", contentPackSignPart.getName());
                                LOSBlocks.BLOCK_SIGN_PART.add(contentPackSignPart);
                            }
                        }
                    }
                    break;
                }
            }
        }
    }

    // For method references
    private static <T> Predicate<T> not(Predicate<T> t) {
        return t.negate();
    }

}
