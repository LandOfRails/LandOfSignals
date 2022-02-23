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
    public void init(final IScreenBuilder screen) {
        final List<ItemStack> itemStackList = new ArrayList<>();
        for (final String id : LOSBlocks.BLOCK_SIGNAL_PART.getSignalParts().keySet()) {
            if (!id.equals(Static.MISSING)) {
                final ItemStack is = new ItemStack(LOSItems.ITEM_SIGNAL_PART, 1);
                final TagCompound tag = is.getTagCompound();
                tag.setString("itemId", id);
                is.setTagCompound(tag);
                itemStackList.add(is);
            }
        }
        for (final String id : LOSBlocks.BLOCK_SIGNAL_PART_ANIMATED.getSignalParts().keySet()) {
            if (!id.equals(Static.MISSING)) {
                final ItemStack is = new ItemStack(LOSItems.ITEM_SIGNAL_PART_ANIMATED, 1);
                final TagCompound tag = is.getTagCompound();
                tag.setString("itemId", id);
                is.setTagCompound(tag);
                itemStackList.add(is);
            }
        }
        final ItemPickerGUI gui = new ItemPickerGUI(itemStackList, itemStack -> {
            if (itemStack != null) {
                boolean intoInv = false;
                final IInventory inv = MinecraftClient.getPlayer().getInventory();
                for (int i = 0; i != inv.getSlotCount(); i++) {
                    if (inv.get(i).isEmpty()) {
                        final SignalSelectorGuiPacket packet = new SignalSelectorGuiPacket(itemStack, i, false);
                        packet.sendToServer();
                        intoInv = true;
                        break;
                    }
                }
                if (!intoInv) {
                    final SignalSelectorGuiPacket packet = new SignalSelectorGuiPacket(itemStack, 0, true);
                    packet.sendToServer();
                }
            }
            screen.close();
        });
        gui.show();
    }

    @Override
    public void onEnterKey(final IScreenBuilder builder) {
        //Not finished
    }

    @Override
    public void onClose() {
        //Not finished
    }

    @Override
    public void draw(final IScreenBuilder builder) {
        //Not finished
    }
}
