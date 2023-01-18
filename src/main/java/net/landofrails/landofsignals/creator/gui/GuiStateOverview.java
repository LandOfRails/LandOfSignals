package net.landofrails.landofsignals.creator.gui;

import cam72cam.mod.MinecraftClient;
import cam72cam.mod.entity.Player;
import cam72cam.mod.gui.GuiRegistry;
import cam72cam.mod.gui.screen.Button;
import cam72cam.mod.gui.screen.IScreen;
import cam72cam.mod.gui.screen.IScreenBuilder;
import cam72cam.mod.gui.screen.Slider;
import cam72cam.mod.text.PlayerMessage;
import net.landofrails.api.contentpacks.v2.signal.ContentPackSignal;
import net.landofrails.api.contentpacks.v2.signal.ContentPackSignalState;
import net.landofrails.landofsignals.LOSGuis;
import net.landofrails.landofsignals.creator.utils.ContentPackZipHandler;

import java.text.MessageFormat;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class GuiStateOverview implements IScreen {
    private static final Supplier<GuiRegistry.GUI> GUI = () -> LOSGuis.CREATOR_STATE_OVERVIEW;

    private static ContentPackZipHandler contentPackZipHandler;
    private static String signalId;
    private static String groupId;
    private static String[][] states;

    private Button[] upButtons = new Button[6];
    private Button[] buttons = new Button[6];
    private Button[] downButtons = new Button[6];
    private Consumer<Integer>[] actions = new Consumer[6];

    private int entriesIndex = 0;

    private Slider slider;

    @Override
    public void init(IScreenBuilder screen) {
        slider = new Slider(screen, -75, 120, "Index: ", 0, Math.max(0, states.length - 5d), entriesIndex, false) {
            @Override
            public void onSlider() {
                entriesIndex = slider.getValueInt();
                updateButtons();
            }
        };
        slider.setEnabled(states.length > 5);

        for (int i = 0; i < 6; i++) {
            int finalI = i;
            upButtons[i] = new Button(screen, -120, finalI * 20, 20, 20, "^") {
                @Override
                public void onClick(Player.Hand hand) {
                    int calcIndex = entriesIndex + finalI;
                    String[] upState = states[calcIndex];
                    states[calcIndex] = states[calcIndex - 1];
                    states[calcIndex - 1] = upState;
                    updateSignal();
                    updateButtons();
                }
            };
            buttons[i] = new Button(screen, -100, finalI * 20, 200, 20, "") {
                @Override
                public void onClick(Player.Hand hand) {
                    actions[finalI].accept(finalI);
                }
            };
            downButtons[i] = new Button(screen, 100, finalI * 20, 20, 20, "v") {
                @Override
                public void onClick(Player.Hand hand) {
                    int calcIndex = entriesIndex + finalI;
                    String[] downState = states[calcIndex];
                    states[calcIndex] = states[calcIndex + 1];
                    states[calcIndex + 1] = downState;
                    updateSignal();
                    updateButtons();
                }
            };
            actions[i] = indexOffset -> {
                int index = entriesIndex + indexOffset;
                if (states.length > index) {
                    MinecraftClient.getPlayer().sendMessage(PlayerMessage.direct("Not implemented yet."));
                } else if (states.length == index) {
                    GuiNewState.open(contentPackZipHandler, signalId, groupId);
                } else {
                    MinecraftClient.getPlayer().sendMessage(PlayerMessage.direct("How???"));
                }
            };
        }

        updateButtons();

    }


    @Override
    public void draw(IScreenBuilder builder) {
        // Nothing to draw
        builder.drawCenteredString("Select state...", 0, -20, 0xFFFFFF);
    }

    @Override
    public void onEnterKey(IScreenBuilder builder) {
        // Nothing to do
    }

    @Override
    public void onClose() {
        // Nothing to do
    }

    private void updateButtons() {
        for (int i = 0; i < 6; i++) {
            buttons[i].setText(getTextForIndex(i));
            buttons[i].setEnabled(shouldBeEnabled(i));
            upButtons[i].setEnabled(shouldBeEnabled(true, i));
            downButtons[i].setEnabled(shouldBeEnabled(false, i));
        }
    }

    private String getTextForIndex(int indexOffset) {
        int index = entriesIndex + indexOffset;
        if (states.length > index) {
            String stateId = states[index][0];
            String stateName = states[index][1];
            return MessageFormat.format("{1} (ID: {0})", stateId, stateName);
        } else if (states.length == index) {
            return "Create new state..";
        } else {
            return "";
        }
    }

    private boolean shouldBeEnabled(int indexOffset) {
        int index = entriesIndex + indexOffset;
        return states.length >= index;
    }

    private boolean shouldBeEnabled(boolean upButton, int indexOffset) {
        int index = entriesIndex + indexOffset;
        if (upButton) {
            return index > 0 && index < states.length;
        } else {
            return index + 1 < states.length;
        }
    }

    private void updateSignal() {
        ContentPackSignal signal = contentPackZipHandler.getSignal(signalId).orElseThrow(() -> new RuntimeException("error"));
        Map<String, ContentPackSignalState> oldStates = signal.getSignals().get(groupId).getStates();
        LinkedHashMap<String, ContentPackSignalState> newStates = new LinkedHashMap<>();
        for (String[] state : states) {
            String key = state[0];
            newStates.put(key, oldStates.get(key));
        }
        signal.getSignals().get(groupId).setStates(newStates);
        contentPackZipHandler.writeSignal(signalId, signal);
    }

    public static void open(ContentPackZipHandler contentPackZipHandler, String signalId, String groupId) {
        GuiStateOverview.contentPackZipHandler = contentPackZipHandler;
        GuiStateOverview.signalId = signalId;
        GuiStateOverview.groupId = groupId;
        ContentPackSignal signal = contentPackZipHandler.getSignal(signalId).orElseThrow(() -> new RuntimeException("error"));
        Map<String, ContentPackSignalState> states = signal.getSignals().get(groupId).getStates();
        GuiStateOverview.states = new String[states != null ? states.size() : 0][2];
        if (states != null && !states.isEmpty()) {
            for (int i = 0; i < states.size(); i++) {
                GuiStateOverview.states[i][0] = states.keySet().toArray(new String[0])[i];
                GuiStateOverview.states[i][1] = states.values().toArray(new ContentPackSignalState[0])[i].getSignalName();
            }
        }
        GUI.get().open(MinecraftClient.getPlayer());
    }
}
