package net.landofrails.stellwand.contentpacks.loader;

import cam72cam.mod.ModCore;
import net.landofrails.stellwand.Stellwand;
import net.landofrails.stellwand.contentpacks.Content;
import net.landofrails.stellwand.contentpacks.entries.ContentPack;
import net.landofrails.stellwand.contentpacks.entries.filler.BlockFillerEntry;
import net.landofrails.stellwand.contentpacks.entries.filler.BlockFillerEntryBlock;
import net.landofrails.stellwand.contentpacks.entries.filler.BlockFillerEntryItem;
import net.landofrails.stellwand.contentpacks.entries.multisignal.BlockMultisignalEntry;
import net.landofrails.stellwand.contentpacks.entries.multisignal.BlockMultisignalEntryBlock;
import net.landofrails.stellwand.contentpacks.entries.multisignal.BlockMultisignalEntryItem;
import net.landofrails.stellwand.contentpacks.entries.parent.ContentPackEntry;
import net.landofrails.stellwand.contentpacks.entries.sender.BlockSenderEntry;
import net.landofrails.stellwand.contentpacks.entries.sender.BlockSenderEntryBlock;
import net.landofrails.stellwand.contentpacks.entries.sender.BlockSenderEntryItem;
import net.landofrails.stellwand.contentpacks.entries.signal.BlockSignalEntry;
import net.landofrails.stellwand.contentpacks.entries.signal.BlockSignalEntryBlock;
import net.landofrails.stellwand.contentpacks.entries.signal.BlockSignalEntryItem;
import net.landofrails.stellwand.contentpacks.types.DirectionType;
import net.landofrails.stellwand.contentpacks.types.EntryType;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static net.landofrails.stellwand.contentpacks.types.DirectionType.*;

public class StaticLoader {

    private StaticLoader() {

    }

    public static void init() {
        ContentPack contentPack = new ContentPack(Stellwand.ADDON_VERSION, "LandOfSignals", "1.0.0", "SeltixSub", new ArrayList<>());
        ModCore.info("Loading ContentPack: %s v%s", contentPack.getName(), contentPack.getPackversion());
        contentPack.setEntries(getEntries());
        Content.addContentPack(contentPack);
    }

