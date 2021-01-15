package net.landofrails.landofsignals.gui;

import cam72cam.mod.entity.Player;
import cam72cam.mod.gui.screen.Button;
import cam72cam.mod.gui.screen.IScreen;
import cam72cam.mod.gui.screen.IScreenBuilder;
import cam72cam.mod.item.ItemStack;
import net.landofrails.landofsignals.items.ItemSignalPart;
import net.landofrails.landofsignals.utils.Static;

import java.util.List;

public class GuiSelectSignal implements IScreen {
    @Override
    public void init(IScreenBuilder screen) {
        List<ItemSignalPart> itemSignalPartList = Static.itemSignalPartList;
        for (int i = 0, itemSignalPartListSize = itemSignalPartList.size(); i < itemSignalPartListSize; i++) {
            ItemSignalPart item = itemSignalPartList.get(i);
            new Button(screen, 0, 0, item.getCustomName(new ItemStack(item, 1))) {
                @Override
                public void onClick(Player.Hand hand) {

                }
            };
        }
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
