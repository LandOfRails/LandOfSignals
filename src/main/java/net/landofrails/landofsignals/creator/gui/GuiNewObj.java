package net.landofrails.landofsignals.creator.gui;

import cam72cam.mod.MinecraftClient;
import cam72cam.mod.gui.GuiRegistry;
import cam72cam.mod.gui.screen.Button;
import cam72cam.mod.gui.screen.IScreen;
import cam72cam.mod.gui.screen.IScreenBuilder;
import cam72cam.mod.gui.screen.Slider;
import net.landofrails.landofsignals.LOSGuis;
import net.landofrails.landofsignals.creator.utils.ContentPackZipHandler;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class GuiNewObj implements IScreen {

    private static final Supplier<GuiRegistry.GUI> GUI = () -> LOSGuis.CREATOR_NEW_OBJ;
    private static ContentPackZipHandler contentPackZipHandler;
    private static String signalId;
    private static String groupId;
    private static String stateId;

    private Slider slider;
    private Button[] buttons = new Button[6];
    private Consumer<Integer>[] actions = new Consumer[6];

    private String[] entries;
    private int entriesIndex = 0;

    @Override
    public void init(IScreenBuilder screen) {
        slider = new Slider(screen, -75, 120, "Index: ", 0, Math.max(0, entries.length - 5d), entriesIndex, false) {
            @Override
            public void onSlider() {
                entriesIndex = slider.getValueInt();
                updateButtons();
            }
        };
        slider.setEnabled(entries.length > 5);
    }

    @Override
    public void draw(IScreenBuilder builder) {
        builder.drawCenteredString("Select new obj...", 0, -40, 0xFFFFFF);

    }

    @Override
    public void onEnterKey(IScreenBuilder builder) {

    }

    @Override
    public void onClose() {
        GuiModifyState.open(contentPackZipHandler, signalId, groupId, stateId);
    }

    private void updateButtons() {
        for (int i = 0; i < 6; i++) {
            buttons[i].setText(getTextForIndex(i));
            buttons[i].setEnabled(shouldBeEnabled(i));
        }
    }

    private String getTextForIndex(int indexOffset) {
        int index = entriesIndex + indexOffset;
        if (entries.length > index) {
            return entries[index];
        } else if (entries.length == index) {
            return "Create new model..";
        } else {
            return "";
        }
    }

    private boolean shouldBeEnabled(int indexOffset) {
        int index = entriesIndex + indexOffset;
        return entries.length >= index && contentPackZipHandler.containsOBJ(signalId);
    }

    public static void open(ContentPackZipHandler contentPackZipHandler, String signalId, String groupId, String stateId) {
        GuiNewObj.contentPackZipHandler = contentPackZipHandler;
        GuiNewObj.signalId = signalId;
        GuiNewObj.groupId = groupId;
        GuiNewObj.stateId = stateId;
        GUI.get().open(MinecraftClient.getPlayer());
    }

}