    private static List<ContentPackEntry> getEntries() {

        List<ContentPackEntry> contentPackEntries = new ArrayList<>();
        Properties prop;

        //// Senders

        // Blocksender
        prop = new Properties().setName("Blocksender");
        prop.setType(EntryType.BLOCKSENDER);
        prop.setModel("models/block/blocksender/blocksender/blocksender.obj");
        contentPackEntries.add(prop.toEntry());

        // Microchip
        prop = new Properties().setName("Microchip Sender");
        prop.setType(EntryType.BLOCKSENDER);
        prop.setModel("models/block/blocksender/microchip/microchip.obj");
        contentPackEntries.add(prop.toEntry());

        //// Signals

        // Signal Straight Track
        prop = new Properties().setName("Signal Straight Track");
        prop.setType(EntryType.BLOCKSIGNAL);
        prop.setModel("models/block/blocksignal/blocktrackstraight/blocksignal.obj");
        prop.setModes(modes("Off", "off", "White", "white", "Red", "red"));
        prop.setFromDir(LEFT).setToDir(RIGHT);
        prop.setItemMode("white");
        contentPackEntries.add(prop.toEntry());

        // Signal Straight Unisolated
        prop = new Properties().setName("Signal Straight Unisolated");
        prop.setType(EntryType.BLOCKSIGNAL);
        prop.setModel("models/block/blocksignal/blocktrackunisolated/unisolated.obj");
        prop.setModes(modes("Off", "off", "White", "white"));
        prop.setFromDir(LEFT).setToDir(RIGHT);
        prop.setItemMode("white");
        contentPackEntries.add(prop.toEntry());

        // Signal Diagonal Track
        prop = new Properties().setName("Signal Diagonal Track DL");
        prop.setType(EntryType.BLOCKSIGNAL);
        prop.setModel("models/block/blocksignal/trackdiag/downleft/trackdiagdownleft.obj");
        prop.setModes(modes("Off", "off", "White", "white", "Red", "red"));
        prop.setFromDir(LEFT).setToDir(BOTTOM);
        prop.setItemMode("white");
        contentPackEntries.add(prop.toEntry());

        prop = new Properties().setName("Signal Diagonal Track DR");
        prop.setType(EntryType.BLOCKSIGNAL);
        prop.setModel("models/block/blocksignal/trackdiag/downright/trackdiagdownright.obj");
        prop.setModes(modes("Off", "off", "White", "white", "Red", "red"));
        prop.setFromDir(RIGHT).setToDir(BOTTOM);
        prop.setItemMode("white");
        contentPackEntries.add(prop.toEntry());

        prop = new Properties().setName("Signal Diagonal Track UL");
        prop.setType(EntryType.BLOCKSIGNAL);
        prop.setModel("models/block/blocksignal/trackdiag/upleft/trackdiagupleft.obj");
        prop.setModes(modes("Off", "off", "White", "white", "Red", "red"));
        prop.setFromDir(LEFT).setToDir(TOP);
        prop.setItemMode("white");
        contentPackEntries.add(prop.toEntry());

        prop = new Properties().setName("Signal Diagonal Track UR");
        prop.setType(EntryType.BLOCKSIGNAL);
        prop.setModel("models/block/blocksignal/trackdiag/upright/trackdiagupright.obj");
        prop.setModes(modes("Off", "off", "White", "white", "Red", "red"));
        prop.setFromDir(RIGHT).setToDir(TOP);
        prop.setItemMode("white");
        contentPackEntries.add(prop.toEntry());

        // Signal HSig
        prop = new Properties().setName("Signal HSig Left");
        prop.setType(EntryType.BLOCKSIGNAL);
        prop.setModel("models/block/blocksignal/trackmainsignal/left/hsigleft.obj");
        prop.setModes(modes("Off", "off", "Green", "green", "Orange", "orange", "Red", "red"));
        prop.setFromDir(LEFT).setToDir(RIGHT);
        prop.setItemMode("green");
        contentPackEntries.add(prop.toEntry());

        prop = new Properties().setName("Signal HSig Right");
        prop.setType(EntryType.BLOCKSIGNAL);
        prop.setModel("models/block/blocksignal/trackmainsignal/right/hsigright.obj");
        prop.setModes(modes("Off", "off", "Green", "green", "Orange", "orange", "Red", "red"));
        prop.setFromDir(LEFT).setToDir(RIGHT);
        prop.setItemMode("green");
        contentPackEntries.add(prop.toEntry());

        // Signal dwarfsignal
        prop = new Properties().setName("Dwarfsignal bottom");
        prop.setType(EntryType.BLOCKSIGNAL);
        prop.setModel("models/block/blocksignal/blockdwarfsignal/blockdwarfsignalbottom.obj");
        prop.setModes(modes("Off", "off", "Green", "green"));
        prop.setFromDir(LEFT).setToDir(RIGHT);
        prop.setItemMode("green");
        contentPackEntries.add(prop.toEntry());

        prop = new Properties().setName("Dwarfsignal top");
        prop.setType(EntryType.BLOCKSIGNAL);
        prop.setModel("models/block/blocksignal/blockdwarfsignal/blockdwarfsignaltop.obj");
        prop.setModes(modes("Off", "off", "Green", "green"));
        prop.setFromDir(LEFT).setToDir(RIGHT);
        prop.setItemMode("green");
        contentPackEntries.add(prop.toEntry());

        // Track Switches
        // Left -> Right, Down (LRD)
        prop = new Properties().setName("Track switch (Left -> Right, Down)");
        prop.setType(EntryType.BLOCKSIGNAL);
        prop.setModel("models/block/blocksignal/trackswitch/lrd/trackswitch.obj");
        prop.setModes(modes("Off", "off", "Main single white", "main_signalwhite", "Main double white", "main_doublewhite",
                "Branch white", "branch_white", "Main double red", "main_doublered", "Branch red", "branch_red"));
        prop.setFromDir(LEFT).setToDir(RIGHT, BOTTOM);
        prop.setItemMode("branch_white");
        contentPackEntries.add(prop.toEntry());

        // Left -> Right, Up (LRU)
        prop = new Properties().setName("Track switch (Left -> Right, Up)");
        prop.setType(EntryType.BLOCKSIGNAL);
        prop.setModel("models/block/blocksignal/trackswitch/lru/trackswitch.obj");
        prop.setModes(modes("Off", "off", "Main single white", "main_signalwhite", "Main double white", "main_doublewhite", "Branch white",
                "branch_white", "Main double red", "main_doublered", "Branch red", "branch_red"));
        prop.setFromDir(LEFT).setToDir(RIGHT, TOP);
        prop.setItemMode("branch_white");
        contentPackEntries.add(prop.toEntry());

        // Right -> Left, Down (RLD)
        prop = new Properties().setName("Track switch (Right -> Left, Down)");
        prop.setType(EntryType.BLOCKSIGNAL);
        prop.setModel("models/block/blocksignal/trackswitch/rld/trackswitch.obj");
        prop.setModes(modes("Off", "off", "Main single white", "main_signalwhite", "Main double white", "main_doublewhite", "Branch white",
                "branch_white", "Main double red", "main_doublered", "Branch red", "branch_red"));
        prop.setFromDir(RIGHT).setToDir(LEFT, BOTTOM);
        prop.setItemMode("branch_white");
        contentPackEntries.add(prop.toEntry());

        // Right -> Left, Up (RLU)
        prop = new Properties().setName("Track switch (Right -> Left, Up)");
        prop.setType(EntryType.BLOCKSIGNAL);
        prop.setModel("models/block/blocksignal/trackswitch/rlu/trackswitch.obj");
        prop.setModes(modes("Off", "off", "Main single white", "main_signalwhite", "Main double white", "main_doublewhite", "Branch white",
                "branch_white", "Main double red", "main_doublered", "Branch red", "branch_red"));
        prop.setFromDir(RIGHT).setToDir(LEFT, TOP);
        prop.setItemMode("branch_white");
        contentPackEntries.add(prop.toEntry());


        //// Fillers

        // Block Filler
        prop = new Properties().setName("Block Filler");
        prop.setType(EntryType.BLOCKFILLER);
        prop.setModel("models/block/blockfiller/blockfiller/blockfiller.obj");
        prop.setBlockTranslation(0.5f, 0f, 0.5f);
        prop.setItemTranslation(0.5f, 0.1625f, 0.5f);
        contentPackEntries.add(prop.toEntry());

        // Straight Track
        prop = new Properties().setName("Straight track");
        prop.setType(EntryType.BLOCKFILLER);
        prop.setModel("models/block/blockfiller/trackstraight/trackstraight.obj");
        contentPackEntries.add(prop.toEntry());

        // TrackDiag (down left)
        prop = new Properties().setName("Diagonal track (down - left)");
        prop.setType(EntryType.BLOCKFILLER);
        prop.setModel("models/block/blockfiller/trackdiag/dl/trackdiag.obj");
        contentPackEntries.add(prop.toEntry());

        // TrackDiag (down right)
        prop = new Properties().setName("Diagonal track (down - right)");
        prop.setType(EntryType.BLOCKFILLER);
        prop.setModel("models/block/blockfiller/trackdiag/dr/trackdiag.obj");
        contentPackEntries.add(prop.toEntry());

        // TrackDiag (up left)
        prop = new Properties().setName("Diagonal track (up - left)");
        prop.setType(EntryType.BLOCKFILLER);
        prop.setModel("models/block/blockfiller/trackdiag/ul/trackdiag.obj");
        contentPackEntries.add(prop.toEntry());

        // TrackDiag (up right)
        prop = new Properties().setName("Diagonal track (up - right)");
        prop.setType(EntryType.BLOCKFILLER);
        prop.setModel("models/block/blockfiller/trackdiag/ur/trackdiag.obj");
        contentPackEntries.add(prop.toEntry());

        return contentPackEntries;
    }

