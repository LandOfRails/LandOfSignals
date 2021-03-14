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
import net.landofrails.landofsignals.tile.TileSignalPart;
import org.lwjgl.opengl.GL11;

public class GuiSignalBox implements IScreen {

    private final ItemStack itemStackRight;
    private final ItemStack itemStackLeft;
    private final TileSignalBox tsb;
    private static String textureName = null;
    private static String textureNameRight = null;
    private static String textureNameLeft = null;
    private int stateRight;
    private int stateLeft;

    private final String[] listTexureNames;

    @SuppressWarnings("java:S3010")
    public GuiSignalBox(TileSignalBox tsb) {
        this.tsb = tsb;
        TileSignalPart tsp = tsb.getTileSignalPart();

        itemStackLeft = new ItemStack(LOSItems.ITEM_SIGNAL_PART, 1);
        TagCompound tag = itemStackLeft.getTagCompound();
        tag.setString("itemId", tsp.getId());
        itemStackLeft.setTagCompound(tag);

        itemStackRight = new ItemStack(LOSItems.ITEM_SIGNAL_PART, 1);
        TagCompound tag2 = itemStackRight.getTagCompound();
        tag2.setString("itemId", tsp.getId());
        itemStackRight.setTagCompound(tag2);

        listTexureNames = LOSBlocks.BLOCK_SIGNAL_PART.getStates(tsp.getId()).toArray(new String[0]);
        stateRight = tsb.getRedstone();
        stateLeft = tsb.getNoRedstone();
        if (stateRight >= listTexureNames.length || stateLeft >= listTexureNames.length) {
            stateRight = 0;
            stateLeft = 0;
        }
        textureNameLeft = listTexureNames[stateLeft];
        textureNameRight = listTexureNames[stateRight];
    }

    @SuppressWarnings("java:S2696")
    @Override
    public void init(IScreenBuilder screen) {
        new Button(screen, -100, 0, "<-- " + GuiText.LABEL_NOREDSTONE.toString()) {
            @Override
            public void onClick(Player.Hand hand) {
                stateLeft++;
                if (stateLeft == listTexureNames.length) {
                    stateLeft = 0;
                }
                textureNameLeft = listTexureNames[stateLeft];
            }
        };
        new Button(screen, -100, 50, GuiText.LABEL_REDSTONE.toString() + " -->") {
            @Override
            public void onClick(Player.Hand hand) {
                stateRight++;
                if (stateRight == listTexureNames.length) {
                    stateRight = 0;
                }
                textureNameRight = listTexureNames[stateRight];
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
        textureName = null;
        tsb.setNoRedstone(stateLeft);
        tsb.setRedstone(stateRight);
        SignalBoxGuiToServerPacket packet = new SignalBoxGuiToServerPacket(stateRight, stateLeft, tsb.getPos());
        packet.sendToServer();
    }

    @SuppressWarnings("java:S2696")
    @Override
    public void draw(IScreenBuilder builder) {
        int scale = 8;
        textureName = textureNameRight;
        try (OpenGL.With ignored = OpenGL.matrix()) {
            GL11.glTranslated((double) GUIHelpers.getScreenWidth() / 2 + (double) builder.getWidth() / 4, (double) builder.getHeight() / 4, 0);
            GL11.glScaled(scale, scale, 1);
            GUIHelpers.drawItem(itemStackRight, 0, 0);
        }
        textureName = textureNameLeft;
        try (OpenGL.With ignored = OpenGL.matrix()) {
            GL11.glTranslated(((double) GUIHelpers.getScreenWidth() / 2 - (double) builder.getWidth() / 4) - 120, (double) builder.getHeight() / 4, 0);
            GL11.glScaled(scale, scale, 1);
            GUIHelpers.drawItem(itemStackLeft, 0, 0);
        }
    }

    public static String getTexureName() {
        return textureName;
    }
}
