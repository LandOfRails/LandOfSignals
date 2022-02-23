package net.landofrails.landofsignals.tile;

import cam72cam.mod.block.BlockEntity;
import cam72cam.mod.entity.Player;
import cam72cam.mod.entity.boundingbox.IBoundingBox;
import cam72cam.mod.item.ItemStack;
import cam72cam.mod.math.Vec3d;
import cam72cam.mod.serialization.TagField;
import cam72cam.mod.util.Facing;
import net.landofrails.landofsignals.LOSItems;

public class TileTicketMachineDB extends BlockEntity {

    @TagField("Rotation")
    private float blockRotate;

    public TileTicketMachineDB(final float rot) {
        blockRotate = rot;
    }

    @Override
    public ItemStack onPick() {
        return new ItemStack(LOSItems.ITEM_TICKET_MACHINE_DB, 1);
    }

    @Override
    public IBoundingBox getBoundingBox() {
        return IBoundingBox.BLOCK.expand(new Vec3d(0, 1, 0));
    }

    @Override
    public boolean onClick(final Player player, final Player.Hand hand, final Facing facing, final Vec3d hit) {
        return false;
    }

    public float getBlockRotate() {
        return blockRotate;
    }

    public void setBlockRotate(final float blockRotate) {
        this.blockRotate = blockRotate;
    }
}