    private static LinkedHashMap<String, String> modes(String... modes) {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();

        for (int i = 0; i < modes.length; i += 2) {
            String key = modes[i];
            String value = modes[i + 1];
            map.put(key, value);
        }

        return map;
    }

    private static class Properties {

        private EntryType type;
        private String name;
        private DirectionType[] fromDir;
        private DirectionType[] toDir;
        private String model;

        private LinkedHashMap<String, String> modes = new LinkedHashMap<>();
        private List<Map<String, String>> modeList = new ArrayList<>();

        private String itemMode;
        private float[] blockRotation = new float[]{0f, 0f, 0f};
        private float[] blockTranslation = new float[]{0.5f, 0.5f, 0.5f};
        private float[] itemRotation = new float[]{15f, 195f, 0};
        private float[] itemTranslation = new float[]{0.5f, 0.5f, 0.5f};
        private float scale = 0.7f;

        public Properties setType(EntryType type) {
            this.type = type;
            return this;
        }

        public Properties setName(String name) {
            this.name = name;
            return this;
        }

        public Properties setFromDir(DirectionType... fromDir) {
            this.fromDir = fromDir;
            return this;
        }

        public Properties setToDir(DirectionType... toDir) {
            this.toDir = toDir;
            return this;
        }

        public Properties setModel(String model) {
            this.model = model;
            return this;
        }

