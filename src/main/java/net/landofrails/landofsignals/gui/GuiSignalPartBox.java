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

public class GuiSignalPartBox implements IScreen {

    private final ItemStack itemStackRight;
    private final ItemStack itemStackLeft;
    private final TileSignalBox tsb;
    private static String textureNameRight;
    private static String textureNameLeft;
    private int stateRight;
    private int stateLeft;

    private final String[] listTextureNames;

    public GuiSignalPartBox(final TileSignalBox tsb) {
        this.tsb = tsb;
        final TileSignalPart tsp = tsb.getTileSignalPart();

        listTextureNames = LOSBlocks.BLOCK_SIGNAL_PART.getStates(tsp.getId()).toArray(new String[0]);
        stateRight = tsb.getRedstone();
        stateLeft = tsb.getNoRedstone();
        if (stateRight >= listTextureNames.length || stateLeft >= listTextureNames.length) {
            stateRight = 0;
            stateLeft = 0;
        }
        textureNameLeft = listTextureNames[stateLeft];
        textureNameRight = listTextureNames[stateRight];

        itemStackLeft = new ItemStack(LOSItems.ITEM_SIGNAL_PART, 1);
        final TagCompound tag = itemStackLeft.getTagCompound();
        tag.setString("itemId", tsp.getId());
        tag.setString("textureName", textureNameLeft);
        itemStackLeft.setTagCompound(tag);

        itemStackRight = new ItemStack(LOSItems.ITEM_SIGNAL_PART, 1);
        final TagCompound tag2 = itemStackRight.getTagCompound();
        tag2.setString("itemId", tsp.getId());
        tag2.setString("textureName", textureNameRight);
        itemStackRight.setTagCompound(tag2);


    }

    @Override
    public void init(final IScreenBuilder screen) {
        new Button(screen, -100, 0, "<-- " + GuiText.LABEL_NOREDSTONE) {
            @Override
            public void onClick(final Player.Hand hand) {
                stateLeft++;
                if (stateLeft == listTextureNames.length) {
                    stateLeft = 0;
                }
                textureNameLeft = listTextureNames[stateLeft];
            }
        };
        new Button(screen, -100, 50, GuiText.LABEL_REDSTONE + " -->") {
            @Override
            public void onClick(final Player.Hand hand) {
                stateRight++;
                if (stateRight == listTextureNames.length) {
                    stateRight = 0;
                }
                textureNameRight = listTextureNames[stateRight];
            }
        };
    }

    @Override
    public void onEnterKey(final IScreenBuilder builder) {
        builder.close();
    }

    @Override
    public void onClose() {
        tsb.setNoRedstone(stateLeft);
        tsb.setRedstone(stateRight);
        final SignalBoxGuiToServerPacket packet = new SignalBoxGuiToServerPacket(stateRight, stateLeft, tsb.getPos());
        packet.sendToServer();
    }

    @Override
    public void draw(final IScreenBuilder builder) {
        final int scale = 8;

        final TagCompound rightTag = itemStackRight.getTagCompound();
        rightTag.setString("textureName", textureNameRight);
        itemStackRight.setTagCompound(rightTag);

        try (final OpenGL.With ignored = OpenGL.matrix()) {
            GL11.glTranslated((double) GUIHelpers.getScreenWidth() / 2 + (double) builder.getWidth() / 4, (double) builder.getHeight() / 4, 0);
            GL11.glScaled(scale, scale, 1);
            GUIHelpers.drawItem(itemStackRight, 0, 0);
        }

        final TagCompound leftTag = itemStackLeft.getTagCompound();
        leftTag.setString("textureName", textureNameLeft);
        itemStackLeft.setTagCompound(leftTag);

        try (final OpenGL.With ignored = OpenGL.matrix()) {
            GL11.glTranslated(((double) GUIHelpers.getScreenWidth() / 2 - (double) builder.getWidth() / 4) - 120, (double) builder.getHeight() / 4, 0);
            GL11.glScaled(scale, scale, 1);
            GUIHelpers.drawItem(itemStackLeft, 0, 0);
        }
    }
}
