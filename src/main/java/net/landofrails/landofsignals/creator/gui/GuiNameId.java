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
import net.landofrails.api.contentpacks.v2.signal.ContentPackSignal;
import net.landofrails.api.contentpacks.v2.signal.ContentPackSignalGroup;
import net.landofrails.landofsignals.LOSGuis;
import net.landofrails.landofsignals.creator.utils.ContentPackZipHandler;
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
        nameTextField = new TextField(screen, 0 - 100, -24 + 1 * 22, 200, 20);
        idTextField = new TextField(screen, 0 - 100, -24 + 3 * 22, 200, 20);

        new Button(screen, 0 - 100, -24 + 4 * 22, 200, 20, GuiText.LABEL_CREATOR_CONFIRM.toString()) {
            @Override
            public void onClick(Player.Hand hand) {
                String idText = idTextField.getText();
                String nameText = nameTextField.getText();
                if (idText.length() < 3 || nameText.length() < 3) {
                    MinecraftClient.getPlayer().sendMessage(PlayerMessage.direct("id and name need to be atleast 3 characters"));
                    return;
                }

                if (entryType == EntryType.BLOCKSIGNAL) {
                    ContentPackSignal signal = getGenericContentPackSignal(idTextField.getText(), nameTextField.getText());
                    ContentPackZipHandler.getInstanceOrCreate(nameTextField.getText());
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
        builder.drawCenteredString(GuiText.LABEL_CREATOR_NAME.toString(), 0, -24 + 0 * 22 + 10, 0xffffff);
        builder.drawCenteredString(GuiText.LABEL_CREATOR_ID.toString(), 0, -24 + 2 * 22 + 10, 0xffffff);
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
