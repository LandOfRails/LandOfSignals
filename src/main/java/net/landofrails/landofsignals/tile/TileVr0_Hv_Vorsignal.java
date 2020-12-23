package net.landofrails.landofsignals.tile;

import cam72cam.mod.block.BlockEntity;
import cam72cam.mod.entity.Player;
import cam72cam.mod.entity.boundingbox.IBoundingBox;
import cam72cam.mod.item.ItemStack;
import cam72cam.mod.math.Vec3d;
import cam72cam.mod.util.Facing;
import net.landofrails.landofsignals.LOSItems;

public class TileVr0_Hv_Vorsignal extends BlockEntity {
    private double fullHeight = 0;
    private double fullWidth = 0;
    private double fullLength = 0;

    private float blockRotate;

    private boolean activated = false;

    public TileVr0_Hv_Vorsignal(float rot) {
        this.blockRotate = rot;
    }

    @Override
    public ItemStack onPick() {
        return new ItemStack(LOSItems.ITEM_VR_0_HV_VORSIGNAL, 1);
    }

    @Override
    public IBoundingBox getBoundingBox() {
        return IBoundingBox.ORIGIN.expand(new Vec3d(fullWidth, fullHeight, fullLength)).offset(new Vec3d(0.5 - fullWidth / 2, 0, 0.5 - fullLength / 2));
    }

    @Override
    public boolean onClick(Player player, Player.Hand hand, Facing facing, Vec3d hit) {
        activated = !activated;
//        this.markDirty();
        return true;
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
}
