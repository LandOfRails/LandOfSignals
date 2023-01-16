package net.landofrails.landofsignals.creator.gui;

import cam72cam.mod.MinecraftClient;
import cam72cam.mod.entity.Player;
import cam72cam.mod.gui.GuiRegistry;
import cam72cam.mod.gui.screen.Button;
import cam72cam.mod.gui.screen.IScreen;
import cam72cam.mod.gui.screen.IScreenBuilder;
import cam72cam.mod.gui.screen.TextField;
import net.landofrails.api.contentpacks.v2.signal.ContentPackSignal;
import net.landofrails.landofsignals.LOSGuis;
import net.landofrails.landofsignals.creator.utils.ContentPackZipHandler;
import net.landofrails.landofsignals.gui.GuiText;

import java.util.function.Supplier;

public class GuiStates implements IScreen {
    private static final Supplier<GuiRegistry.GUI> GUI = () -> LOSGuis.CREATOR_STATES;

    private static ContentPackZipHandler zipHandler;
    private static String signalId;
    private TextField statesTextField;

    @Override
    public void init(IScreenBuilder screen) {
        ContentPackSignal signal = zipHandler.getSignal(signalId).orElseThrow(() -> new RuntimeException("Oh oh!"));

        statesTextField = new TextField(screen, 0 - 100, -24 + 1 * 22, 200, 20);
        new Button(screen, 0 - 100, -24 + 2 * 22, 200, 20, GuiText.LABEL_CREATOR_CREATE.toString()) {
            @Override
            public void onClick(Player.Hand hand) {
                GuiNewState.open(MinecraftClient.getPlayer(), signal, statesTextField.getText());
            }
        };

        if (signal.getSignals().get("default").getStates() == null) return;
        new Button(screen, 0 - 100, -24 + 5 * 22, 200, 20, GuiText.LABEL_CREATOR_CONFIRM.toString()) {
            @Override
            public void onClick(Player.Hand hand) {
                //TODO Create final content pack zip file
            }
        };
    }

    @Override
    public void onEnterKey(IScreenBuilder builder) {
        GuiNewState.open(MinecraftClient.getPlayer(), null, statesTextField.getText());
    }

    @Override
    public void onClose() {

    }

    @Override
    public void draw(IScreenBuilder builder) {
        builder.drawCenteredString(GuiText.LABEL_CREATOR_CREATESTATE.toString(), 0, -24 + 0 * 22 + 10, 0xffffff);

        ContentPackSignal signal = zipHandler.getSignal(signalId).orElseThrow(() -> new RuntimeException("Oh oh!"));

        if (signal.getSignals().get("default").getStates() == null) return;
        String states = String.join("\n", signal.getSignals().get("default").getStates().keySet());
        builder.drawCenteredString(states, 0, -24 + 3 * 22 + 10, 0xffffff);
    }

    public static void open(ContentPackZipHandler zipHandler, String signalId) {
        GuiStates.zipHandler = zipHandler;
        GuiStates.signalId = signalId;
        GUI.get().open(MinecraftClient.getPlayer());
    }
}
