package net.landofrails.landofsignals.creator.gui;

import cam72cam.mod.MinecraftClient;
import cam72cam.mod.entity.Player;
import cam72cam.mod.gui.GuiRegistry;
import cam72cam.mod.gui.helpers.GUIHelpers;
import cam72cam.mod.gui.screen.Button;
import cam72cam.mod.gui.screen.IScreen;
import cam72cam.mod.gui.screen.IScreenBuilder;
import cam72cam.mod.gui.screen.TextField;
import net.landofrails.api.contentpacks.v2.signal.ContentPackSignal;
import net.landofrails.landofsignals.LOSGuis;
import net.landofrails.landofsignals.gui.GuiText;

import java.util.function.Supplier;

public class GuiStates implements IScreen {
    private static final Supplier<GuiRegistry.GUI> GUI = () -> LOSGuis.CREATOR_STATES;

    private static ContentPackSignal signal;
    private TextField statesTextField;

    @Override
    public void init(IScreenBuilder screen) {
        statesTextField = new TextField(screen, GUIHelpers.getScreenWidth() / 2, GUIHelpers.getScreenHeight() / 2 - 100, 100, 20);
        Button createStateButton = new Button(screen, GUIHelpers.getScreenWidth() / 2, GUIHelpers.getScreenHeight() - 100, GuiText.LABEL_CREATOR_CREATE.toString()) {
            @Override
            public void onClick(Player.Hand hand) {
                GuiNewState.open(MinecraftClient.getPlayer(), signal, statesTextField.getText());
            }
        };

        String states = String.join("\n", signal.getSignals().get("default").getStates().keySet());
        GUIHelpers.drawCenteredString(states, GUIHelpers.getScreenWidth() / 2, GUIHelpers.getScreenHeight() / 2, 0xffffff);

        Button confirmButton = new Button(screen, GUIHelpers.getScreenWidth() / 2, GUIHelpers.getScreenHeight() - 100, GuiText.LABEL_CREATOR_CONFIRM.toString()) {
            @Override
            public void onClick(Player.Hand hand) {
                //TODO Create zip file
            }
        };
    }

    @Override
    public void onEnterKey(IScreenBuilder builder) {
        GuiNewState.open(MinecraftClient.getPlayer(), signal, statesTextField.getText());
    }

    @Override
    public void onClose() {

    }

    @Override
    public void draw(IScreenBuilder builder) {

    }

    public static void open(Player player, ContentPackSignal signal) {
        GUI.get().open(player);
        GuiStates.signal = signal;
    }
}
