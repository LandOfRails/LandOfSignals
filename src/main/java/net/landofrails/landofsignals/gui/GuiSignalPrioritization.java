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
import cam72cam.mod.serialization.TagCompound;
import net.landofrails.api.contentpacks.v2.complexsignal.ContentPackSignalGroup;
import net.landofrails.landofsignals.LOSBlocks;
import net.landofrails.landofsignals.LOSGuis;
import net.landofrails.landofsignals.LOSItems;
import net.landofrails.landofsignals.packet.GuiSignalPrioritizationToServerPacket;
import net.landofrails.landofsignals.serialization.EmptyStringMapper;
import net.landofrails.landofsignals.tile.TileComplexSignal;
import net.landofrails.landofsignals.tile.TileSignalPart;
import util.Matrix4;

import java.text.MessageFormat;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class GuiSignalPrioritization implements IScreen {

    private static TileSignalPart tileSignalPart;
    private static TileComplexSignal tileComplexSignal;
    private static Vec3i signalPos;

    private static final Supplier<GuiRegistry.GUI> GUI = () -> LOSGuis.SIGNAL_PRIORITIZATION;

    private Button[] upButtons = new Button[6];
    private Button[] buttons = new Button[6];
    private Button[] downButtons = new Button[6];
    private Consumer<Integer>[] actions = new Consumer[6];
    private ItemStack item;
    private Slider slider;
    private Button groupButton;

    // TileSignalPart + TileComplexSignal
    private String[] originalStates;
    private String[] states;
    private int itemIndex = 0;
    private int entriesIndex = 0;

    // TileComplexSignal
    private Map<String, String[]> originalGroupStates;
    private Map<String, String[]> groupStates;
    private Map<String, String> groupNames;
    private String[] groups;
    private String groupId;

    @Override
    public void init(IScreenBuilder screen) {

        createItem();

        boolean shouldContinue = initStates(screen);
        if (!shouldContinue)
            return;

        slider = new Slider(screen, -175, 120, GuiText.LABEL_PAGE + ": ", 0, Math.max(1, states.length - 6d), entriesIndex, false) {
            @Override
            public void onSlider() {
                entriesIndex = slider.getValueInt();
                updateButtons();
            }
        };
        slider.setEnabled(states.length > 6);

        groupButton = new Button(screen, -190, -20, 180, 20, GuiText.LABEL_GROUP + ": -") {
            @Override
            public void onClick(Player.Hand hand) {
                String newGroup = next(groups, groupId);
                updateGroup(newGroup);
            }
        };
        if (groups.length > 1) {
            groupButton.setText(GuiText.LABEL_GROUP + ": " + groupNames.get(groupId));
        } else {
            groupButton.setEnabled(false);
        }

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
        refreshItem();

    }

    @Override
    public void draw(IScreenBuilder builder) {
        builder.drawCenteredString(GuiText.LABEL_PRIORITY.toString(), 0, -40, 0xFFFFFF);

        builder.drawCenteredString(GuiText.LABEL_LOW.toString(), 35, 6, 0xFFFFFF);
        builder.drawCenteredString(GuiText.LABEL_HIGH.toString(), 35, 106, 0xFFFFFF);

        refreshItem();

        final int scale = 8;
        Matrix4 matrix = new Matrix4();
        matrix.translate((double) GUIHelpers.getScreenWidth() / 2 + (double) builder.getWidth() / 6, (double) builder.getHeight() / 4, 0);
        matrix.scale(scale, scale, 1);
        GUIHelpers.drawItem(item, 0, 0, matrix);


        // TODO: Check if still needed
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

        if (tileSignalPart != null) {
            if (Arrays.deepEquals(originalStates, states)) {
                return;
            }

            new GuiSignalPrioritizationToServerPacket(signalPos, states).sendToServer();
        } else {

            if (!Arrays.deepEquals(originalStates, states)) {
                groupStates.put(groupId, states);
            }

            if (originalGroupStates.equals(groupStates)) {
                return;
            }

            new GuiSignalPrioritizationToServerPacket(signalPos, groupStates).sendToServer();
        }


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

    private static <T> T next(T[] arr, T value) {
        boolean useNext = false;
        for (T m : arr) {
            if (Objects.equals(m, value))
                useNext = true;
            else if (useNext)
                return m;
        }
        return getFirstValue(arr);
    }

    private static <T> T getFirstValue(T[] values) {
        for (T object : values)
            return object;
        return null;
    }

    private static Map<String, String[]> cleanCopy(Map<String, String[]> map) {
        Map<String, String[]> val = new HashMap<>();
        map.forEach((groupId, states) -> {
            val.put(groupId, Arrays.copyOf(states, states.length));
        });
        return val;
    }

    // Getters for signal-dependent stuff
    private String getId() {
        if (tileSignalPart != null)
            return tileSignalPart.getId();
        return tileComplexSignal.getId();
    }

    private boolean initStates(IScreenBuilder screen) {
        if (tileSignalPart != null) {
            states = tileSignalPart.getOrderedStates();
            if (states == null || states.length == 0) {
                screen.close();
                return false;
            }

            originalStates = new String[states.length];
            System.arraycopy(states, 0, originalStates, 0, states.length);

            groups = new String[0];
        } else {
            groupStates = tileComplexSignal.getOrderedGroupStates();
            if (groupStates == null || groupStates.size() == 0) {
                screen.close();
                return false;
            }

            originalGroupStates = cleanCopy(groupStates);

            Map<String, ContentPackSignalGroup> signalGroups = LOSBlocks.BLOCK_COMPLEX_SIGNAL.getAllGroupStates(tileComplexSignal.getId());
            groups = signalGroups.keySet().toArray(new String[0]);
            groupNames = new HashMap<>();
            signalGroups.forEach((groupId, group) ->
                    groupNames.put(groupId, group.getGroupName())
            );
            groupId = groups[0];
            states = groupStates.get(groupId);
        }
        return true;
    }

    private void createItem() {
        String blockId = getId();
        if (tileSignalPart != null) {
            item = new ItemStack(LOSItems.ITEM_SIGNAL_PART, 1);
            TagCompound tag = item.getTagCompound();
            tag.setString("itemId", blockId);
            item.setTagCompound(tag);
        } else {
            item = new ItemStack(LOSItems.ITEM_COMPLEX_SIGNAL, 1);
            TagCompound tag = item.getTagCompound();
            tag.setString("itemId", blockId);
            item.setTagCompound(tag);
        }
    }

    private void refreshItem() {
        if (tileSignalPart != null) {
            final TagCompound rightTag = item.getTagCompound();
            rightTag.setString("itemState", states[itemIndex]);
            item.setTagCompound(rightTag);
        } else {
            final TagCompound rightTag = item.getTagCompound();
            rightTag.setMap("itemGroupState", Collections.singletonMap(groupId, states[itemIndex]), EmptyStringMapper::toNullString, value -> new TagCompound().setString("string", EmptyStringMapper.toNullString(value)));
            item.setTagCompound(rightTag);
        }
    }

    private void updateGroup(String newGroup) {
        if (!Arrays.deepEquals(originalStates, states)) {
            groupStates.put(groupId, states);
        }

        groupId = newGroup;
        groupButton.setText("Group: " + groupNames.get(groupId));
        states = groupStates.get(groupId);

        updateButtons();
    }

    // Open-Functions

    public static void open(Vec3i signalPos, TileSignalPart tileSignalPart) {
        GuiSignalPrioritization.signalPos = signalPos;
        GuiSignalPrioritization.tileSignalPart = tileSignalPart;
        GuiSignalPrioritization.tileComplexSignal = null;
        GUI.get().open(MinecraftClient.getPlayer());
    }

    public static void open(Vec3i signalPos, TileComplexSignal tileComplexSignal) {
        GuiSignalPrioritization.signalPos = signalPos;
        GuiSignalPrioritization.tileSignalPart = null;
        GuiSignalPrioritization.tileComplexSignal = tileComplexSignal;
        GUI.get().open(MinecraftClient.getPlayer());
    }

}
