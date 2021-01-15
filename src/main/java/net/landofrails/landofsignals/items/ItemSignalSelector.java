package net.landofrails.landofsignals.items;

import cam72cam.mod.entity.Player;
import cam72cam.mod.gui.helpers.ItemPickerGUI;
import cam72cam.mod.item.CreativeTab;
import cam72cam.mod.item.CustomItem;
import cam72cam.mod.item.ItemStack;
import cam72cam.mod.world.World;
import net.landofrails.landofsignals.LOSTabs;
import net.landofrails.landofsignals.utils.Static;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ItemSignalSelector extends CustomItem {
    public ItemSignalSelector(String modID, String name) {
        super(modID, name);
    }

    @Override
    public List<CreativeTab> getCreativeTabs() {
        return Collections.singletonList(LOSTabs.SIGNALS_TAB);
    }

    @Override
    public void onClickAir(Player player, World world, Player.Hand hand) {
        List<ItemStack> itemStackList = new ArrayList<>();
        for (ItemSignalPart i : Static.itemSignalPartList) itemStackList.add(new ItemStack(i, 1));
        ItemPickerGUI gui = new ItemPickerGUI(itemStackList, itemStack -> {
            player.setHeldItem(hand, itemStack);
        });
        gui.show();
    }
}
