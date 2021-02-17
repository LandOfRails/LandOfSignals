package net.landofrails.landofsignals.gui;

import cam72cam.mod.MinecraftClient;
import cam72cam.mod.gui.helpers.ItemPickerGUI;
import cam72cam.mod.gui.screen.IScreen;
import cam72cam.mod.gui.screen.IScreenBuilder;
import cam72cam.mod.item.IInventory;
import cam72cam.mod.item.ItemStack;
import net.landofrails.landofsignals.items.ItemSignalPart;
import net.landofrails.landofsignals.packet.SignalSelectorGuiPacket;
import net.landofrails.landofsignals.utils.Static;

import java.util.ArrayList;
import java.util.List;

public class GuiSelectSignal implements IScreen {
    @Override
    public void init(IScreenBuilder screen) {
        List<ItemStack> itemStackList = new ArrayList<>();
        for (ItemSignalPart i : Static.itemSignalPartList) itemStackList.add(new ItemStack(i, 1));
        ItemPickerGUI gui = new ItemPickerGUI(itemStackList, itemStack -> {
            if (itemStack != null) {
                boolean intoInv = false;
                IInventory inv = MinecraftClient.getPlayer().getInventory();
                for (int i = 0; i != inv.getSlotCount(); i++) {
                    if (inv.get(i).isEmpty()) {
                        SignalSelectorGuiPacket packet = new SignalSelectorGuiPacket(itemStack, MinecraftClient.getPlayer(), i, false);
                        packet.sendToServer();
                        intoInv = true;
                        break;
                    }
                }
                if (!intoInv) {
                    SignalSelectorGuiPacket packet = new SignalSelectorGuiPacket(itemStack, MinecraftClient.getPlayer(), 0, true);
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
