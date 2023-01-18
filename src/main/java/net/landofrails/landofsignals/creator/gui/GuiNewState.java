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

import java.util.function.Supplier;

public class GuiNewState implements IScreen {
    private static final Supplier<GuiRegistry.GUI> GUI = () -> LOSGuis.CREATOR_NEWSTATE;
    private static final int MIN_LENGTH = 3;

    private static ContentPackZipHandler zipHandler;
    private static ContentPackSignal signal;
    private static String signalId;
    private static String groupId;

    private TextField stateIdTextField;
    private TextField stateNameTextField;

    private Button createButton;

    @Override
    public void init(IScreenBuilder screen) {
        if (stateIdTextField != null)
            stateIdTextField.setVisible(false);
        if (stateNameTextField != null)
            stateNameTextField.setVisible(false);

        stateIdTextField = new TextField(screen, -100, -20, 200, 20);
        stateNameTextField = new TextField(screen, -100, 20, 200, 20);

        createButton = new Button(screen, -100, 60, 200, 20, "Create state") {
            @Override
            public void onClick(Player.Hand hand) {

                zipHandler.createState(signalId, groupId, stateIdTextField.getText(), stateNameTextField.getText());
                screen.close();
            }
        };
        createButton.setEnabled(false);
    }

    @Override
    public void draw(IScreenBuilder builder) {
        builder.drawCenteredString("State id", 0, -30, 0xFFFFFF);
        builder.drawCenteredString("State name", 0, 10, 0xFFFFFF);

        boolean longEnough = isIdLongEnough() && isNameLongEnough();
        boolean unique = isIdUnique() && isNameUnique();

        createButton.setEnabled(longEnough && unique);
    }

    @Override
    public void onEnterKey(IScreenBuilder builder) {
        if (!isNameLongEnough()) {
            stateNameTextField.setFocused(true);
            stateIdTextField.setFocused(false);
        } else if (!isIdLongEnough()) {
            stateIdTextField.setFocused(true);
            stateNameTextField.setFocused(false);
        } else {
            stateIdTextField.setFocused(false);
            stateNameTextField.setFocused(false);
        }
    }

    @Override
    public void onClose() {
        GuiStateOverview.open(zipHandler, signalId, groupId);
    }

    public static void open(ContentPackZipHandler zipHandler, String signalId, String groupId) {
        GuiNewState.zipHandler = zipHandler;
        GuiNewState.signal = zipHandler.getSignal(signalId).orElseThrow(() -> new RuntimeException("God damn it"));
        GuiNewState.signalId = signalId;
        GuiNewState.groupId = groupId;
        GUI.get().open(MinecraftClient.getPlayer());
    }

    private boolean isIdLongEnough(String... inputId) {
        String id = inputId.length > 0 ? inputId[0] : stateIdTextField.getText();
        return id.length() >= MIN_LENGTH;
    }

    private boolean isNameLongEnough(String... nameId) {
        String name = nameId.length > 0 ? nameId[0] : stateNameTextField.getText();
        return name.length() >= MIN_LENGTH;
    }

    private boolean isIdUnique(String... inputId) {
        String id = inputId.length > 0 ? inputId[0] : stateIdTextField.getText();
        return !signal.getSignals().get(groupId).getStates().containsKey(id);
    }

    private boolean isNameUnique(String... nameId) {
        String name = nameId.length > 0 ? nameId[0] : stateNameTextField.getText();
        return signal.getSignals().get(groupId).getStates().values().stream().noneMatch(state -> state.getSignalName().equalsIgnoreCase(name));
    }

}
