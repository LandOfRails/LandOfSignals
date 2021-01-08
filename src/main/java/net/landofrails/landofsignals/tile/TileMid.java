package net.landofrails.landofsignals.tile;

import cam72cam.mod.block.BlockEntity;
import cam72cam.mod.entity.boundingbox.IBoundingBox;
import cam72cam.mod.item.ItemStack;
import cam72cam.mod.serialization.TagField;
import net.landofrails.landofsignals.utils.Static;

public class TileMid extends BlockEntity {

    @TagField("blockRotation")
    private float blockRotate;
    @TagField("blockName")
    private String block;

    public TileMid(float rot, String block) {
        this.blockRotate = rot;
        this.block = block;
    }

    @Override
    public ItemStack onPick() {
        return new ItemStack(Static.listMidModels.get(block)._3(), 1);
    }

    @Override
    public IBoundingBox getBoundingBox() {
        return IBoundingBox.BLOCK;
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
