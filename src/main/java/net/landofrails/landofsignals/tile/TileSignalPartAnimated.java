package net.landofrails.landofsignals.tile;

import cam72cam.mod.block.BlockEntityTickable;
import cam72cam.mod.entity.boundingbox.IBoundingBox;
import cam72cam.mod.item.ItemStack;
import cam72cam.mod.math.Vec3d;
import cam72cam.mod.serialization.TagCompound;
import cam72cam.mod.serialization.TagField;
import net.landofrails.landofsignals.LOSBlocks;
import net.landofrails.landofsignals.LOSItems;
import net.landofrails.landofsignals.utils.IManipulate;

public class TileSignalPartAnimated extends BlockEntityTickable implements IManipulate {

    @TagField("blockRotation")
    private int blockRotate;
    @TagField("id")
    private final String id;
    @TagField("texturePath")
    private String texturePath = null;

    @TagField("offset")
    private Vec3d offset = Vec3d.ZERO;

    @TagField
    private boolean active;
    private float partRotate = 0;

    public TileSignalPartAnimated(String id, int rot) {
        this.blockRotate = rot;
        this.id = id;
    }

    @Override
    public void update() {
        if (active && partRotate <= LOSBlocks.BLOCK_SIGNAL_PART_ANIMATED.getAnimation(id, "wing1").get(0).getRotate()) {
            partRotate++;
        } else if (!active && partRotate >= 0) {
            partRotate--;
        }
    }

    @Override
    public ItemStack onPick() {
        ItemStack is = new ItemStack(LOSItems.ITEM_SIGNAL_PART, 1);
        TagCompound tag = is.getTagCompound();
        tag.setString("itemId", id);
        is.setTagCompound(tag);
        return is;
    }

    @Override
    public IBoundingBox getBoundingBox() {
        return IBoundingBox.BLOCK.offset(offset);
    }

    @Override
    public IBoundingBox getRenderBoundingBox() {
        return IBoundingBox.BLOCK.offset(offset);
    }

    public int getBlockRotate() {
        return blockRotate;
    }

    public String getTexturePath() {
        if (texturePath != null && texturePath.equals("null")) return null;
        else return texturePath;
    }

    public String getId() {
        return id;
    }

    public void setTexturePath(String texturePath) {
        if (texturePath == null) this.texturePath = "null";
        else this.texturePath = texturePath;
        markDirty();
    }

    @Override
    public void setOffset(Vec3d vec) {
        offset = vec;
        try {
            save(new TagCompound().setVec3d("offset", vec));
        } catch (Exception e) {

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
        } catch (Exception e) {

        }
    }

    @Override
    public int getRotation() {
        return blockRotate;
    }

    public void setActive(boolean active) {
        this.active = active;
        markDirty();
    }

    public float getPartRotate() {
        return partRotate;
    }
}
