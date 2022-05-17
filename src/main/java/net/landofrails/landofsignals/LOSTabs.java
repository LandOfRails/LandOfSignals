package net.landofrails.landofsignals;

import cam72cam.mod.item.CreativeTab;
import cam72cam.mod.item.ItemStack;
import cam72cam.mod.serialization.TagCompound;

public class LOSTabs {

    private LOSTabs() {

    }

    public static CreativeTab SIGNALS_TAB;
    public static CreativeTab ASSETS_TAB;
    public static CreativeTab HIDDEN_TAB;

    public static void register() {
        SIGNALS_TAB = new CreativeTab(LandOfSignals.MODID + ".signals", LOSTabs::getFirstSignalPart);
        ASSETS_TAB = new CreativeTab(LandOfSignals.MODID + ".assets", () -> new ItemStack(LOSItems.ITEM_TICKET_MACHINE_DB, 1));
        HIDDEN_TAB = new CreativeTab(null);
    }

    /**
     * @return the first signalpart found
     */
    private static ItemStack getFirstSignalPart() {
        ItemStack itemStack = new ItemStack(LOSItems.ITEM_SIGNAL_PART, 1);
        TagCompound tag = itemStack.getTagCompound();
        String id = LOSBlocks.BLOCK_SIGNAL_PART.getContentpackSignals().keySet().iterator().next();
        tag.setString("itemId", id);
        itemStack.setTagCompound(tag);
        return itemStack;
    }

}
