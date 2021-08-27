package net.landofrails.stellwand;

import cam72cam.mod.ModCore;
import cam72cam.mod.ModEvent;
import cam72cam.mod.config.ConfigFile;
import net.landofrails.stellwand.config.StellwandConfig;
import net.landofrails.stellwand.content.blocks.CustomBlocks;
import net.landofrails.stellwand.content.entities.storage.BlockFillerStorageEntity;
import net.landofrails.stellwand.content.entities.storage.BlockMultisignalStorageEntity;
import net.landofrails.stellwand.content.entities.storage.BlockSenderStorageEntity;
import net.landofrails.stellwand.content.entities.storage.BlockSignalStorageEntity;
import net.landofrails.stellwand.content.guis.CustomGuis;
import net.landofrails.stellwand.content.items.CustomItems;
import net.landofrails.stellwand.content.network.CustomPackets;
import net.landofrails.stellwand.content.recipes.CustomRecipes;
import net.landofrails.stellwand.content.tabs.CustomTabs;
import net.landofrails.stellwand.contentpacks.loader.Loader;

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
                    Loader.init();

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
                break;
            case RELOAD:
            case START:
            case FINALIZE:
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
            case INITIALIZE:
            case SETUP:
            case RELOAD:
            case START:
            case FINALIZE:
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
