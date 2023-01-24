package net.landofrails.landofsignals.utils.contentpacks;

import cam72cam.mod.ModCore;
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

    public static void init() {
        loadAssets();
    }

    public static void loadAssets() {

        final File assetFolder = new File("./config/landofsignals");
        if (assetFolder.exists()) {
            ModCore.Mod.info("Searching for assets..");

            final File[] assets = assetFolder.listFiles((dir, name) -> name.endsWith(".zip"));

            if (assets == null || assets.length == 0) {
                ModCore.Mod.info("No assets found.");
            } else {
                for (final File asset : assets)
                    loadAsset(asset);
            }

        } else {
            final boolean result = assetFolder.mkdirs();
            if (result)
                ModCore.Mod.info("Asset folder created.");
            else
                ModCore.Mod.warn("Couldn't create asset folder: %s", assetFolder.getPath());

        }
    }

    private static void loadAsset(final File asset) {
        ModCore.Mod.info("Loading Asset: %s", asset.getAbsolutePath());

        try (final ZipFile zip = new ZipFile(asset)) {

            final List<ZipEntry> files = zip.stream().filter(not(ZipEntry::isDirectory)).collect(Collectors.toList());
            final Optional<ZipEntry> stellwandJson = files.stream().filter(f -> f.getName().endsWith("landofsignals.json"))
                    .findFirst();
            if (stellwandJson.isPresent()) {
                load(zip, stellwandJson.get());
            } else {
                throw new ContentPackException("[" + asset.getName() + "] Missing landofsignals.json");
            }

        } catch (final IOException zipException) {
            ModCore.Mod.error("Couldn't load asset: %s", asset.getName());
            ModCore.Mod.error("Error: %s", zipException.getMessage());
            ModCore.Mod.error("There seems to be an issue with the zip file.");
        } catch (IllegalArgumentException illegalArgumentException) {
            ModCore.Mod.error("Couldn't load asset: %s", asset.getName());
            ModCore.Mod.error("Error: %s", illegalArgumentException.getMessage());
            ModCore.Mod.error("You might have some characters that are not strict UTF-8.");
        }
    }

    private static void load(final ZipFile zip, final ZipEntry landofsignalsJson) {

        try {
            final ContentPackHead contentPack = ContentPackHead.fromJson(zip.getInputStream(zip.getEntry(landofsignalsJson.getName())));

            if (!contentPack.isValidAddonVersion()) {
                ModCore.warn("%s by %s is on a newer version of the contentpacksystem! Check for updates!", contentPack.getName(), contentPack.getAuthor());
                return;
            }
            // @formatter:off
            final List<ZipEntry> files = zip.stream().
                    filter(not(ZipEntry::isDirectory)).
                    filter(f -> f.getName().endsWith(".json") && !f.getName().endsWith("landofsignals.json")).collect(Collectors.toList());
            // @formatter:on

            ModCore.info("Content for %s:", contentPack.getId());
            for (final String pathToContentPackSignalSet : contentPack.getSignals()) {
                for (final ZipEntry zipEntry : files) {
                    if (zipEntry.getName().equalsIgnoreCase(pathToContentPackSignalSet)) {
                        final ContentPackSignalSet contentPackSignalSet = ContentPackSignalSet.fromJson(zip.getInputStream(zipEntry));
                        ModCore.info("SignalSet: %s", contentPackSignalSet.getName());
                        for (final String pathToContentPackSignalPart : contentPackSignalSet.getSignalparts()) {
                            for (final ZipEntry zipEntry1 : files) {
                                if (zipEntry1.getName().equalsIgnoreCase(pathToContentPackSignalPart)) {
                                    final ContentPackSignalPart contentPackSignalPart = ContentPackSignalPart.fromJson(zip.getInputStream(zipEntry1));
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
        } catch (final IOException e) {
            ModCore.Mod.error("Error while loading Contentpack: %s", e.getMessage());
        }
    }

    // For method references
    private static <T> Predicate<T> not(final Predicate<T> t) {
        return t.negate();
    }

}
