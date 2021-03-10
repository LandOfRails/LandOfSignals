package net.landofrails.landofsignals.utils.contentpacks;

import cam72cam.mod.ModCore;
import net.landofrails.stellwand.utils.StellwandUtils;
import net.landofrails.stellwand.utils.exceptions.ContentPackException;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
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
            ModCore.Mod.info("Mod Folder: " + path, path);
            loadAssets(file);
        } else {
            ModCore.Mod.warn("Couldn't get Mod folder. Can't load assets.", "warning1");
        }

    }

    public static void loadAssets(File modFolder) {

        if (modFolder == null) {
            ModCore.Mod.warn("Couldn't get Mod folder. Can't load assets.", "warning2");
            return;
        }
        File assetFolder = new File("./config/landofsignals");
        if (assetFolder.exists()) {
            ModCore.Mod.info("Searching for assets..", "information1");

            File[] assets = assetFolder.listFiles((dir, name) -> name.endsWith(".zip"));

            if (assets == null || assets.length == 0)
                ModCore.Mod.info("No assets found.", "information2");
            else
                for (File asset : assets)
                    loadAsset(asset);

        } else {
            boolean result = assetFolder.mkdirs();
            if (result)
                ModCore.Mod.info("Asset folder created.", "information3");
            else
                ModCore.Mod.warn("Couldn't create asset folder: " + assetFolder.getPath(), "warning3");

        }
    }

    private static void loadAsset(File asset) {
        ModCore.Mod.info("Loading Asset: " + asset.getAbsolutePath(), "information4");

        try (ZipFile zip = new ZipFile(asset)) {

            List<ZipEntry> files = zip.stream().filter(not(ZipEntry::isDirectory)).collect(Collectors.toList());
            Optional<ZipEntry> landofsignalsJson = files.stream().filter(f -> f.getName().endsWith("landofsignals.json")).findFirst();
            if (landofsignalsJson.isPresent())
                load(zip, landofsignalsJson.get());
            else
                throw new ContentPackException("[" + asset.getName() + "] Missing landofsignals.json");

        } catch (ZipException zipException) {
            ModCore.Mod.error("Couldn't load asset: " + asset.getName(), "error2");
            ModCore.Mod.error("Error: " + zipException.getMessage(), "error2");
        } catch (IOException e) {
            ModCore.Mod.error("Couldn't load asset: " + asset.getName(), "error3");
            ModCore.Mod.error("Error: " + e.getMessage(), "error3");
        }
    }

    public static List<String> modelPath = new ArrayList<>();

    private static void load(ZipFile zip, ZipEntry landofsignalsJson) {

        for (Enumeration e = zip.entries(); e.hasMoreElements(); ) {
            ZipEntry entry = (ZipEntry) e.nextElement();
            if (!entry.isDirectory() && FilenameUtils.getExtension(entry.getName()).equals("obj")) {
                modelPath.add(entry.getName());
                System.out.println(entry.getName());
            }
        }
    }

    // For method references
    private static <T> Predicate<T> not(Predicate<T> t) {
        return t.negate();
    }

}
