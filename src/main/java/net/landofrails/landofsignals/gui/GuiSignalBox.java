package net.landofrails.landofsignals.gui;

import cam72cam.mod.entity.Player;
import cam72cam.mod.gui.screen.Button;
import cam72cam.mod.gui.screen.IScreen;
import cam72cam.mod.gui.screen.IScreenBuilder;

public class GuiSignalBox implements IScreen {

    private Button buyButton;

    @Override
    public void init(IScreenBuilder screen) {
        buyButton = new Button(screen, 0 - 100, 250, "Speichern") {
            @Override
            public void onClick(Player.Hand hand) {

            }
        };
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
