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
import net.landofrails.landofsignals.packet.SignSelectorGuiPacket;
import net.landofrails.landofsignals.utils.Static;

import java.util.ArrayList;
import java.util.List;

public class GuiSelectSign implements IScreen {
    @Override
    public void init(IScreenBuilder screen) {
        List<ItemStack> itemStackList = new ArrayList<>();
        for (String id : LOSBlocks.BLOCK_SIGN_PART.getContentpackSigns().keySet()) {
            if (!id.equals(Static.MISSING)) {
                ItemStack is = new ItemStack(LOSItems.ITEM_SIGN_PART, 1);
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
                        SignSelectorGuiPacket packet = new SignSelectorGuiPacket(itemStack, MinecraftClient.getPlayer(), i, false);
                        packet.sendToServer();
                        intoInv = true;
                        break;
                    }
                }
                if (!intoInv) {
                    SignSelectorGuiPacket packet = new SignSelectorGuiPacket(itemStack, MinecraftClient.getPlayer(), 0, true);
                    packet.sendToServer();
                }
            }
            screen.close();
        });
        gui.show();
    }

    @Override
    public void onEnterKey(IScreenBuilder builder) {

    }

    @Override
    public void onClose() {

    }

    @Override
    public void draw(IScreenBuilder builder) {

    }
}
