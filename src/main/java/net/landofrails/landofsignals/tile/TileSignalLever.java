package net.landofrails.landofsignals.tile;

import cam72cam.mod.block.BlockEntityTickable;
import cam72cam.mod.block.IRedstoneProvider;
import cam72cam.mod.entity.Player;
import cam72cam.mod.entity.boundingbox.IBoundingBox;
import cam72cam.mod.item.ItemStack;
import cam72cam.mod.math.Vec3d;
import cam72cam.mod.serialization.TagField;
import cam72cam.mod.util.Facing;
import net.landofrails.landofsignals.LOSItems;

public class TileSignalLever extends BlockEntityTickable implements IRedstoneProvider {
    private double fullHeight = 0;
    private double fullWidth = 0;
    private double fullLength = 0;

    private long ticks;
    private int leverRotate = 0;
    @TagField("Rotation")
    private float blockRotate;

    @TagField("Activated")
    private boolean activated = false;

    public TileSignalLever(float rot) {
        this.blockRotate = rot;
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
    public boolean onClick(Player player, Player.Hand hand, Facing facing, Vec3d hit) {
        activated = !activated;
//        this.markDirty();
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
        return this.leverRotate;
    }

    public void setFullHeight(double fullHeight) {
        this.fullHeight = fullHeight;
    }

    public void setFullWidth(double fullWidth) {
        this.fullWidth = fullWidth;
    }

    public void setFullLength(double fullLength) {
        this.fullLength = fullLength;
    }

    public float getBlockRotate() {
        return blockRotate;
    }

    public void setBlockRotate(float blockRotate) {
        this.blockRotate = blockRotate;
    }

    @Override
    public int getStrongPower(Facing from) {
        if (activated) {
            return 15;
        } else return 0;
    }

    @Override
    public int getWeakPower(Facing from) {
        if (activated) {
            return 15;
        } else return 0;
    }
}
