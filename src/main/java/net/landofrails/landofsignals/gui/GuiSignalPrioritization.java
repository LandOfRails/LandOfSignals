package net.landofrails.landofsignals.gui;

import cam72cam.mod.MinecraftClient;
import cam72cam.mod.entity.Player;
import cam72cam.mod.gui.GuiRegistry;
import cam72cam.mod.gui.helpers.GUIHelpers;
import cam72cam.mod.gui.screen.Button;
import cam72cam.mod.gui.screen.IScreen;
import cam72cam.mod.gui.screen.IScreenBuilder;
import cam72cam.mod.gui.screen.Slider;
import cam72cam.mod.item.ItemStack;
import cam72cam.mod.math.Vec3i;
import cam72cam.mod.render.OpenGL;
import cam72cam.mod.serialization.TagCompound;
import net.landofrails.landofsignals.LOSBlocks;
import net.landofrails.landofsignals.LOSGuis;
import net.landofrails.landofsignals.LOSItems;
import net.landofrails.landofsignals.tile.TileSignalPart;
import org.lwjgl.opengl.GL11;

import java.text.MessageFormat;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class GuiSignalPrioritization implements IScreen {

    private static TileSignalPart signal;
    private static Vec3i signalPos;

    private static final Supplier<GuiRegistry.GUI> GUI = () -> LOSGuis.SIGNAL_PRIORITIZATION;

    private Button[] upButtons = new Button[6];
    private Button[] buttons = new Button[6];
    private Button[] downButtons = new Button[6];
    private Consumer<Integer>[] actions = new Consumer[6];
    private ItemStack item;

    private String[] states;
    private int itemIndex = 0;
    private int entriesIndex = 0;

    private Slider slider;

    @Override
    public void init(IScreenBuilder screen) {

        String blockId = signal.getId();

        item = new ItemStack(LOSItems.ITEM_SIGNAL_PART, 1);
        TagCompound tag = item.getTagCompound();
        tag.setString("itemId", blockId);
        item.setTagCompound(tag);

        states = LOSBlocks.BLOCK_SIGNAL_PART.getAllStates(blockId);

        slider = new Slider(screen, -175, 120, "Index: ", 0, Math.max(1, states.length - 6d), entriesIndex, false) {
            @Override
            public void onSlider() {
                entriesIndex = slider.getValueInt();
                updateButtons();
            }
        };
        slider.setEnabled(states.length > 6);

        for (int i = 0; i < 6; i++) {
            int finalI = i;

            upButtons[i] = new Button(screen, -220, finalI * 20, 20, 20, "^") {
                @Override
                public void onClick(Player.Hand hand) {
                    int calcIndex = entriesIndex + finalI;
                    String upState = states[calcIndex];
                    states[calcIndex] = states[calcIndex - 1];
                    states[calcIndex - 1] = upState;

                    if (itemIndex == calcIndex) {
                        itemIndex--;
                    } else if (itemIndex + 1 == calcIndex) {
                        itemIndex++;
                    }

                    updateButtons();
                }
            };

            buttons[i] = new Button(screen, -200, finalI * 20, 200, 20, "") {
                @Override
                public void onClick(Player.Hand hand) {
                    actions[finalI].accept(finalI);
                }
            };

            downButtons[i] = new Button(screen, 0, finalI * 20, 20, 20, "v") {
                @Override
                public void onClick(Player.Hand hand) {
                    int calcIndex = entriesIndex + finalI;
                    String downState = states[calcIndex];
                    states[calcIndex] = states[calcIndex + 1];
                    states[calcIndex + 1] = downState;

                    if (itemIndex == calcIndex) {
                        itemIndex++;
                    } else if (itemIndex - 1 == calcIndex) {
                        itemIndex--;
                    }

                    updateButtons();
                }
            };

            actions[i] = indexOffset -> {
                int index = entriesIndex + indexOffset;
                itemIndex = index;
                updateButtons();
            };
        }

        updateButtons();

        final int scale = 8;
        final TagCompound rightTag = item.getTagCompound();
        rightTag.setString("itemState", states[itemIndex]);
        item.setTagCompound(rightTag);

    }

    @Override
    public void draw(IScreenBuilder builder) {
        builder.drawCenteredString("Prioritize states...", 0, -20, 0xFFFFFF);

        builder.drawCenteredString("Low", 35, 6, 0xFFFFFF);
        builder.drawCenteredString("High", 35, 106, 0xFFFFFF);

        final int scale = 8;
        final TagCompound rightTag = item.getTagCompound();
        rightTag.setString("itemState", states[itemIndex]);
        item.setTagCompound(rightTag);

        try (final OpenGL.With ignored = OpenGL.matrix()) {
            GL11.glTranslated((double) GUIHelpers.getScreenWidth() / 2 + (double) builder.getWidth() / 6, (double) builder.getHeight() / 4, 0);
            GL11.glScaled(scale, scale, 1);
            GUIHelpers.drawItem(item, 0, 0);
        }

        // Slider and GUIHelper.drawItem() are interacting with each other. Fix background:
        // Has no effect, but makes slider work :)
        GUIHelpers.drawRect(-175, 120, 200, 20, 0x616161);
        //
        
    }

    @Override
    public void onEnterKey(IScreenBuilder builder) {
        // Nothing to do
    }

    @Override
    public void onClose() {

        // TODO Save the old states in Constructor
        // TODO Save the new states to the Tile if changes were made.

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
            return MessageFormat.format("{0}. {1}", index + 1, states[index]);
        } else {
            return "-";
        }
    }

    private boolean shouldBeEnabled(int indexOffset) {
        int index = entriesIndex + indexOffset;
        return index != itemIndex && index < states.length;
    }

    private boolean shouldBeEnabled(boolean upButton, int indexOffset) {
        int index = entriesIndex + indexOffset;
        if (upButton) {
            return index > 0 && index < states.length;
        } else {
            return index + 1 < states.length;
        }
    }

    public static void open(Vec3i signalPos, TileSignalPart tileSignalPart) {
        GuiSignalPrioritization.signalPos = signalPos;
        GuiSignalPrioritization.signal = tileSignalPart;
        GUI.get().open(MinecraftClient.getPlayer());
    }


}
