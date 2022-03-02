package net.landofrails.landofsignals.items;

import cam72cam.mod.entity.Player;
import cam72cam.mod.item.CreativeTab;
import cam72cam.mod.item.CustomItem;
import cam72cam.mod.world.World;
import net.landofrails.landofsignals.LOSGuis;
import net.landofrails.landofsignals.LOSTabs;

import java.util.Collections;
import java.util.List;

public class ItemSignSelector extends CustomItem {
    public ItemSignSelector(String modID, String name) {
        super(modID, name);
    }

    @Override
    public List<CreativeTab> getCreativeTabs() {
        return Collections.singletonList(LOSTabs.SIGNALS_TAB);
    }

    @Override
    public void onClickAir(Player player, World world, Player.Hand hand) {
        if (world.isClient) LOSGuis.SIGN_SELECTOR.open(player);
    }
}
