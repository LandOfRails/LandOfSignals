package net.landofrails.landofsignals.creator.gui;

import cam72cam.mod.MinecraftClient;
import cam72cam.mod.entity.Player;
import cam72cam.mod.gui.GuiRegistry;
import cam72cam.mod.gui.helpers.GUIHelpers;
import cam72cam.mod.gui.screen.Button;
import cam72cam.mod.gui.screen.IScreen;
import cam72cam.mod.gui.screen.IScreenBuilder;
import cam72cam.mod.gui.screen.TextField;
import net.landofrails.api.contentpacks.v2.EntryType;
import net.landofrails.api.contentpacks.v2.signal.ContentPackSignal;
import net.landofrails.api.contentpacks.v2.signal.ContentPackSignalGroup;
import net.landofrails.landofsignals.LOSGuis;
import net.landofrails.landofsignals.gui.GuiText;

import java.util.Collections;
import java.util.function.Supplier;

public class GuiNameId implements IScreen {

    private static final Supplier<GuiRegistry.GUI> GUI = () -> LOSGuis.CREATOR_NAME_ID;

    private static EntryType entryType;

    private TextField nameTextField;
    private TextField idTextField;

    @Override
    public void init(IScreenBuilder screen) {
        nameTextField = new TextField(screen, GUIHelpers.getScreenWidth() / 2, GUIHelpers.getScreenHeight() / 2 - 100, 100, 20);
        idTextField = new TextField(screen, GUIHelpers.getScreenWidth() / 2, GUIHelpers.getScreenHeight() / 2, 100, 20);

        screen.drawCenteredString(GuiText.LABEL_CREATOR_NAME.toString(), GUIHelpers.getScreenWidth() / 2, GUIHelpers.getScreenHeight() / 2 - 120, 0xffffff);
        screen.drawCenteredString(GuiText.LABEL_CREATOR_ID.toString(), GUIHelpers.getScreenWidth() / 2, GUIHelpers.getScreenHeight() / 2 - 20, 0xffffff);

        Button confirmButton = new Button(screen, GUIHelpers.getScreenWidth() / 2, GUIHelpers.getScreenHeight() - 100, GuiText.LABEL_CREATOR_CONFIRM.toString()) {
            @Override
            public void onClick(Player.Hand hand) {
                if (entryType == EntryType.BLOCKSIGNAL) {
                    ContentPackSignal signal = getGenericContentPackSignal(idTextField.getText(), nameTextField.getText());
                    GuiStates.open(MinecraftClient.getPlayer(), signal);
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

    }

    private ContentPackSignal getGenericContentPackSignal(String id, String name) {
        ContentPackSignal signal = new ContentPackSignal();

        signal.setId(id);
        signal.setName(name);

        ContentPackSignalGroup group = new ContentPackSignalGroup();
        group.setGroupName("Default");
        signal.setSignals(Collections.singletonMap("default", group));

        return signal;
    }

    public static void open(Player player, EntryType entryType) {
        GuiNameId.entryType = entryType;
        GUI.get().open(player);
    }

}
