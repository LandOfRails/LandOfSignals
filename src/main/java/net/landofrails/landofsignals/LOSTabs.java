package net.landofrails.landofsignals;

import cam72cam.mod.item.CreativeTab;
import cam72cam.mod.item.Fuzzy;
import cam72cam.mod.item.ItemStack;
import cam72cam.mod.serialization.TagCompound;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LOSTabs {

    private LOSTabs() {

    }

    private static final Map<String, CreativeTab> CREATIVE_TABS = new HashMap<>();

    public static final String SIGNALS_TAB = "default";
    public static final String SIGNS_TAB = "signs";
    public static final String ASSETS_TAB = "assets";
    public static final String HIDDEN_TAB = "hidden";

    public static void register() {
        CREATIVE_TABS.put(SIGNALS_TAB, new CreativeTab(LandOfSignals.MODID + ".signals", LOSTabs::getFirstSignalPart));
        CREATIVE_TABS.put(SIGNS_TAB, new CreativeTab(LandOfSignals.MODID + ".signs", LOSTabs::getFirstSignPart));
        CREATIVE_TABS.put(ASSETS_TAB, new CreativeTab(LandOfSignals.MODID + ".assets", LOSTabs::getFirstDeco));
        CREATIVE_TABS.put(HIDDEN_TAB, new CreativeTab(null));
    }

    public static void register(String creativeTab) {
        CREATIVE_TABS.putIfAbsent(creativeTab, new CreativeTab(LandOfSignals.MODID + ".signals", Fuzzy.GLASS_PANE::example));
    }

    public static CreativeTab get(String creativeTab) {
        return CREATIVE_TABS.get(creativeTab);
    }

    public static List<CreativeTab> getAsList(String signalsTab) {
        return Collections.singletonList(get(signalsTab));
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

    /**
     * @return the first signpart found
     */
    private static ItemStack getFirstSignPart() {
        ItemStack itemStack = new ItemStack(LOSItems.ITEM_SIGN_PART, 1);
        TagCompound tag = itemStack.getTagCompound();
        String id = LOSBlocks.BLOCK_SIGN_PART.getContentpackSigns().keySet().iterator().next();
        tag.setString("itemId", id);
        itemStack.setTagCompound(tag);
        return itemStack;
    }

    private static ItemStack getFirstDeco() {
        ItemStack itemStack = new ItemStack(LOSItems.ITEM_DECO, 1);
        TagCompound tag = itemStack.getTagCompound();
        String id = LOSBlocks.BLOCK_DECO.getContentpackDeco().keySet().iterator().next();
        tag.setString("itemId", id);
        itemStack.setTagCompound(tag);
        return itemStack;
    }

}
