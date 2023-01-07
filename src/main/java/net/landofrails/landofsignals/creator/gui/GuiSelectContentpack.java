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

import java.util.Optional;
import java.util.function.Supplier;

public class GuiSelectContentpack implements IScreen {

    private static final Supplier<GuiRegistry.GUI> GUI = () -> LOSGuis.CREATOR_SELECT_CONTENTPACK;

    private static EntryType entryType;

    private TextField packIdTextField;
    private TextField packNameTextField;
    private Button confirmButton;

    @Override
    public void init(IScreenBuilder screen) {
        if (packIdTextField != null)
            packIdTextField.setVisible(false);
        if (packNameTextField != null)
            packNameTextField.setVisible(false);

        packIdTextField = new TextField(screen, -100, -24 + 3 * 22, 200, 20);
        packNameTextField = new TextField(screen, -100, -24 + 22, 200, 20);

        packIdTextField.setValidator(this::validateId);
        packNameTextField.setValidator(this::validateName);
        screen.drawCenteredString(GuiText.LABEL_CREATOR_NAME.toString(), 0, -24 + 10, 0xffffff);

        confirmButton = new Button(screen, -100, -24 + 4 * 22, 200, 20, GuiText.LABEL_CREATOR_CONFIRM.toString()) {
            @Override
            public void onClick(Player.Hand hand) {
                Optional<ContentPackZipHandler> cpzh = ContentPackZipHandler.getInstanceOrCreate(packNameTextField.getText(), packIdTextField.getText());
                cpzh.ifPresent(contentPackZipHandler -> GuiNameId.open(MinecraftClient.getPlayer(), contentPackZipHandler, entryType));
            }
        };
        confirmButton.setEnabled(false);

    }

    @Override
    public void draw(IScreenBuilder builder) {

        boolean confirmable = validateId() && validateName();
        confirmButton.setEnabled(confirmable);
    }

    @Override
    public void onEnterKey(IScreenBuilder builder) {

    }

    @Override
    public void onClose() {

    }

    private boolean validateId(String packId) {
        if (ContentPackZipHandler.getInstance(packId).isPresent()) {
            MinecraftClient.getPlayer().sendMessage(PlayerMessage.direct("Found contentpack (id)!"));
        }
        return true;
    }

    private boolean validateId() {
        String packId = packIdTextField.getText();
        if (ContentPackZipHandler.getInstance(packId).isPresent()) {
            return false;
        }
        return packId.length() >= 3;
    }

    private boolean validateName(String packName) {
        if (ContentPackZipHandler.contentPackFileExists(packName)) {
            MinecraftClient.getPlayer().sendMessage(PlayerMessage.direct("Found contentpack (name)!"));
        }
        return true;
    }

    private boolean validateName() {
        String packName = packNameTextField.getText();
        if (ContentPackZipHandler.contentPackFileExists(packName)) {
            return false;
        }
        return packName.length() >= 3;
    }

    public static void open(Player player, EntryType entryType) {
        GuiSelectContentpack.entryType = entryType;
        GUI.get().open(player);
    }

}
