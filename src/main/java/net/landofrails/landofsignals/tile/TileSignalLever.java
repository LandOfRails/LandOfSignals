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
        return this.leverRotate;
    }

    public float getBlockRotate() {
        return blockRotate;
    }

    public void setBlockRotate(float blockRotate) {
        this.blockRotate = blockRotate;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

	/**
	 * <pre>
	 * Gibt den anliegenden Blöcken ein Redstonesignal.
	 * <b>HINWEIS:</b> Löst leider kein onNeighborChange aus!
	 * </pre>
	 */
    @Override
	public int getStrongPower(Facing from) {
		return activated ? 15 : 0;
	}

	@Override
    public int getWeakPower(Facing from) {
		return activated ? 15 : 0;
    }
}
