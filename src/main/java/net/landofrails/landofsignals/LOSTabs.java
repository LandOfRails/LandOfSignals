package net.landofrails.landofsignals;

import cam72cam.mod.item.CreativeTab;
import cam72cam.mod.item.ItemStack;

public class LOSTabs {

    public static CreativeTab MAIN_TAB;

    static {
        MAIN_TAB = new CreativeTab(LandOfSignals.MODID + ".main", () -> new ItemStack(net.landofrails.landofsignals.LOSItems.ITEM_SIGNALSO12, 1));
    }

}
