package net.landofrails.landofsignals.tile;

import cam72cam.mod.block.BlockEntity;
import cam72cam.mod.entity.boundingbox.IBoundingBox;
import cam72cam.mod.item.ItemStack;
import cam72cam.mod.math.Vec3d;
import net.landofrails.landofsignals.LOSItems;

public class TileSignalSO12 extends BlockEntity {

    private double fullHeight = 0;
    private double fullWidth = 0;
    private double fullLength = 0;

    @Override
    public ItemStack onPick() {
        return new ItemStack(LOSItems.ITEM_SIGNALSO12, 1);
    }

    @Override
    public IBoundingBox getBoundingBox() {
        return IBoundingBox.ORIGIN.expand(new Vec3d(fullWidth, fullHeight, fullLength)).offset(new Vec3d(0.5 - fullWidth / 2, 0, 0.5 - fullLength / 2));
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
}
