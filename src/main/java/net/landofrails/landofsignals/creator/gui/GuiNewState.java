package net.landofrails.landofsignals.creator.gui;

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

import javax.swing.*;
import java.util.function.Supplier;

public class GuiNewState implements IScreen {
    private static final Supplier<GuiRegistry.GUI> GUI = () -> LOSGuis.CREATOR_NEWSTATE;

    private static ContentPackSignal signal;
    private static String stateId;

    private TextField stateNameTextField;
    private TextField stateTextureTextField;

    @Override
    public void init(IScreenBuilder screen) {

        final int width = GUIHelpers.getScreenWidth();
        final int height = GUIHelpers.getScreenHeight();

        // Wir wollen einen Statenamen (Signalname)
        stateNameTextField = new TextField(screen, width / 2, height / 2 - 100, 100, 20);

        // Wir wollen die OBJ
        Button loadOBJButton = new Button(screen, width / 2, height / 2, GuiText.LABEL_CREATOR_LOADOBJ.toString()) {
            @Override
            public void onClick(Player.Hand hand) {
                JFileChooser chooser = new JFileChooser();
                chooser.setMultiSelectionEnabled(true);
                int returnVal = chooser.showOpenDialog(null);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    chooser.getSelectedFiles();
                }
            }
        };

        stateTextureTextField = new TextField(screen, width / 2, height / 2 - 200, 100, 20);

        // Wir wollen die Texturen
        // Wir wollen die OBJ-Gruppen ?
        // Wir wollen die Translation ?
        // Wir wollen die Rotation ?
        // Wir wollen das Scaling ?

    }

    @Override
    public void onEnterKey(IScreenBuilder builder) {
        // signal.getSignals().get("default").getStates().put(statesTextField.getText(), )
    }

    @Override
    public void onClose() {

    }

    @Override
    public void draw(IScreenBuilder builder) {

    }

    public static void open(Player player, ContentPackSignal signal, String stateId) {
        GUI.get().open(player);
        GuiNewState.signal = signal;
        GuiNewState.stateId = stateId;
    }
}
