package net.landofrails.landofsignals.gui;

import cam72cam.mod.MinecraftClient;
import cam72cam.mod.entity.Player;
import cam72cam.mod.gui.helpers.GUIHelpers;
import cam72cam.mod.gui.screen.Button;
import cam72cam.mod.gui.screen.IScreen;
import cam72cam.mod.gui.screen.IScreenBuilder;
import cam72cam.mod.item.ItemStack;
import cam72cam.mod.render.OpenGL;
import net.landofrails.landofsignals.tile.TileSignalBox;
import net.landofrails.landofsignals.tile.TileTop;
import net.landofrails.landofsignals.utils.Static;
import org.lwjgl.opengl.GL11;

public class GuiSignalBox implements IScreen {

    private Button switchWithoutRedstoneButton;
    private Button switchWithRedstoneButton;
    private ItemStack itemStack;
    private TileTop tt;

    public GuiSignalBox(TileSignalBox ts) {
        this.tt = MinecraftClient.getPlayer().getWorld().getBlockEntity(Static.listTopBlocks.get(ts.getUUIDTileTop()), TileTop.class);
        itemStack = new ItemStack(Static.listTopModels.get(tt.getBlock())._3(), 1);
    }

    @Override
    public void init(IScreenBuilder screen) {
        switchWithoutRedstoneButton = new Button(screen, 0 - 100, 0, "Without redstone") {
            @Override
            public void onClick(Player.Hand hand) {

            }
        };
        switchWithRedstoneButton = new Button(screen, 0 - 100, 0 + 50, "With redstone") {
            @Override
            public void onClick(Player.Hand hand) {

            }
        };
    }

    @Override
    public void onEnterKey(IScreenBuilder builder) {
        builder.close();
    }

    @Override
    public void onClose() {

    }

    @Override
    public void draw(IScreenBuilder builder) {
        int scale = 8;
        try (OpenGL.With matrix = OpenGL.matrix()) {
            GL11.glTranslated(GUIHelpers.getScreenWidth() / 2 + builder.getWidth() / 4, builder.getHeight() / 4, 0);
            GL11.glScaled(scale, scale, 1);
            GUIHelpers.drawItem(itemStack, 0, 0);
        }
    }
}
