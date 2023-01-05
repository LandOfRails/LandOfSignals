package net.landofrails.landofsignals.contentpacks;

import net.landofrails.api.contentpacks.GenericContentPack;
import net.landofrails.landofsignals.LandOfSignals;
import net.landofrails.stellwand.utils.exceptions.ContentPackException;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
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
            LandOfSignals.info("Searching for assets..");

            File[] assets = assetFolder.listFiles((dir, name) -> name.endsWith(".zip"));

            if (assets == null || assets.length == 0) {
                LandOfSignals.info("No assets found.");
            } else {
                for (File asset : assets)
                    loadAsset(asset, StandardCharsets.UTF_8);
            }

        } else {
            boolean result = assetFolder.mkdirs();
            if (result)
                LandOfSignals.info("Asset folder created.");
            else
                LandOfSignals.warn("Couldn't create asset folder: %s", assetFolder.getPath());

        }
    }

    private static void loadAsset(File asset, Charset charset) {
        boolean isUTF8 = StandardCharsets.UTF_8.equals(charset);
        LandOfSignals.info("Loading Asset: %s", asset.getAbsolutePath());
        LandOfSignals.info("Encoding: %s", charset.displayName());
        try (ZipFile zip = new ZipFile(asset, charset)) {

            List<ZipEntry> files = zip.stream().filter(not(ZipEntry::isDirectory)).collect(Collectors.toList());

            if (files.stream().anyMatch(f -> f.getName().contains("ignore.me"))) {
                return;
            }

            // File.getName() returns full qualified name
            Optional<ZipEntry> landofsignalsJson = files.stream().filter(f -> f.getName().endsWith("landofsignals.json"))
                    .findFirst();
            if (landofsignalsJson.isPresent()) {
                load(zip, landofsignalsJson.get(), isUTF8);
            } else {
                throw new ContentPackException("[" + asset.getName() + "] Missing landofsignals.json");
            }

        } catch (IOException zipException) {
            LandOfSignals.error("Couldn't load asset: %s", asset.getName());
            LandOfSignals.error("Error: %s", zipException.getMessage());
        } catch (IllegalArgumentException illegalArgumentException) {
            if ("MALFORMED".equals(illegalArgumentException.getMessage())) {
                if (isUTF8) {
                    LandOfSignals.warn("Failed loading content with UTF-8. Trying ISO_8859_1 next. Try fixing this if possible. See wiki.");
                    loadAsset(asset, StandardCharsets.ISO_8859_1);
                } else {
                    LandOfSignals.error("Failed loading content with UTF-8 and ISO_8859_1. There seems to be atleast one character thats not working.");
                    throw illegalArgumentException;
                }
            } else {
                throw illegalArgumentException;
            }
        }
    }

    private static void load(ZipFile zip, ZipEntry landofsignalsJson, boolean isUTF8) {

        try {
            GenericContentPack genericContentPack = GenericContentPack.fromJson(
                    zip.getInputStream(zip.getEntry(landofsignalsJson.getName())));

            if (!genericContentPack.isValid()) {
                LandOfSignals.error("Failed loading ZIP named %s!\nNot all required fields have been set.", zip.getName());
                return;
            }

            String addonversion = genericContentPack.getAddonversion();

            LandOfSignals.info("Name: %s, Author: %s, Version: %s, Addonversion: %s",
                    genericContentPack.getName(),
                    genericContentPack.getAuthor(),
                    genericContentPack.getPackversion(),
                    addonversion);

            if ("1".equals(addonversion)) {
                ContentPackHandlerV1.load(zip, landofsignalsJson.getName(), isUTF8);

            } else if ("2".equals(addonversion)) {
                ContentPackHandlerV2.load(zip, landofsignalsJson.getName(), isUTF8);

            } else {
                LandOfSignals.error("Failed loading Contentpack named %s!\nUnsupported addonversion: %s." +
                        " Either your version of LandOfSignals is not up-to-date" +
                        " or the author used an invalid addonversion.", genericContentPack.getName(), addonversion);
            }


        } catch (IOException e) {
            LandOfSignals.error("Error while loading Contentpack: %s", e.getMessage());
        }
    }


    // For method references
    private static <T> Predicate<T> not(Predicate<T> t) {
        return t.negate();
    }

}
