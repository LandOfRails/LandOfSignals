package net.landofrails.landofsignals.creator.gui;

import cam72cam.mod.entity.Player;
import cam72cam.mod.gui.GuiRegistry;
import cam72cam.mod.gui.screen.Button;
import cam72cam.mod.gui.screen.IScreen;
import cam72cam.mod.gui.screen.IScreenBuilder;
import cam72cam.mod.gui.screen.TextField;
import net.landofrails.api.contentpacks.v2.signal.ContentPackSignal;
import net.landofrails.landofsignals.LOSGuis;
import net.landofrails.landofsignals.gui.GuiText;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.io.File;
import java.text.MessageFormat;
import java.util.function.Supplier;

public class GuiNewState implements IScreen {
    private static final Supplier<GuiRegistry.GUI> GUI = () -> LOSGuis.CREATOR_NEWSTATE;

    private static ContentPackSignal signal;
    private static String stateId;

    private TextField stateNameTextField;
    private TextField stateTextureTextField;

    @Override
    public void init(IScreenBuilder screen) {

        // Wir wollen einen Statenamen (Signalname)
        stateNameTextField = new TextField(screen, 0 - 100, -24 + 1 * 22, 200, 20);

        // Wir wollen die OBJ
        new Button(screen, 0 - 100, -24 + 2 * 22, 200, 20, GuiText.LABEL_CREATOR_LOADOBJ.toString()) {
            @Override
            public void onClick(Player.Hand hand) {
                JFileChooser chooser = new JFileChooser();
                chooser.setMultiSelectionEnabled(true);
                chooser.setFileFilter(new OBJFileFilter());
                int returnVal = chooser.showOpenDialog(null);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File[] files = chooser.getSelectedFiles();
                    for (File file : files)
                        System.out.println(MessageFormat.format("{0} - Dir: {1}, File: {2}", file.getName(), file.isDirectory(), file.isFile()));
                }
            }
        };

        // Wir wollen die Texturen
        stateTextureTextField = new TextField(screen, 0 - 100, -24 + 5 * 22, 200, 20);

        new Button(screen, 0 - 100, -24 + 7 * 22, 200, 20, GuiText.LABEL_CREATOR_CONFIRM.toString()) {
            @Override
            public void onClick(Player.Hand hand) {

            }
        };

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
        builder.drawCenteredString(GuiText.LABEL_CREATOR_TEXTURE.toString(), 0, -24 + 4 * 22 + 10, 0xffffff);
        builder.drawCenteredString(GuiText.LABEL_CREATOR_STATENAME.toString(), 0, -24 + 0 * 22 + 10, 0xffffff);
    }

    public static void open(Player player, ContentPackSignal signal, String stateId) {
        GUI.get().open(player);
        GuiNewState.signal = signal;
        GuiNewState.stateId = stateId;
    }

    private static class OBJFileFilter extends FileFilter {

        @Override
        public boolean accept(final File file) {
            if (file.isDirectory())
                return true;

            final String name = file.getName();
            return name.endsWith(".obj") || name.endsWith(".mtl") || name.endsWith(".png") || name.endsWith(".jpg") || name.endsWith(".jpeg");
        }

        @Override
        public String getDescription() {
            return OBJFileFilter.class.getName();
        }
    }
}
