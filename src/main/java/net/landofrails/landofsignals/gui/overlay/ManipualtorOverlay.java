package net.landofrails.landofsignals.gui.overlay;

import cam72cam.mod.MinecraftClient;
import cam72cam.mod.block.BlockEntity;
import cam72cam.mod.entity.Player;
import cam72cam.mod.gui.helpers.GUIHelpers;
import cam72cam.mod.item.ItemStack;
import cam72cam.mod.math.Vec3d;
import net.landofrails.landofsignals.LOSGuis;
import net.landofrails.landofsignals.LOSItems;
import net.landofrails.landofsignals.gui.GuiText;
import net.landofrails.landofsignals.items.ItemManipulator;
import net.landofrails.landofsignals.packet.ManipulatorToClientPacket;
import net.landofrails.landofsignals.utils.IManipulate;
import net.landofrails.landofsignals.utils.Static;

public class ManipualtorOverlay {

    private final int screenWidth;
    private final int screenHeight;

    public ManipualtorOverlay() {
        screenWidth = GUIHelpers.getScreenWidth();
        screenHeight = GUIHelpers.getScreenHeight();
    }

    Vec3d offset;

    public void draw() {
        final ItemStack item = MinecraftClient.getPlayer().getHeldItem(Player.Hand.PRIMARY);
        if (!item.is(LOSItems.ITEM_MANIPULATOR) || !ItemManipulator.editIngame) return;
        final BlockEntity block = LOSItems.ITEM_MANIPULATOR.getBlock();
        if (block instanceof IManipulate) {
            final Player player = MinecraftClient.getPlayer();
            if (player.isCrouching()) {
                LOSGuis.MANIPULATOR.open(player, block.getPos());
                ItemManipulator.editIngame = false;
                return;
            }
            if (!ItemManipulator.editHeight) {
                final Vec3d fastMovement = player.getMovementInput();
                final Vec3d movement = new Vec3d(Static.round(fastMovement.x / 10, 3), 0, Static.round(fastMovement.z / 10, 3));
                handlePacket(block, player, movement);
                GUIHelpers.drawCenteredString("X: " + Static.round(offset.x, 3), screenWidth - 50, screenHeight - 50, 0xffffff);
                GUIHelpers.drawCenteredString("Z: " + Static.round(offset.z, 3), screenWidth - 50, screenHeight - 70, 0xffffff);
            } else {
                final Vec3d fastMovement = player.getVelocity();
                final Vec3d movement = new Vec3d(0, Static.round(fastMovement.x / 10, 3), 0);
                handlePacket(block, player, movement);
                GUIHelpers.drawCenteredString("Y: " + Static.round(offset.y, 3), screenWidth - 50, screenHeight - 50, 0xffffff);
            }
        }
    }

    private void handlePacket(final BlockEntity block, final Player player, final Vec3d movement) {
        final ManipulatorToClientPacket packet = new ManipulatorToClientPacket(LOSItems.ITEM_MANIPULATOR.getPlayerMainPos(), movement, block.getPos(), ItemManipulator.sneak);
        packet.sendToAll();
        player.setPosition(LOSItems.ITEM_MANIPULATOR.getPlayerMainPos());
        final IManipulate manipulate = (IManipulate) block;
        manipulate.setOffset(manipulate.getOffset().add(movement));

        GUIHelpers.drawCenteredString(GuiText.LABEL_UNATTACH.toString(), screenWidth - 100, screenHeight - 30, 0xffffff);
        offset = manipulate.getOffset();
    }
}
