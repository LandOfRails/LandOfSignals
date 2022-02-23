package net.landofrails.landofsignals.tile;

import cam72cam.mod.block.BlockEntity;
import cam72cam.mod.entity.boundingbox.IBoundingBox;
import cam72cam.mod.item.ItemStack;
import cam72cam.mod.math.Vec3d;
import net.landofrails.landofsignals.LOSItems;

public class TileSignalSO12 extends BlockEntity {

    private double fullHeight;
    private double fullWidth;
    private double fullLength;

    @Override
    public ItemStack onPick() {
        return new ItemStack(LOSItems.ITEM_SIGNALSO12, 1);
    }

    @Override
    public IBoundingBox getBoundingBox() {
        return IBoundingBox.ORIGIN.expand(new Vec3d(fullWidth, fullHeight, fullLength)).offset(new Vec3d(0.5 - fullWidth / 2, 0, 0.5 - fullLength / 2));
    }

    public void setFullHeight(final double fullHeight) {
        this.fullHeight = fullHeight;
    }

    public void setFullWidth(final double fullWidth) {
        this.fullWidth = fullWidth;
    }

    public void setFullLength(final double fullLength) {
        this.fullLength = fullLength;
    }
}
