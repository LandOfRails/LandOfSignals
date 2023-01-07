package net.landofrails.landofsignals.creator.gui;

import cam72cam.mod.MinecraftClient;
import cam72cam.mod.entity.Player;
import cam72cam.mod.gui.GuiRegistry;
import cam72cam.mod.gui.screen.Button;
import cam72cam.mod.gui.screen.IScreen;
import cam72cam.mod.gui.screen.IScreenBuilder;
import cam72cam.mod.gui.screen.TextField;
import cam72cam.mod.text.PlayerMessage;
import net.landofrails.api.contentpacks.v2.EntryType;
import net.landofrails.landofsignals.LOSGuis;
import net.landofrails.landofsignals.creator.utils.ContentPackZipHandler;
import net.landofrails.landofsignals.gui.GuiText;

import java.util.function.Supplier;

public class GuiNameId implements IScreen {

    private static final Supplier<GuiRegistry.GUI> GUI = () -> LOSGuis.CREATOR_NAME_ID;

    private static ContentPackZipHandler zipHandler;
    private static EntryType entryType;

    private TextField signalNameTextField;
    private TextField signalIdTextField;

    @Override
    public void init(IScreenBuilder screen) {
        signalNameTextField = new TextField(screen, 0 - 100, -24 + 1 * 22, 200, 20);
        signalIdTextField = new TextField(screen, 0 - 100, -24 + 3 * 22, 200, 20);

        new Button(screen, 0 - 100, -24 + 4 * 22, 200, 20, GuiText.LABEL_CREATOR_CONFIRM.toString()) {
            @Override
            public void onClick(Player.Hand hand) {
                String signalIdText = signalIdTextField.getText();
                String signalNameText = signalNameTextField.getText();
                if (signalIdText.length() < 3 || signalNameText.length() < 3) {
                    MinecraftClient.getPlayer().sendMessage(PlayerMessage.direct("id and name need to be atleast 3 characters"));
                    return;
                }

                if (entryType == EntryType.BLOCKSIGNAL) {

                    // Create signal

                    GuiStates.open(MinecraftClient.getPlayer(), zipHandler);
                }
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
        builder.drawCenteredString(GuiText.LABEL_CREATOR_NAME.toString(), 0, -24 + 10, 0xffffff);
        builder.drawCenteredString(GuiText.LABEL_CREATOR_ID.toString(), 0, -24 + 2 * 22 + 10, 0xffffff);
    }

    public static void open(Player player, ContentPackZipHandler zipHandler, EntryType entryType) {
        GuiNameId.zipHandler = zipHandler;
        GuiNameId.entryType = entryType;
        GUI.get().open(player);
    }

}
