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
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class GuiStateOverview implements IScreen {
    private static final Supplier<GuiRegistry.GUI> GUI = () -> LOSGuis.CREATOR_STATE_OVERVIEW;

    private static ContentPackZipHandler contentPackZipHandler;
    private static String signalId;
    private static String groupId;
    private static Map<String, String> states;

    private Button[] buttons = new Button[6];
    private Consumer<Integer>[] actions = new Consumer[6];

    private int entriesIndex = 0;

    private Slider slider;

    @Override
    public void init(IScreenBuilder screen) {
        slider = new Slider(screen, -75, 120, "Index: ", 0, Math.max(0, states.size() - 5d), entriesIndex, false) {
            @Override
            public void onSlider() {
                entriesIndex = slider.getValueInt();
                updateButtons();
            }
        };
        slider.setEnabled(states.size() > 5);

        for (int i = 0; i < 6; i++) {
            int finalI = i;
            buttons[i] = new Button(screen, -100, finalI * 20, 200, 20, "") {
                @Override
                public void onClick(Player.Hand hand) {
                    actions[finalI].accept(finalI);
                }
            };
            actions[i] = indexOffset -> {
                int index = entriesIndex + indexOffset;
                if (states.size() > index) {
                    MinecraftClient.getPlayer().sendMessage(PlayerMessage.direct("Not implemented yet."));
                } else if (states.size() == index) {
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
        }
    }

    private String getTextForIndex(int indexOffset) {
        int index = entriesIndex + indexOffset;
        if (states.size() > index) {
            String stateId = states.keySet().toArray(new String[0])[index];
            String stateName = states.get(stateId);
            return MessageFormat.format("{1} (ID: {0})", stateId, stateName);
        } else if (states.size() == index) {
            return "Create new state..";
        } else {
            return "";
        }
    }

    private boolean shouldBeEnabled(int indexOffset) {
        int index = entriesIndex + indexOffset;
        return states.size() >= index;
    }

    public static void open(ContentPackZipHandler contentPackZipHandler, String signalId, String groupId) {
        GuiStateOverview.contentPackZipHandler = contentPackZipHandler;
        GuiStateOverview.signalId = signalId;
        GuiStateOverview.groupId = groupId;
        ContentPackSignal signal = contentPackZipHandler.getSignal(signalId).orElseThrow(() -> new RuntimeException("error"));
        GuiStateOverview.states = new HashMap<>();
        Map<String, ContentPackSignalState> states = signal.getSignals().get(groupId).getStates();
        if (states != null && !states.isEmpty()) {
            states.forEach((stateId, state) -> GuiStateOverview.states.put(stateId, state.getSignalName()));
        }
        GUI.get().open(MinecraftClient.getPlayer());
    }
}
