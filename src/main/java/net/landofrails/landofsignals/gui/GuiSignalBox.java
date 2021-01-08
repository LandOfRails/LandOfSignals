package net.landofrails.landofsignals.gui;

import cam72cam.mod.MinecraftClient;
import cam72cam.mod.entity.Player;
import cam72cam.mod.gui.helpers.GUIHelpers;
import cam72cam.mod.gui.screen.Button;
import cam72cam.mod.gui.screen.IScreen;
import cam72cam.mod.gui.screen.IScreenBuilder;
import cam72cam.mod.item.ItemStack;
import cam72cam.mod.render.OpenGL;
import net.landofrails.landofsignals.packet.SignalBoxGuiPacket;
import net.landofrails.landofsignals.tile.TileSignalBox;
import net.landofrails.landofsignals.tile.TileTop;
import net.landofrails.landofsignals.utils.Static;
import org.lwjgl.opengl.GL11;

public class GuiSignalBox implements IScreen {

    private final ItemStack itemStackRight;
    private final ItemStack itemStackLeft;
    private final TileSignalBox ts;
    private static String textureName = null;
    private static String textureNameRight = null;
    private static String textureNameLeft = null;
    private int stateRight;
    private int stateLeft;

    private final String[] listTexureNames;

    @SuppressWarnings("java:S3010")
    public GuiSignalBox(TileSignalBox ts) {
        this.ts = ts;
        TileTop tt = MinecraftClient.getPlayer().getWorld().getBlockEntity(Static.listTopBlocks.get(ts.getUUIDTileTop()), TileTop.class);
        itemStackLeft = new ItemStack(Static.listTopModels.get(tt.getBlock())._3(), 1);
        itemStackRight = new ItemStack(Static.listTopModels.get(tt.getBlock())._3(), 1);
        listTexureNames = Static.listTopModels.get(tt.getBlock())._4().toArray(new String[0]);
        stateRight = ts.getRedstone();
        stateLeft = ts.getNoRedstone();
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
        ts.setNoRedstone(stateLeft);
        ts.setRedstone(stateRight);
        SignalBoxGuiPacket packet = new SignalBoxGuiPacket(stateRight, stateLeft, ts.getPos());
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
