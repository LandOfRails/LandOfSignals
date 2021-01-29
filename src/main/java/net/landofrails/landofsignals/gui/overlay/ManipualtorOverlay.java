package net.landofrails.landofsignals.gui.overlay;

import cam72cam.mod.MinecraftClient;
import cam72cam.mod.block.BlockEntity;
import cam72cam.mod.entity.Player;
import cam72cam.mod.gui.helpers.GUIHelpers;
import cam72cam.mod.item.ItemStack;
import cam72cam.mod.math.Vec3d;
import net.landofrails.landofsignals.LOSItems;
import net.landofrails.landofsignals.packet.ManipulatorPacket;
import net.landofrails.landofsignals.utils.IManipulate;

public class ManipualtorOverlay {
    public void draw() {
        ItemStack item = MinecraftClient.getPlayer().getHeldItem(Player.Hand.PRIMARY);
        if (!item.is(LOSItems.ITEM_MANIPULATOR)) return;
        BlockEntity block = LOSItems.ITEM_MANIPULATOR.getBlock();
        if (block != null && block instanceof IManipulate) {
            Player player = MinecraftClient.getPlayer();
            if (player.isCrouching()) {
                LOSItems.ITEM_MANIPULATOR.clearBlock();
                return;
            }
            Vec3d fastMovement = player.getMovementInput();
            Vec3d movement = new Vec3d(fastMovement.x / 10, fastMovement.y / 10, fastMovement.z / 10);
            ManipulatorPacket packet = new ManipulatorPacket(LOSItems.ITEM_MANIPULATOR.getPlayerMainPos(), movement, player, block.getPos());
            packet.sendToAll();
            player.setPosition(LOSItems.ITEM_MANIPULATOR.getPlayerMainPos());
            IManipulate manipulate = (IManipulate) block;
            manipulate.setOffset(movement);

            //TODO Some gui
            GUIHelpers.drawCenteredString("Moin", 50, 50, 360);
        }
    }
}
