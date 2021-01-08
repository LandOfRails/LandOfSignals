package net.landofrails.landofsignals.gui;

import cam72cam.mod.entity.Player;
import cam72cam.mod.gui.screen.Button;
import cam72cam.mod.gui.screen.IScreen;
import cam72cam.mod.gui.screen.IScreenBuilder;

public class GuiTicketMachine implements IScreen {

    @Override
    public void init(IScreenBuilder screen) {
        new Button(screen, -100, -24, "Kaufen") {
            @Override
            public void onClick(Player.Hand hand) {
                //TODO
            }
        };
    }

    @Override
    public void onEnterKey(IScreenBuilder builder) {
        //TODO
    }

    @Override
    public void onClose() {
        //TODO
    }

    @Override
    public void draw(IScreenBuilder builder) {
        //TODO
    }
}
