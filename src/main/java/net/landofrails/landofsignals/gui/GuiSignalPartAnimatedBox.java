package net.landofrails.landofsignals.gui;

import cam72cam.mod.entity.Player;
import cam72cam.mod.gui.helpers.GUIHelpers;
import cam72cam.mod.gui.screen.Button;
import cam72cam.mod.gui.screen.IScreen;
import cam72cam.mod.gui.screen.IScreenBuilder;
import cam72cam.mod.item.ItemStack;
import cam72cam.mod.render.OpenGL;
import cam72cam.mod.serialization.TagCompound;
import net.landofrails.landofsignals.LOSBlocks;
import net.landofrails.landofsignals.LOSItems;
import net.landofrails.landofsignals.packet.SignalBoxGuiToServerPacket;
import net.landofrails.landofsignals.tile.TileSignalBox;
import net.landofrails.landofsignals.tile.TileSignalPartAnimated;
import org.lwjgl.opengl.GL11;

public class GuiSignalPartAnimatedBox implements IScreen {

    private final ItemStack itemStackRight;
    private final ItemStack itemStackLeft;
    private final TileSignalBox tsb;
    private static String name = null;
    private static String nameRight = null;
    private static String nameLeft = null;
    private int stateRight;
    private int stateLeft;

    private final String[] listTexureNames;
    private final String[] listAnimationNames;

    @SuppressWarnings("java:S3010")
    public GuiSignalPartAnimatedBox(TileSignalBox tsb) {
        this.tsb = tsb;
        TileSignalPartAnimated tsp = tsb.getTileSignalPartAnimated();

        itemStackLeft = new ItemStack(LOSItems.ITEM_SIGNAL_PART_ANIMATED, 1);
        TagCompound tag = itemStackLeft.getTagCompound();
        tag.setString("itemId", tsp.getId());
        itemStackLeft.setTagCompound(tag);

        itemStackRight = new ItemStack(LOSItems.ITEM_SIGNAL_PART_ANIMATED, 1);
        TagCompound tag2 = itemStackRight.getTagCompound();
        tag2.setString("itemId", tsp.getId());
        itemStackRight.setTagCompound(tag2);

        listTexureNames = LOSBlocks.BLOCK_SIGNAL_PART_ANIMATED.getStates(tsp.getId()).toArray(new String[0]);
        listAnimationNames = LOSBlocks.BLOCK_SIGNAL_PART_ANIMATED.getAniamtions(tsp.getId()).keySet().toArray(new String[0]);

        stateRight = tsb.getRedstone();
        stateLeft = tsb.getNoRedstone();
        if (stateRight >= listTexureNames.length + listAnimationNames.length || stateLeft >= listTexureNames.length + listAnimationNames.length) {
            stateRight = 0;
            stateLeft = 0;
        }
        if (stateLeft < listTexureNames.length) nameLeft = listTexureNames[stateLeft];
        else nameLeft = listAnimationNames[stateLeft];
        if (stateRight < listTexureNames.length) nameRight = listTexureNames[stateRight];
        else nameRight = listAnimationNames[stateRight];
    }

    @SuppressWarnings("java:S2696")
    @Override
    public void init(IScreenBuilder screen) {
        new Button(screen, -100, 0, "<-- " + GuiText.LABEL_NOREDSTONE.toString() + " : " + nameLeft) {
            @Override
            public void onClick(Player.Hand hand) {
                stateLeft++;
                if (stateLeft == listTexureNames.length + listAnimationNames.length) {
                    stateLeft = 0;
                }
                if (stateLeft < listTexureNames.length) nameLeft = listTexureNames[stateLeft];
                else nameLeft = listAnimationNames[stateLeft];

                this.setText("<-- " + GuiText.LABEL_NOREDSTONE.toString() + " : " + nameLeft);
            }
        };
        new Button(screen, -100, 50, GuiText.LABEL_REDSTONE.toString() + " : " + nameRight + " -->") {
            @Override
            public void onClick(Player.Hand hand) {
                stateRight++;
                if (stateRight == listTexureNames.length + listAnimationNames.length) {
                    stateRight = 0;
                }
                if (stateRight < listTexureNames.length) nameRight = listTexureNames[stateRight];
                else nameRight = listAnimationNames[stateRight];

                this.setText(GuiText.LABEL_REDSTONE.toString() + " : " + nameRight + " -->");
            }
        };
    }

    @Override
    public void onEnterKey(IScreenBuilder builder) {
        builder.close();
    }

    @SuppressWarnings("java:S2696")
    @Override
    public void onClose() {
        name = null;
        tsb.setNoRedstone(stateLeft);
        tsb.setRedstone(stateRight);
        SignalBoxGuiToServerPacket packet = new SignalBoxGuiToServerPacket(stateRight, stateLeft, tsb.getPos());
        packet.sendToServer();
    }

    @SuppressWarnings("java:S2696")
    @Override
    public void draw(IScreenBuilder builder) {
        int scale = 8;
        name = nameRight;
        try (OpenGL.With ignored = OpenGL.matrix()) {
            GL11.glTranslated((double) GUIHelpers.getScreenWidth() / 2 + (double) builder.getWidth() / 4, (double) builder.getHeight() / 4, 0);
            GL11.glScaled(scale, scale, 1);
            GUIHelpers.drawItem(itemStackRight, 0, 0);
        }
        name = nameLeft;
        try (OpenGL.With ignored = OpenGL.matrix()) {
            GL11.glTranslated(((double) GUIHelpers.getScreenWidth() / 2 - (double) builder.getWidth() / 4) - 120, (double) builder.getHeight() / 4, 0);
            GL11.glScaled(scale, scale, 1);
            GUIHelpers.drawItem(itemStackLeft, 0, 0);
        }
    }

    public static String getTexureName() {
        return name;
    }
}
