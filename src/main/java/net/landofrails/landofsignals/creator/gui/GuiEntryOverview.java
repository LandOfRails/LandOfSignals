package net.landofrails.landofsignals.creator.gui;

import cam72cam.mod.MinecraftClient;
import cam72cam.mod.entity.Player;
import cam72cam.mod.gui.GuiRegistry;
import cam72cam.mod.gui.screen.Button;
import cam72cam.mod.gui.screen.IScreen;
import cam72cam.mod.gui.screen.IScreenBuilder;
import cam72cam.mod.gui.screen.Slider;
import cam72cam.mod.text.PlayerMessage;
import net.landofrails.api.contentpacks.v2.EntryType;
import net.landofrails.landofsignals.LOSGuis;
import net.landofrails.landofsignals.creator.utils.ContentPackZipHandler;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class GuiEntryOverview implements IScreen {

    private static final Supplier<GuiRegistry.GUI> GUI = () -> LOSGuis.CREATOR_ENTRY_OVERVIEW;

    private static ContentPackZipHandler contentPackZipHandler;
    private static EntryType entryType;
    private static Map<String, String> entries;

    private Button[] buttons = new Button[6];
    private Consumer<Integer>[] actions = new Consumer[6];

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
            buttons[i] = new Button(screen, -100, finalI * 20, 200, 20, "") {
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
                    GuiNameId.open(contentPackZipHandler, entryType);
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
        builder.drawCenteredString("Select entry...", 0, -20, 0xFFFFFF);
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
        if (entries.size() > index) {
            return entries.keySet().toArray(new String[0])[index];
        } else if (entries.size() == index) {
            return "Create new entry..";
        } else {
            return "";
        }
    }

    private boolean shouldBeEnabled(int indexOffset) {
        int index = entriesIndex + indexOffset;
        return entries.size() == index;
    }

    public static void open(ContentPackZipHandler contentPackZipHandler, EntryType entryType) {
        GuiEntryOverview.contentPackZipHandler = contentPackZipHandler;
        GuiEntryOverview.entryType = entryType;
        GuiEntryOverview.entries = contentPackZipHandler.listSignals();
        GUI.get().open(MinecraftClient.getPlayer());
    }

}
