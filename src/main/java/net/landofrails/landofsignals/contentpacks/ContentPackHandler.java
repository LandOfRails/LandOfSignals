package net.landofrails.landofsignals.contentpacks;

import cam72cam.mod.ModCore;
import cam72cam.mod.gui.Progress;
import cam72cam.mod.model.obj.OBJModel;
import cam72cam.mod.resource.Identifier;
import net.landofrails.api.contentpacks.GenericContentPack;
import net.landofrails.api.contentpacks.v2.complexsignal.ContentPackSignalGroup;
import net.landofrails.api.contentpacks.v2.parent.ContentPackModel;
import net.landofrails.api.contentpacks.v2.signal.ContentPackSignal;
import net.landofrails.landofsignals.LOSBlocks;
import net.landofrails.landofsignals.LandOfSignals;
import net.landofrails.landofsignals.render.block.*;
import net.landofrails.landofsignals.render.item.*;
import net.landofrails.stellwand.utils.exceptions.ContentPackException;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ContentPackHandler {

    private static final String GENERIC_ITEM_ERRMSG = "Couldn't preload item with id \"%s\". Cause:";
    private static final String GENERIC_BLOCK_ERRMSG = "Couldn't preload block with id \"%s\". Cause:";

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

    public static void preloadModels(){

        LandOfSignals.info("Starting preloading models");

        Progress.Bar topProgressBar = Progress.push("LandOfSignals", 2);
        topProgressBar.step("Preloading models");

        // Signalpart
        Progress.Bar progressBar = Progress.push("Signalpart", LOSBlocks.BLOCK_SIGNAL_PART.getContentpackSignals().size());
        for (String id : LOSBlocks.BLOCK_SIGNAL_PART.getContentpackSignals().keySet()) {
            ModCore.info("Preloading signalpart %s", id);
            progressBar.step(LOSBlocks.BLOCK_SIGNAL_PART.getContentpackSignals().get(id).getName());

            final ContentPackSignal signal = LOSBlocks.BLOCK_SIGNAL_PART.getContentpackSignals().get(id);
            final String[] states = LOSBlocks.BLOCK_SIGNAL_PART.getAllStates(id);
            final String objPath = signal.getModel();

            // Cache items
            try {
                ItemSignalPartRender.cache().put(objPath, new OBJModel(new Identifier(LandOfSignals.MODID, objPath), 0, Arrays.asList(states)));
            } catch (Exception e) {
                String errmsg = "Couldn't preload item with id \"%s\" (objPath: %s). Cause:";
                throw new ContentPackException(String.format(errmsg, id, objPath), e);
            }

            // Cache blocks
            try {
                TileSignalPartRender.cache().put(objPath, new OBJModel(new Identifier(LandOfSignals.MODID, objPath), 0, Arrays.asList(states)));
            } catch (Exception e) {
                String errmsg = "Couldn't preload block with id \"%s\" (objPath: %s). Cause:";
                throw new ContentPackException(String.format(errmsg, id, objPath), e);
            }
        }
        Progress.pop(progressBar);

        // Complexsignals
        progressBar = Progress.push("Complexsignal", LOSBlocks.BLOCK_COMPLEX_SIGNAL.getContentpackComplexSignals().size());
        for(String id : LOSBlocks.BLOCK_COMPLEX_SIGNAL.getContentpackComplexSignals().keySet()){
            ModCore.info("Preloading complexsignal %s", id);
            progressBar.step(LOSBlocks.BLOCK_COMPLEX_SIGNAL.getContentpackComplexSignals().get(id).getName());

            final Map<String, ContentPackSignalGroup> signalGroups = LOSBlocks.BLOCK_COMPLEX_SIGNAL.getContentpackComplexSignals().get(id).getSignals();
            final Map<String, ContentPackModel[]> base = LOSBlocks.BLOCK_COMPLEX_SIGNAL.getContentpackComplexSignals().get(id).getBase();

            // Cache items
            try{
                ItemComplexSignalRender.checkCache(id, base, "/base/", false);
                ItemComplexSignalRender.checkCache(id, signalGroups.values());
            }catch (Exception e){
                throw new ContentPackException(String.format(GENERIC_ITEM_ERRMSG, id), e);
            }

            // Cache blocks
            try {
                TileComplexSignalRender.checkCache(id, base, "/base/", true);
                TileComplexSignalRender.checkCache(id, signalGroups.values(), "/signals/");
            }catch (Exception e){
                throw new ContentPackException(String.format(GENERIC_BLOCK_ERRMSG, id), e);
            }

        }
        Progress.pop(progressBar);

        // Blockdeco
        progressBar = Progress.push("Deco", LOSBlocks.BLOCK_DECO.getContentpackDeco().size());
        for(String id : LOSBlocks.BLOCK_DECO.getContentpackDeco().keySet()){
            ModCore.info("Preloading deco %s", id);
            progressBar.step(LOSBlocks.BLOCK_DECO.getContentpackDeco().get(id).getName());

            final Map<String, ContentPackModel[]> models = LOSBlocks.BLOCK_DECO.getContentpackDeco().get(id).getBase();

            // Cache items
            try{
                ItemDecoRender.checkCache(id, models);
            }catch (Exception e){
                throw new ContentPackException(String.format(GENERIC_ITEM_ERRMSG, id), e);
            }

            // Cache blocks
            try {
                TileDecoRender.checkCache(id, models);
            }catch (Exception e){
                throw new ContentPackException(String.format(GENERIC_BLOCK_ERRMSG, id), e);
            }
        }
        Progress.pop(progressBar);

        // Signparts
        progressBar = Progress.push("Sign", LOSBlocks.BLOCK_SIGN_PART.getContentpackSigns().size());
        for(String id : LOSBlocks.BLOCK_SIGN_PART.getContentpackSigns().keySet()){
            ModCore.info("Preloading sign %s", id);
            progressBar.step(LOSBlocks.BLOCK_SIGN_PART.getContentpackSigns().get(id).getName());

            final Map<String, ContentPackModel[]> base = LOSBlocks.BLOCK_SIGN_PART.getContentpackSigns().get(id).getBase();

            // Cache items
            try{
                ItemSignPartRender.checkCache(id, base);
            }catch (Exception e){
                throw new ContentPackException(String.format(GENERIC_ITEM_ERRMSG, id), e);
            }

            // Cache blocks
            try {
                TileSignPartRender.checkCache(id, base);
            }catch (Exception e){
                throw new ContentPackException(String.format(GENERIC_BLOCK_ERRMSG, id), e);
            }

        }
        Progress.pop(progressBar);

        // Signalboxes
        progressBar = Progress.push("Signalbox", LOSBlocks.BLOCK_SIGNAL_BOX.getContentpackSignalboxes().size());
        for(String id : LOSBlocks.BLOCK_SIGNAL_BOX.getContentpackSignalboxes().keySet()){
            ModCore.info("Preloading signalbox %s", id);
            progressBar.step(LOSBlocks.BLOCK_SIGNAL_BOX.getContentpackSignalboxes().get(id).getName());

            final Map<String, ContentPackModel[]> base = LOSBlocks.BLOCK_SIGNAL_BOX.getContentpackSignalboxes().get(id).getBase();

            // Cache items
            try{
                ItemSignalBoxRender.checkCache(id, base);
            }catch (Exception e){
                throw new ContentPackException(String.format(GENERIC_ITEM_ERRMSG, id), e);
            }

            // Cache blocks
            try {
                TileSignalBoxRender.checkCache(id, base);
            }catch (Exception e){
                throw new ContentPackException(String.format(GENERIC_BLOCK_ERRMSG, id), e);
            }

        }
        Progress.pop(progressBar);
        topProgressBar.step("Finishing");
        Progress.pop(topProgressBar);

        LandOfSignals.info("Finished preloading models");

    }

    // For method references
    private static <T> Predicate<T> not(Predicate<T> t) {
        return t.negate();
    }

}
