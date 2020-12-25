package net.landofrails.landofsignals.tile;

import cam72cam.mod.block.BlockEntity;
import cam72cam.mod.entity.boundingbox.IBoundingBox;
import cam72cam.mod.item.ItemStack;
import cam72cam.mod.serialization.TagField;
import net.landofrails.landofsignals.utils.Static;

public class TileTop extends BlockEntity {
    private double fullHeight = 0;
    private double fullWidth = 0;
    private double fullLength = 0;

    @TagField("blockRotation")
    private float blockRotate;
    @TagField("blockName")
    private String block;

    private boolean activated = false;

    public TileTop(float rot, String block) {
        this.blockRotate = rot;
        this.block = block;
//        try {
//            save(new TagCompound().setString("blockName", block));
//        } catch (SerializationException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public ItemStack onPick() {
        return new ItemStack(Static.listTopModels.get(block)._3(), 1);
    }

    @Override
    public IBoundingBox getBoundingBox() {
//        return IBoundingBox.ORIGIN.expand(new Vec3d(fullWidth, fullHeight, fullLength)).offset(new Vec3d(0.5 - fullWidth / 2, 0, 0.5 - fullLength / 2));
        return IBoundingBox.BLOCK;
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

    public String getBlock() {
        return block;
    }
}
