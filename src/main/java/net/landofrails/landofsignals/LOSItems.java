package net.landofrails.landofsignals;

import net.landofrails.landofsignals.items.*;

public class LOSItems {

    private LOSItems() {

    }

    public static final ItemSignalLever ITEM_SIGNAL_LEVER = new ItemSignalLever(LandOfSignals.MODID, "item_signal_lever");
    public static final ItemConnector ITEM_CONNECTOR = new ItemConnector(LandOfSignals.MODID, "item_connector");
    public static final ItemManipulator ITEM_MANIPULATOR = new ItemManipulator(LandOfSignals.MODID, "item_manipulator");

    public static final ItemSignalPart ITEM_SIGNAL_PART = new ItemSignalPart(LandOfSignals.MODID, "item_signal_part");
    public static final ItemSignPart ITEM_SIGNAL_PART_ANIMATED = null; // new ItemSignPart(LandOfSignals.MODID, "item_signal_part_animated");
    public static final ItemSignPart ITEM_SIGN_PART = new ItemSignPart(LandOfSignals.MODID, "item_sign");
    public static final ItemSignalBox ITEM_SIGNAL_BOX = new ItemSignalBox(LandOfSignals.MODID, "item_signal_box");
    public static final ItemDeco ITEM_DECO = new ItemDeco(LandOfSignals.MODID, "item_deco");

    public static void register() {
        // loads static classes and ctrs
    }
}
