package net.landofrails.landofsignals.creator.gui;

import cam72cam.mod.entity.Player;
import cam72cam.mod.gui.GuiRegistry;
import cam72cam.mod.gui.helpers.GUIHelpers;
import cam72cam.mod.gui.screen.IScreen;
import cam72cam.mod.gui.screen.IScreenBuilder;
import cam72cam.mod.item.ItemStack;
import cam72cam.mod.render.OpenGL;
import cam72cam.mod.serialization.TagCompound;
import net.landofrails.landofsignals.LOSGuis;
import net.landofrails.landofsignals.LOSItems;
import org.lwjgl.opengl.GL11;

import java.util.function.Supplier;

public class Test implements IScreen {

    private static final Supplier<GuiRegistry.GUI> GUI = () -> LOSGuis.test;

    @Override
    public void init(IScreenBuilder screen) {

    }

    @Override
    public void onEnterKey(IScreenBuilder builder) {

    }

    @Override
    public void onClose() {

    }

    @Override
    public void draw(IScreenBuilder builder) {
        ItemStack item = new ItemStack(LOSItems.ITEM_CREATOR, 1);
        TagCompound tag = item.getTagCompound();
        tag.setString("path", null);
        item.setTagCompound(tag);
        try (final OpenGL.With ignored = OpenGL.matrix()) {
            GL11.glTranslated(((double) GUIHelpers.getScreenWidth() / 2 - (double) builder.getWidth() / 4) - 120, (double) builder.getHeight() / 4, 0);
            GL11.glScaled(1, 1, 1);
            GUIHelpers.drawItem(item, 0, 0);
        }
    }

    public static void open(Player player) {
        GUI.get().open(player);
    }
}
