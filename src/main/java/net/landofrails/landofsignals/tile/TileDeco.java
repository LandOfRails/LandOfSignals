package net.landofrails.landofsignals.tile;

import cam72cam.mod.block.BlockEntity;
import cam72cam.mod.entity.boundingbox.IBoundingBox;
import cam72cam.mod.item.ItemStack;
import cam72cam.mod.math.Vec3d;
import cam72cam.mod.serialization.TagCompound;
import cam72cam.mod.serialization.TagField;
import net.landofrails.landofsignals.LOSItems;
import net.landofrails.landofsignals.utils.IManipulate;

public class TileDeco extends BlockEntity implements IManipulate {

    @TagField("blockRotation")
    private int blockRotate;
    @TagField("id")
    private String id;
    @TagField("offset")
    private Vec3d offset = Vec3d.ZERO;

    public TileDeco(String id, int rot) {
        this.blockRotate = rot;
        this.id = id;
    }

    @Override
    public ItemStack onPick() {
        ItemStack is = new ItemStack(LOSItems.ITEM_DECO, 1);
        TagCompound tag = is.getTagCompound();
        tag.setString("itemId", id);
        is.setTagCompound(tag);
        return is;
    }

    @Override
    public IBoundingBox getBoundingBox() {
        return IBoundingBox.BLOCK;
    }

    @Override
    public IBoundingBox getRenderBoundingBox() {
        return IBoundingBox.BLOCK.offset(offset);
    }

    public int getBlockRotate() {
        return blockRotate;
    }

    public String getId() {
        return id;
    }


    @Override
    public void setOffset(Vec3d vec) {
        offset = vec;
        try {
            save(new TagCompound().setVec3d("offset", vec));
        } catch (Exception ignored) {
            // Can be ignored
        }
    }

    @Override
    public Vec3d getOffset() {
        return offset;
    }

    @Override
    public void setRotation(int rotation) {
        this.blockRotate = rotation;
        try {
            save(new TagCompound().setInteger("blockRotation", rotation));
        } catch (Exception ignored) {
            // Can be ignored
        }
    }

    @Override
    public int getRotation() {
        return getBlockRotate();
    }

    @Override
    public void setScaling(Vec3d scaling) {
        throw new UnsupportedOperationException("Not yet implemented.");
    }

    @Override
    public Vec3d getScaling() {
        return new Vec3d(1, 1, 1);
    }

}
