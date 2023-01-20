package net.landofrails.landofsignals.creator.gui;

import cam72cam.mod.MinecraftClient;
import cam72cam.mod.entity.Player;
import cam72cam.mod.gui.GuiRegistry;
import cam72cam.mod.gui.screen.Button;
import cam72cam.mod.gui.screen.IScreen;
import cam72cam.mod.gui.screen.IScreenBuilder;
import cam72cam.mod.gui.screen.Slider;
import cam72cam.mod.text.PlayerMessage;
import net.landofrails.api.contentpacks.v2.parent.ContentPackModel;
import net.landofrails.api.contentpacks.v2.signal.ContentPackSignal;
import net.landofrails.api.contentpacks.v2.signal.ContentPackSignalState;
import net.landofrails.landofsignals.LOSGuis;
import net.landofrails.landofsignals.creator.utils.ContentPackZipHandler;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class GuiModifyState implements IScreen {

    private static final Supplier<GuiRegistry.GUI> GUI = () -> LOSGuis.CREATOR_MODIFY_STATE;

    private static ContentPackZipHandler contentPackZipHandler;
    private static String signalId;
    private static String groupId;
    private static String[] stateInfo;

    private static Map<String, ContentPackModel[]> entries;

    private Button[] buttons = new Button[6];
    private Consumer<Integer>[] actions = new Consumer[6];
    private Button[] deleteButtons = new Button[6];
    private Consumer<Integer>[] deleteActions = new Consumer[6];
    private Button fileButton;

    private int entriesIndex = 0;

    private Slider slider;

    @Override
    public void init(IScreenBuilder screen) {
        slider = new Slider(screen, -75, 120, "Index: ", 0, Math.max(0, entries.size() - 5d), entriesIndex, false) {
            @Override
            public void onSlider() {
                entriesIndex = slider.getValueInt();
                updateButtons();
            }
        };
        slider.setEnabled(entries.size() > 5);

        for (int i = 0; i < 6; i++) {
            int finalI = i;
            buttons[i] = new Button(screen, -100, (finalI * 20) - 20, 200, 20, "") {
                @Override
                public void onClick(Player.Hand hand) {
                    actions[finalI].accept(finalI);
                }
            };
            actions[i] = indexOffset -> {
                int index = entriesIndex + indexOffset;
                if (entries.size() > index) {
                    MinecraftClient.getPlayer().sendMessage(PlayerMessage.direct("Not implemented yet."));
                } else if (entries.size() == index) {
                    // Create new OBJ
                    MinecraftClient.getPlayer().sendMessage(PlayerMessage.direct("Not implemented yet."));
                } else {
                    MinecraftClient.getPlayer().sendMessage(PlayerMessage.direct("How???"));
                }
            };
            deleteButtons[i] = new Button(screen, 100, (finalI * 20) - 20, 20, 20, "x") {
                @Override
                public void onClick(Player.Hand hand) {
                    deleteActions[finalI].accept(finalI);
                }
            };
            deleteActions[i] = indexOffset -> {
                int index = entriesIndex + indexOffset;
                MinecraftClient.getPlayer().sendMessage(PlayerMessage.direct("Not implemented yet."));
            };
        }

        updateButtons();

        fileButton = new Button(screen, -100, 120, 200, 20, "Edit files") {
            @Override
            public void onClick(Player.Hand hand) {
                // Open file explorer
                contentPackZipHandler.openSignalFolder(signalId);
            }
        };
    }

    @Override
    public void draw(IScreenBuilder builder) {
        builder.drawCenteredString(MessageFormat.format("{1} (ID: {0})", stateInfo[0], stateInfo[1]), 0, -40, 0xFFFFFF);
    }

    @Override
    public void onEnterKey(IScreenBuilder builder) {
        // Nothin' to do
    }

    @Override
    public void onClose() {
        // Nothin' to do
    }

    private void updateButtons() {
        for (int i = 0; i < 6; i++) {
            buttons[i].setText(getTextForIndex(i));
            buttons[i].setEnabled(shouldBeEnabled(i));
            deleteButtons[i].setEnabled(shouldBeEnabled(i + 1));
        }
    }

    private String getTextForIndex(int indexOffset) {
        int index = entriesIndex + indexOffset;
        if (entries.size() > index) {
            return entries.keySet().toArray(new String[0])[index];
        } else if (entries.size() == index) {
            return "Create new model..";
        } else {
            return "";
        }
    }

    private boolean shouldBeEnabled(int indexOffset) {
        int index = entriesIndex + indexOffset;
        return entries.size() >= index && contentPackZipHandler.containsOBJ(signalId);
    }

    public static void open(ContentPackZipHandler contentPackZipHandler, String signalId, String groupId, String stateId) {
        GuiModifyState.contentPackZipHandler = contentPackZipHandler;
        GuiModifyState.signalId = signalId;
        GuiModifyState.groupId = groupId;
        ContentPackSignal signal = contentPackZipHandler.getSignal(signalId).orElseThrow(() -> new RuntimeException("Oh no..."));
        ContentPackSignalState state = signal.getSignals().get(groupId).getStates().get(stateId);
        String stateName = state.getSignalName();
        GuiModifyState.stateInfo = new String[]{stateId, stateName};
        GuiModifyState.entries = state.getModels() != null ? state.getModels() : new HashMap<>();
        GUI.get().open(MinecraftClient.getPlayer());
    }

}
