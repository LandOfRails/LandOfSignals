package net.landofrails.stellwand;

import cam72cam.mod.ModCore;
import cam72cam.mod.ModEvent;
import cam72cam.mod.config.ConfigFile;
import net.landofrails.stellwand.config.StellwandConfig;
import net.landofrails.stellwand.content.blocks.CustomBlocks;
import net.landofrails.stellwand.content.entities.storage.*;
import net.landofrails.stellwand.content.guis.CustomGuis;
import net.landofrails.stellwand.content.items.CustomItems;
import net.landofrails.stellwand.content.items.connector.AItemConnector;
import net.landofrails.stellwand.content.network.CustomPackets;
import net.landofrails.stellwand.content.recipes.CustomRecipes;
import net.landofrails.stellwand.content.tabs.CustomTabs;
import net.minecraftforge.fml.common.Loader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class Stellwand {

    public static final String DOMAIN = "stellwand";

    // Version 2 statt 0.0.2
    public static final String ADDON_VERSION = "2";

    private Stellwand() {

    }

    // Gets called before clientEvent and serverEvent
    public static void commonEvent(ModEvent event) {

        switch (event) {
            case CONSTRUCT:

                // Config
                StellwandConfig.init();
                ConfigFile.sync(StellwandConfig.class);

                if (!StellwandConfig.disableStellwand) {
                    net.landofrails.stellwand.contentpacks.loader.Loader.init();

                    AItemConnector.registerConnectors();

                    CustomGuis.register();
                    CustomTabs.register();
                    CustomItems.register();
                    CustomRecipes.register();
                    CustomBlocks.init();

                    CustomPackets.register();
                }

                break;
            case INITIALIZE:
                break;
            case SETUP:
                if (StellwandConfig.disableStellwand) {
                    return;
                }

                // Loading here, Files not available at CONSTRUCT
                BlockFillerStorageEntity.prepare();
                BlockSignalStorageEntity.prepare();
                BlockSenderStorageEntity.prepare();
                BlockMultisignalStorageEntity.prepare();
                BlockButtonReceiverStorageEntity.prepare();
                break;
            case RELOAD:
            case START:
            case FINALIZE:
            default:
                break;
        }

    }


    public static void clientEvent(ModEvent event) {

        if (StellwandConfig.disableStellwand) {
            return;
        }

        switch (event) {
            case CONSTRUCT:

                CustomItems.registerRenderers();
                CustomBlocks.registerBlockRenderers();

                break;
            case RELOAD:
                // Release renderers after reload (rejoining a world for example)
                BlockButtonReceiverStorageEntity.releaseRenderersIntoTheWild();
                BlockFillerStorageEntity.releaseRenderersIntoTheWild();
                BlockMultisignalStorageEntity.releaseRenderersIntoTheWild();
                BlockSenderStorageEntity.releaseRenderersIntoTheWild();
                BlockSignalStorageEntity.releaseRenderersIntoTheWild();

                File configDir = Loader.instance().getConfigDir();
                File cacheDir = Paths.get(configDir.getParentFile().getPath(), "cache", "universalmodcore").toFile();
                if (cacheDir.exists()) {
                    File[] files = cacheDir.listFiles();
                    if (files == null)
                        files = new File[0];
                    for (File dir : files) {
                        Path meta = Paths.get(cacheDir.getPath(), "meta.nbt");
                        try (Stream<String> stream = Files.lines(meta)) {
                            boolean remove = stream.anyMatch(lines -> lines.contains("landofsignals"));
                            if (remove) {
                                ModCore.warn("Removeing: " + dir.getName());
                                Files.deleteIfExists(dir.toPath());
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                break;
            default:
                break;
        }

    }

    public static void serverEvent(ModEvent event) {

        if (StellwandConfig.disableStellwand) {
            return;
        }

        switch (event) {
            case CONSTRUCT:
            case INITIALIZE:
            case SETUP:
            case RELOAD:
            case START:
            case FINALIZE:
            default:
                break;
        }

    }

    public static void info(String msg, Object... params) {
        ModCore.info(msg, params);
    }

    public static void warn(String msg, Object... params) {
        ModCore.warn(msg, params);
    }

    public static void error(String msg, Object... params) {
        ModCore.error(msg, params);
    }

    public static void debug(String msg, Object... params) {
        if (StellwandConfig.Debugging.debugOutput)
            ModCore.debug(msg, params);
    }

}
