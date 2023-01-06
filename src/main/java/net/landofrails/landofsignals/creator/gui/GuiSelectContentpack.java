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

public class GuiSelectContentpack implements IScreen {

    private static final Supplier<GuiRegistry.GUI> GUI = () -> LOSGuis.CREATOR_SELECT_CONTENTPACK;

    private static EntryType entryType;

    private TextField groupNameTextField;

    @Override
    public void init(IScreenBuilder screen) {
        if (groupNameTextField != null)
            groupNameTextField.setVisible(false);

        groupNameTextField = new TextField(screen, -100, -24 + 22, 200, 20);
        groupNameTextField.setValidator(this::validator);

        screen.drawCenteredString(GuiText.LABEL_CREATOR_NAME.toString(), 0, -24 + 10, 0xffffff);

        new Button(screen, -100, -24 + 4 * 22, 200, 20, GuiText.LABEL_CREATOR_CONFIRM.toString()) {
            @Override
            public void onClick(Player.Hand hand) {

            }
        };

    }

    @Override
    public void draw(IScreenBuilder builder) {

    }

    @Override
    public void onEnterKey(IScreenBuilder builder) {

    }

    @Override
    public void onClose() {

    }

    private boolean validator(String text) {
        if (text.length() > 3 && ContentPackZipHandler.getInstance(text).isPresent()) {
            MinecraftClient.getPlayer().sendMessage(PlayerMessage.direct("Found contentpack!"));
        }
        return true;
    }

    public static void open(Player player, EntryType entryType) {
        GuiSelectContentpack.entryType = entryType;
        GUI.get().open(player);
    }

}
