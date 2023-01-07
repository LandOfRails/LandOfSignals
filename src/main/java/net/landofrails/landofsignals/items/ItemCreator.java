package net.landofrails.landofsignals.items;

import cam72cam.mod.item.CreativeTab;
import cam72cam.mod.item.CustomItem;
import net.landofrails.landofsignals.LOSTabs;

import java.util.List;

public class ItemCreator extends CustomItem {

    public ItemCreator(final String modID, final String name) {
        super(modID, name);
    }

    @Override
    public List<CreativeTab> getCreativeTabs() {
        return LOSTabs.getAsList(LOSTabs.HIDDEN_TAB);
    }
}
