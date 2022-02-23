package net.landofrails.landofsignals.gui;

import cam72cam.mod.MinecraftClient;
import cam72cam.mod.gui.helpers.ItemPickerGUI;
import cam72cam.mod.gui.screen.IScreen;
import cam72cam.mod.gui.screen.IScreenBuilder;
import cam72cam.mod.item.IInventory;
import cam72cam.mod.item.ItemStack;
import cam72cam.mod.serialization.TagCompound;
import net.landofrails.landofsignals.LOSBlocks;
import net.landofrails.landofsignals.LOSItems;
import net.landofrails.landofsignals.packet.SignalSelectorGuiPacket;
import net.landofrails.landofsignals.utils.Static;

import java.util.ArrayList;
import java.util.List;

public class GuiSelectSignal implements IScreen {
    @Override
    public void init(IScreenBuilder screen) {
        List<ItemStack> itemStackList = new ArrayList<>();
        for (String id : LOSBlocks.BLOCK_SIGNAL_PART.getSignalParts().keySet()) {
            if (!id.equals(Static.MISSING)) {
                ItemStack is = new ItemStack(LOSItems.ITEM_SIGNAL_PART, 1);
                TagCompound tag = is.getTagCompound();
                tag.setString("itemId", id);
                is.setTagCompound(tag);
                itemStackList.add(is);
            }
        }
        for (String id : LOSBlocks.BLOCK_SIGNAL_PART_ANIMATED.getSignalParts().keySet()) {
            if (!id.equals(Static.MISSING)) {
                ItemStack is = new ItemStack(LOSItems.ITEM_SIGNAL_PART_ANIMATED, 1);
                TagCompound tag = is.getTagCompound();
                tag.setString("itemId", id);
                is.setTagCompound(tag);
                itemStackList.add(is);
            }
        }
        ItemPickerGUI gui = new ItemPickerGUI(itemStackList, itemStack -> {
            if (itemStack != null) {
                boolean intoInv = false;
                IInventory inv = MinecraftClient.getPlayer().getInventory();
                for (int i = 0; i != inv.getSlotCount(); i++) {
                    if (inv.get(i).isEmpty()) {
                        SignalSelectorGuiPacket packet = new SignalSelectorGuiPacket(itemStack, i, false);
                        packet.sendToServer();
                        intoInv = true;
                        break;
                    }
                }
                if (!intoInv) {
                    SignalSelectorGuiPacket packet = new SignalSelectorGuiPacket(itemStack, 0, true);
                    packet.sendToServer();
                }
            }
            screen.close();
        });
        gui.show();
    }

    @Override
    public void onEnterKey(IScreenBuilder builder) {
        //Not finished
    }

    @Override
    public void onClose() {
        //Not finished
    }

    @Override
    public void draw(IScreenBuilder builder) {
        //Not finished
    }
}
