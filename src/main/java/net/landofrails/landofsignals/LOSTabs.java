package net.landofrails.landofsignals;

import cam72cam.mod.item.CreativeTab;
import cam72cam.mod.item.ItemStack;
import net.landofrails.landofsignals.utils.Static;

public class LOSTabs {

    private LOSTabs() {

    }

    public static final CreativeTab SIGNALS_TAB;
    public static final CreativeTab ASSETS_TAB;
    public static final CreativeTab HIDDEN_TAB;

    static {
        SIGNALS_TAB = new CreativeTab(LandOfSignals.MODID + ".signals", () -> new ItemStack(Static.itemSignalPartList.get(0), 1));
        ASSETS_TAB = new CreativeTab(LandOfSignals.MODID + ".assets", () -> new ItemStack(LOSItems.ITEM_TICKET_MACHINE_DB, 1));
        HIDDEN_TAB = new CreativeTab(null);
    }

}
