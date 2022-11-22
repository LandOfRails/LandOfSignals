package net.landofrails.landofsignals.gui;

import cam72cam.mod.entity.Player;
import cam72cam.mod.gui.screen.Button;
import cam72cam.mod.gui.screen.IScreen;
import cam72cam.mod.gui.screen.IScreenBuilder;

@SuppressWarnings("java:S1135")
public class GuiTicketMachine implements IScreen {

    @Override
    public void init(final IScreenBuilder screen) {
        new Button(screen, -100, -24, "Kaufen") {
            @Override
            public void onClick(final Player.Hand hand) {
                // Implement
            }
        };
    }

    @Override
    public void onEnterKey(final IScreenBuilder builder) {
        // Implement
    }

    @Override
    public void onClose() {
        // Implement
    }

    @Override
    public void draw(final IScreenBuilder builder) {
        // Implement
    }
}
