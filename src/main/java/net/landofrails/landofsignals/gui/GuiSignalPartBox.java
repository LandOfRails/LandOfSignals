package net.landofrails.landofsignals.gui;

import cam72cam.mod.entity.Player;
import cam72cam.mod.gui.helpers.GUIHelpers;
import cam72cam.mod.gui.screen.Button;
import cam72cam.mod.gui.screen.IScreen;
import cam72cam.mod.gui.screen.IScreenBuilder;
import cam72cam.mod.item.ItemStack;
import cam72cam.mod.render.OpenGL;
import cam72cam.mod.serialization.TagCompound;
import net.landofrails.api.contentpacks.v2.signal.ContentPackSignalGroup;
import net.landofrails.landofsignals.LOSBlocks;
import net.landofrails.landofsignals.LOSItems;
import net.landofrails.landofsignals.packet.SignalBoxGuiToServerPacket;
import net.landofrails.landofsignals.serialization.EmptyStringMapper;
import net.landofrails.landofsignals.tile.TileSignalBox;
import net.landofrails.landofsignals.tile.TileSignalPart;
import org.lwjgl.opengl.GL11;

import java.util.*;

public class GuiSignalPartBox implements IScreen {

    private final ItemStack itemStackRight;
    private final ItemStack itemStackLeft;
    private final TileSignalBox tsb;

    // List of modes
    private Map<String, ContentPackSignalGroup> modes;
    private Set<String> modeGroups;

    // Group
    private String signalGroup;
    private Button groupButton;
    private String rightState;
    private String leftState;

    public GuiSignalPartBox(TileSignalBox tsb) {
        this.tsb = tsb;
        TileSignalPart tsp = tsb.getTileSignalPart();

        modes = LOSBlocks.BLOCK_SIGNAL_PART.getAllGroupStates(tsp.getId());
        modeGroups = modes.keySet();
        signalGroup = tsb.getGroupId(getFirstValue(modeGroups));
        rightState = tsb.getActiveGroupState(modes.get(signalGroup).getStates().keySet().iterator().next());
        leftState = tsb.getInactiveGroupState(modes.get(signalGroup).getStates().keySet().iterator().next());

        itemStackLeft = new ItemStack(LOSItems.ITEM_SIGNAL_PART, 1);
        TagCompound tag = itemStackLeft.getTagCompound();
        tag.setString("itemId", tsp.getId());
        itemStackLeft.setTagCompound(tag);

        itemStackRight = new ItemStack(LOSItems.ITEM_SIGNAL_PART, 1);
        TagCompound tag2 = itemStackRight.getTagCompound();
        tag2.setString("itemId", tsp.getId());
        itemStackRight.setTagCompound(tag2);


    }

    @Override
    public void init(IScreenBuilder screen) {
        // Use first available group
        groupButton = new Button(screen, -100, 0, GuiText.LABEL_SIGNALGROUP.toString(modes.get(signalGroup).getGroupName())) {
            @Override
            public void onClick(Player.Hand hand) {
                signalGroup = nextMode(signalGroup);
                rightState = modes.get(signalGroup).getStates().keySet().iterator().next();
                leftState = modes.get(signalGroup).getStates().keySet().iterator().next();
            }
        };
        new Button(screen, -100, 50, "<-- " + GuiText.LABEL_NOREDSTONE.toString()) {
            @Override
            public void onClick(Player.Hand hand) {
                leftState = nextState(leftState);
            }
        };
        new Button(screen, -100, 100, GuiText.LABEL_REDSTONE.toString() + " -->") {
            @Override
            public void onClick(Player.Hand hand) {
                rightState = nextState(rightState);
            }
        };

    }

    @Override
    public void onEnterKey(IScreenBuilder builder) {
        builder.close();
    }

    @Override
    public void onClose() {

        tsb.setGroupId(signalGroup);
        tsb.setInactiveGroupState(leftState);
        tsb.setActiveGroupState(rightState);

        SignalBoxGuiToServerPacket packet = new SignalBoxGuiToServerPacket(tsb);
        packet.sendToServer();
    }

    @Override
    public void draw(IScreenBuilder builder) {
        int scale = 8;

        TagCompound rightTag = itemStackRight.getTagCompound();
        rightTag.setMap("itemGroupState", Collections.singletonMap(signalGroup, rightState), EmptyStringMapper::toNullString, value -> new TagCompound().setString("string", value));
        itemStackRight.setTagCompound(rightTag);

        try (OpenGL.With ignored = OpenGL.matrix()) {
            GL11.glTranslated((double) GUIHelpers.getScreenWidth() / 2 + (double) builder.getWidth() / 4, (double) builder.getHeight() / 4, 0);
            GL11.glScaled(scale, scale, 1);
            GUIHelpers.drawItem(itemStackRight, 0, 0);
        }

        TagCompound leftTag = itemStackLeft.getTagCompound();
        leftTag.setMap("itemGroupState", Collections.singletonMap(signalGroup, leftState), EmptyStringMapper::toNullString, value -> new TagCompound().setString("string", value));
        itemStackLeft.setTagCompound(leftTag);

        try (OpenGL.With ignored = OpenGL.matrix()) {
            GL11.glTranslated(((double) GUIHelpers.getScreenWidth() / 2 - (double) builder.getWidth() / 4) - 120, (double) builder.getHeight() / 4, 0);
            GL11.glScaled(scale, scale, 1);
            GUIHelpers.drawItem(itemStackLeft, 0, 0);
        }

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
