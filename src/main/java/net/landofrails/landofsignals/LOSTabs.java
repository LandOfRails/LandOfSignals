package net.landofrails.landofsignals;

import cam72cam.mod.item.CreativeTab;
import cam72cam.mod.item.ItemStack;

public class LOSTabs {

    private LOSTabs() {

    }

    public static final CreativeTab SIGNALS_TAB;
    public static final CreativeTab ASSETS_TAB;
    public static final CreativeTab HIDDEN_TAB;

    static {
        ASSETS_TAB = new CreativeTab(LandOfSignals.MODID + ".assets", () -> new ItemStack(LOSItems.ITEM_TICKET_MACHINE_DB, 1));
        HIDDEN_TAB = new CreativeTab(null);
//        ItemStack is = new ItemStack(LOSItems.ITEM_SIGNAL_PART, 1);
//        TagCompound tag = is.getTagCompound();
//        tag.setString("itemId", LOSBlocks.BLOCK_SIGNAL_PART.getSignalParts().keySet().iterator().next());
//        is.setTagCompound(tag);
        SIGNALS_TAB = new CreativeTab(LandOfSignals.MODID + ".signals", () -> new ItemStack(LOSItems.ITEM_SIGNAL_BOX, 1));
    }

}
