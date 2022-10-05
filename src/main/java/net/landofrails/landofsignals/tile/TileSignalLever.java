package net.landofrails.landofsignals.tile;

import cam72cam.mod.block.BlockEntityTickable;
import cam72cam.mod.block.IRedstoneProvider;
import cam72cam.mod.entity.Player;
import cam72cam.mod.entity.boundingbox.IBoundingBox;
import cam72cam.mod.item.ItemStack;
import cam72cam.mod.math.Vec3d;
import cam72cam.mod.serialization.TagField;
import cam72cam.mod.util.Facing;
import net.landofrails.landofsignals.LOSBlocks;
import net.landofrails.landofsignals.LOSItems;

public class TileSignalLever extends BlockEntityTickable implements IRedstoneProvider {

    private int leverRotate;
    @TagField("Rotation")
    private float blockRotate;

    @TagField("Activated")
    private boolean activated;

    public TileSignalLever(final float rot) {
        blockRotate = rot;
    }

    @Override
    public ItemStack onPick() {
        return new ItemStack(LOSItems.ITEM_SIGNAL_LEVER, 1);
    }

    @Override
    public IBoundingBox getBoundingBox() {
        return IBoundingBox.BLOCK;
    }

    @Override
    public boolean onClick(final Player player, final Player.Hand hand, final Facing facing, final Vec3d hit) {
        activated = !activated;
        // Updates the own coordinate
        getWorld().notifyNeighborsOfStateChange(getPos(), LOSBlocks.BLOCK_SIGNAL_LEVER, true);
        // Update coordinate below
        getWorld().notifyNeighborsOfStateChange(getPos().offset(Facing.DOWN), LOSBlocks.BLOCK_SIGNAL_LEVER, true);
        markDirty();
        return true;
    }

    @Override
    public void update() {
        if (activated && leverRotate < 40) {
            leverRotate++;
        } else if (!activated && leverRotate > 0) {
            leverRotate--;
        }
    }

    public long getLeverRotate() {
        return leverRotate;
    }

    public float getBlockRotate() {
        return blockRotate;
    }

    public void setBlockRotate(final float blockRotate) {
        this.blockRotate = blockRotate;
    }

    public void setActivated(final boolean activated) {
        this.activated = activated;
    }

    @Override
    public int getStrongPower(final Facing from) {
        return activated ? 15 : 0;
    }

    @Override
    public int getWeakPower(final Facing from) {
        return activated ? 15 : 0;
    }
}
