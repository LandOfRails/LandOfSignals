package net.landofrails.landofsignals.gui;

import cam72cam.mod.MinecraftClient;
import cam72cam.mod.entity.Player;
import cam72cam.mod.gui.helpers.GUIHelpers;
import cam72cam.mod.gui.screen.Button;
import cam72cam.mod.gui.screen.IScreen;
import cam72cam.mod.gui.screen.IScreenBuilder;
import cam72cam.mod.item.ItemStack;
import cam72cam.mod.render.OpenGL;
import cam72cam.mod.serialization.TagCompound;
import cam72cam.mod.text.PlayerMessage;
import net.landofrails.landofsignals.LOSBlocks;
import net.landofrails.landofsignals.LOSGuis;
import net.landofrails.landofsignals.LOSItems;
import net.landofrails.landofsignals.packet.SignalBoxGuiToServerPacket;
import net.landofrails.landofsignals.tile.TileSignalBox;
import net.landofrails.landofsignals.tile.TileSignalPart;
import org.lwjgl.opengl.GL11;

import java.util.Objects;

public class GuiSignalBoxSignalPart implements IScreen {

    private static TileSignalBox tsb;

    private final ItemStack itemStackRight;
    private final ItemStack itemStackLeft;

    private String[] states;
    private Button groupButton;
    private String rightState;
    private String leftState;
    private String originalRightState;
    private String originalLeftState;

    public GuiSignalBoxSignalPart() {

        final TileSignalPart tsp = tsb.getTileSignalPart();
        String itemId = tsp.getId();

        states = LOSBlocks.BLOCK_SIGNAL_PART.getAllStates(tsp.getId());

        originalRightState = tsb.getActiveGroupState(new String[]{null});
        rightState = originalRightState;
        originalLeftState = tsb.getInactiveGroupState(new String[]{null});
        leftState = originalLeftState;


        itemStackLeft = new ItemStack(LOSItems.ITEM_SIGNAL_PART, 1);
        final TagCompound tag = itemStackLeft.getTagCompound();
        tag.setString("itemId", itemId);
        itemStackLeft.setTagCompound(tag);

        itemStackRight = new ItemStack(LOSItems.ITEM_SIGNAL_PART, 1);
        final TagCompound tag2 = itemStackRight.getTagCompound();
        tag2.setString("itemId", itemId);
        itemStackRight.setTagCompound(tag2);


    }

    public static void open(final TileSignalBox tileSignalBox) {
        tsb = tileSignalBox;
        LOSGuis.SIGNAL_BOX_SIGNAL_PART.open(MinecraftClient.getPlayer());
    }

    @Override
    public void init(final IScreenBuilder screen) {
        // Use first available group
        groupButton = new Button(screen, -100, 0, GuiText.LABEL_SIGNALGROUP.toString("default")) {
            @Override
            public void onClick(Player.Hand hand) {

            }
        };
        new Button(screen, -100, 50, "<-- " + GuiText.LABEL_NOREDSTONE) {
            @Override
            public void onClick(final Player.Hand hand) {
                leftState = nextState(leftState);
                MinecraftClient.getPlayer().sendMessage(PlayerMessage.direct(leftState != null ? leftState : "its null"));
            }
        };
        new Button(screen, -100, 100, GuiText.LABEL_REDSTONE + " -->") {
            @Override
            public void onClick(final Player.Hand hand) {
                rightState = nextState(rightState);
                MinecraftClient.getPlayer().sendMessage(PlayerMessage.direct(rightState != null ? rightState : "its null"));
            }
        };

    }

    @Override
    public void onEnterKey(final IScreenBuilder builder) {
        builder.close();
    }

    @Override
    public void onClose() {

        if (!Objects.equals(originalLeftState, leftState) || !Objects.equals(originalRightState, rightState)) {
            tsb.setGroupId(null);
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
        rightTag.setString("itemState", rightState);
        itemStackRight.setTagCompound(rightTag);

        try (final OpenGL.With ignored = OpenGL.matrix()) {
            GL11.glTranslated((double) GUIHelpers.getScreenWidth() / 2 + (double) builder.getWidth() / 4, (double) builder.getHeight() / 4, 0);
            GL11.glScaled(scale, scale, 1);
            GUIHelpers.drawItem(itemStackRight, 0, 0);
        }

        final TagCompound leftTag = itemStackLeft.getTagCompound();
        leftTag.setString("itemState", leftState);
        itemStackLeft.setTagCompound(leftTag);

        try (final OpenGL.With ignored = OpenGL.matrix()) {
            GL11.glTranslated(((double) GUIHelpers.getScreenWidth() / 2 - (double) builder.getWidth() / 4) - 120, (double) builder.getHeight() / 4, 0);
            GL11.glScaled(scale, scale, 1);
            GUIHelpers.drawItem(itemStackLeft, 0, 0);
        }

        groupButton.setText(GuiText.LABEL_SIGNALGROUP.toString("default"));
    }

    private String nextState(String currentState) {
        boolean useNext = false;
        for (String state : states) {
            if (Objects.equals(state, currentState) || (state != null && state.equalsIgnoreCase(currentState)))
                useNext = true;
            else if (useNext)
                return state;
        }
        return getFirstValue(states);
    }

    @SuppressWarnings("java:S1751")
    private static <T> T getFirstValue(T[] values) {
        for (T object : values)
            return object;
        return null;
    }

}