        public Properties setModes(LinkedHashMap<String, String> modes) {
            this.modes = modes;
            return this;
        }

        public Properties setModeList(List<Map<String, String>> modeList) {
            this.modeList = modeList;
            return this;
        }

        public Properties setItemMode(String itemMode) {
            this.itemMode = itemMode;
            return this;
        }

        public Properties setBlockRotation(float... blockRotation) {
            this.blockRotation = blockRotation;
            return this;
        }

        public Properties setBlockTranslation(float... blockTranslation) {
            this.blockTranslation = blockTranslation;
            return this;
        }

        public Properties setItemRotation(float... itemRotation) {
            this.itemRotation = itemRotation;
            return this;
        }

        public Properties setItemTranslation(float... itemTranslation) {
            this.itemTranslation = itemTranslation;
            return this;
        }

        public Properties setScale(float scale) {
            this.scale = scale;
            return this;
        }

        public ContentPackEntry toEntry() {
            switch (type) {
                case BLOCKFILLER:
                    BlockFillerEntryBlock fillerBlock = new BlockFillerEntryBlock(blockRotation, blockTranslation);
                    BlockFillerEntryItem fillerItem = new BlockFillerEntryItem(itemRotation, itemTranslation, scale, model, itemMode);
                    return new BlockFillerEntry(name, fromDir, toDir, model, fillerBlock, fillerItem);
                case BLOCKSENDER:
                    BlockSenderEntryBlock senderBlock = new BlockSenderEntryBlock(blockRotation, blockTranslation);
                    BlockSenderEntryItem senderItem = new BlockSenderEntryItem(itemRotation, itemTranslation, scale, model, itemMode);
                    return new BlockSenderEntry(name, fromDir, toDir, model, senderBlock, senderItem);
                case BLOCKSIGNAL:
                    BlockSignalEntryBlock signalBlock = new BlockSignalEntryBlock(blockRotation, blockTranslation, modes);
                    BlockSignalEntryItem signalItem = new BlockSignalEntryItem(itemRotation, itemTranslation, scale, model, itemMode);
                    return new BlockSignalEntry(name, model, signalBlock, signalItem);
                case BLOCKMULTISIGNAL:
                    BlockMultisignalEntryBlock multisignalBlock = new BlockMultisignalEntryBlock(blockRotation, blockTranslation, modeList);
                    BlockMultisignalEntryItem multisignalItem = new BlockMultisignalEntryItem(itemRotation, itemTranslation, scale, model, itemMode);
                    return new BlockMultisignalEntry(name, model, multisignalBlock, multisignalItem);
                default:
                    return null;
            }
        }

    }

}
