package net.landofrails.landofsignals.contentpacks;

import cam72cam.mod.ModCore;
import net.landofrails.api.contentpacks.GenericContentPack;
import net.landofrails.stellwand.utils.exceptions.ContentPackException;

import java.io.File;
import java.io.IOException;
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
                ContentPackHandlerV1.load(zip, landofsignalsJson.getName());

            } else if ("2".equals(addonversion)) {
                ContentPackHandlerV2.load(zip, landofsignalsJson.getName());

            } else {
                ModCore.error("Failed loading Contentpack named %s!\nUnsupported addonversion: %s." +
                        " Either your version of LandOfSignals is not up-to-date" +
                        " or the author used an invalid addonversion.", genericContentPack.getName(), addonversion);
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
