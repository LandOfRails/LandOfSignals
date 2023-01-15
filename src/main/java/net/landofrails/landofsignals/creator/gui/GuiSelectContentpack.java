package net.landofrails.landofsignals.creator.gui;

import cam72cam.mod.MinecraftClient;
import cam72cam.mod.entity.Player;
import cam72cam.mod.gui.GuiRegistry;
import cam72cam.mod.gui.screen.Button;
import cam72cam.mod.gui.screen.IScreen;
import cam72cam.mod.gui.screen.IScreenBuilder;
import cam72cam.mod.gui.screen.TextField;
import cam72cam.mod.text.PlayerMessage;
import net.landofrails.landofsignals.LOSGuis;
import net.landofrails.landofsignals.creator.utils.ContentPackZipHandler;

import java.util.Optional;
import java.util.function.Supplier;

public class GuiSelectContentpack implements IScreen {

    private static final Supplier<GuiRegistry.GUI> GUI = () -> LOSGuis.CREATOR_SELECT_CONTENTPACK;
    private static final int MIN_LENGTH = 3;

    private TextField packIdTextField;
    private TextField packNameTextField;

    private Button createButton;
    private Button modifyButton;
    private Button validateButton;

    @Override
    public void init(IScreenBuilder screen) {

        if (packIdTextField != null)
            packIdTextField.setVisible(false);
        if (packNameTextField != null)
            packNameTextField.setVisible(false);

        packIdTextField = new TextField(screen, -100, -20, 200, 20);
        packNameTextField = new TextField(screen, -100, 20, 200, 20);

        createButton = new Button(screen, -100, 60, 200, 20, "Create Contentpack") {
            @Override
            public void onClick(Player.Hand hand) {
                Optional<ContentPackZipHandler> cpzh = ContentPackZipHandler.getInstanceOrCreate(packNameTextField.getText(), packIdTextField.getText());
                cpzh.ifPresent(GuiSelectType::open);
            }
        };
        createButton.setEnabled(false);
        modifyButton = new Button(screen, -100, 80, 200, 20, "Modify Contentpack") {
            @Override
            public void onClick(Player.Hand hand) {
                MinecraftClient.getPlayer().sendMessage(PlayerMessage.direct("Not implemented yet."));
            }
        };
        modifyButton.setEnabled(false);
        validateButton = new Button(screen, -100, 100, 200, 20, "Validate Contentpack") {
            @Override
            public void onClick(Player.Hand hand) {
                MinecraftClient.getPlayer().sendMessage(PlayerMessage.direct("Not implemented yet."));
            }
        };
        validateButton.setEnabled(false);
    }

    @Override
    public void draw(IScreenBuilder builder) {

        builder.drawCenteredString("Contentpack id", 0, -30, 0xFFFFFF);
        builder.drawCenteredString("Contentpack name", 0, 10, 0xFFFFFF);

        boolean longEnough = isIdLongEnough() && isNameLongEnough();
        boolean unique = isIdUnique() && isNameUnique();

        createButton.setEnabled(longEnough && unique);
        modifyButton.setEnabled(longEnough && !unique);
        validateButton.setEnabled(longEnough && !unique);
    }

    @Override
    public void onEnterKey(IScreenBuilder builder) {

        if (!isNameLongEnough()) {
            packNameTextField.setFocused(true);
            packIdTextField.setFocused(false);
        } else if (!isIdLongEnough()) {
            packIdTextField.setFocused(true);
            packNameTextField.setFocused(false);
        } else {
            packIdTextField.setFocused(false);
            packNameTextField.setFocused(false);
        }
    }

    @Override
    public void onClose() {
        // Nothing to do
    }

    private boolean isIdLongEnough(String... inputId) {
        String id = inputId.length > 0 ? inputId[0] : packIdTextField.getText();
        return id.length() >= MIN_LENGTH;
    }

    private boolean isNameLongEnough(String... nameId) {
        String name = nameId.length > 0 ? nameId[0] : packNameTextField.getText();
        return name.length() >= MIN_LENGTH;
    }

    private boolean isIdUnique(String... inputId) {
        String id = inputId.length > 0 ? inputId[0] : packIdTextField.getText();
        return !ContentPackZipHandler.getInstance(id).isPresent();
    }

    private boolean isNameUnique(String... nameId) {
        String name = nameId.length > 0 ? nameId[0] : packNameTextField.getText();
        return !ContentPackZipHandler.contentPackFileExists(name);
    }

    public static void open() {
        GUI.get().open(MinecraftClient.getPlayer());
    }

}
