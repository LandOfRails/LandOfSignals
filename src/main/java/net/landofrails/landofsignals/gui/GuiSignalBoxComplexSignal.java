package net.landofrails.landofsignals.gui;

import cam72cam.mod.MinecraftClient;
import cam72cam.mod.entity.Player;
import cam72cam.mod.gui.helpers.GUIHelpers;
import cam72cam.mod.gui.screen.Button;
import cam72cam.mod.gui.screen.IScreen;
import cam72cam.mod.gui.screen.IScreenBuilder;
import cam72cam.mod.item.ItemStack;
import cam72cam.mod.serialization.TagCompound;
import net.landofrails.api.contentpacks.v2.complexsignal.ContentPackSignalGroup;
import net.landofrails.landofsignals.LOSBlocks;
import net.landofrails.landofsignals.LOSGuis;
import net.landofrails.landofsignals.LOSItems;
import net.landofrails.landofsignals.packet.SignalBoxGuiToServerPacket;
import net.landofrails.landofsignals.serialization.EmptyStringMapper;
import net.landofrails.landofsignals.tile.TileComplexSignal;
import net.landofrails.landofsignals.tile.TileSignalBox;
import util.Matrix4;

import java.util.*;

public class GuiSignalBoxComplexSignal implements IScreen {

    private static TileSignalBox tsb;
    private static String signalId;

    private final ItemStack itemStackRight;
    private final ItemStack itemStackLeft;

    // List of modes
    private Map<String, ContentPackSignalGroup> modes;
    private Set<String> modeGroups;

    // Group
    private String signalGroup;
    private String originalSignalGroup;
    private Button groupButton;
    private String rightState;
    private String leftState;
    private String originalRightState;
    private String originalLeftState;

    public GuiSignalBoxComplexSignal() {

        modes = LOSBlocks.BLOCK_COMPLEX_SIGNAL.getAllGroupStates(signalId);
        modeGroups = modes.keySet();
        originalSignalGroup = tsb.getGroupId(getFirstValue(modeGroups));
        signalGroup = originalSignalGroup;
        originalRightState = tsb.getActiveGroupState(modes.get(signalGroup).getStates().keySet().iterator().next());
        rightState = originalRightState;
        originalLeftState = tsb.getInactiveGroupState(modes.get(signalGroup).getStates().keySet().iterator().next());
        leftState = originalLeftState;


        itemStackLeft = new ItemStack(LOSItems.ITEM_COMPLEX_SIGNAL, 1);
        final TagCompound tag = itemStackLeft.getTagCompound();
        tag.setString("itemId", signalId);
        itemStackLeft.setTagCompound(tag);

        itemStackRight = new ItemStack(LOSItems.ITEM_COMPLEX_SIGNAL, 1);
        final TagCompound tag2 = itemStackRight.getTagCompound();
        tag2.setString("itemId", signalId);
        itemStackRight.setTagCompound(tag2);


    }

    public static void open(final TileSignalBox tileSignalBox, String signalId) {
        tsb = tileSignalBox;
        GuiSignalBoxComplexSignal.signalId = signalId;
        LOSGuis.SIGNAL_BOX_COMPLEX_SIGNAL.open(MinecraftClient.getPlayer());
    }

    @Override
    public void init(final IScreenBuilder screen) {
        // Use first available group
        groupButton = new Button(screen, -100, 0, GuiText.LABEL_SIGNALGROUP.toString(modes.get(signalGroup).getGroupName())) {
            @Override
            public void onClick(Player.Hand hand) {
                originalSignalGroup = nextMode(signalGroup);
                signalGroup = originalSignalGroup;
                originalRightState = modes.get(signalGroup).getStates().keySet().iterator().next();
                rightState = originalRightState;
                originalLeftState = modes.get(signalGroup).getStates().keySet().iterator().next();
                leftState = originalLeftState;
            }
        };
        new Button(screen, -100, 50, "<-- " + GuiText.LABEL_NOREDSTONE) {
            @Override
            public void onClick(final Player.Hand hand) {
                leftState = nextState(leftState);
            }
        };
        new Button(screen, -100, 100, GuiText.LABEL_REDSTONE + " -->") {
            @Override
            public void onClick(final Player.Hand hand) {
                rightState = nextState(rightState);
            }
        };

    }

    @Override
    public void onEnterKey(final IScreenBuilder builder) {
        builder.close();
    }

    @Override
    public void onClose() {

        if (!Objects.equals(originalSignalGroup, signalGroup) || !Objects.equals(originalLeftState, leftState) || !Objects.equals(originalRightState, rightState)) {
            tsb.setGroupId(signalGroup);
            tsb.setInactiveGroupState(leftState);
            tsb.setActiveGroupState(rightState);

            final SignalBoxGuiToServerPacket packet = new SignalBoxGuiToServerPacket(tsb);
            packet.sendToServer();
        }

    }

    @Override
    public void draw(final IScreenBuilder builder) {
        final int scale = 8;

        final TagCompound rightTag = itemStackRight.getTagCompound();
        rightTag.setMap("itemGroupState", Collections.singletonMap(signalGroup, rightState), EmptyStringMapper::toNullString, value -> new TagCompound().setString("string", value));
        itemStackRight.setTagCompound(rightTag);

        Matrix4 matrix = new Matrix4();
        matrix.translate((double) GUIHelpers.getScreenWidth() / 2 + (double) builder.getWidth() / 4, (double) builder.getHeight() / 4, 0);
        matrix.scale(scale, scale, 1);
        GUIHelpers.drawItem(itemStackRight, 0, 0, matrix);

        final TagCompound leftTag = itemStackLeft.getTagCompound();
        leftTag.setMap("itemGroupState", Collections.singletonMap(signalGroup, leftState), EmptyStringMapper::toNullString, value -> new TagCompound().setString("string", value));
        itemStackLeft.setTagCompound(leftTag);

        matrix = new Matrix4();
        matrix.translate(((double) GUIHelpers.getScreenWidth() / 2 - (double) builder.getWidth() / 4) - 120, (double) builder.getHeight() / 4, 0);
        matrix.scale(scale, scale, 1);
        GUIHelpers.drawItem(itemStackLeft, 0, 0, matrix);

        groupButton.setText(GuiText.LABEL_SIGNALGROUP.toString(modes.get(signalGroup).getGroupName()));
    }

    private String nextMode(String mode) {
        boolean useNext = false;
        for (String m : modeGroups) {
            if (Objects.equals(m, mode))
                useNext = true;
            else if (useNext)
                return m;
        }
        return getFirstValue(modeGroups);
    }

    private String nextState(String state) {
        boolean useNext = false;
        for (String m : modes.get(signalGroup).getStates().keySet()) {
            if (Objects.equals(m, state) || (m != null && m.equalsIgnoreCase(state)))
                useNext = true;
            else if (useNext)
                return m;
        }
        return getFirstValue(modes.get(signalGroup).getStates().keySet());
    }

    @SuppressWarnings("java:S1751")
    private static <T> T getFirstValue(Collection<T> collection) {
        for (T object : collection)
            return object;
        return null;
    }

}
