package net.landofrails.landofsignals.tile;

import cam72cam.mod.block.BlockEntity;
import cam72cam.mod.entity.boundingbox.IBoundingBox;
import cam72cam.mod.item.ItemStack;
import cam72cam.mod.math.Vec3d;
import cam72cam.mod.serialization.TagCompound;
import cam72cam.mod.serialization.TagField;
import net.landofrails.landofsignals.LOSItems;
import net.landofrails.landofsignals.utils.IManipulate;

public class TileSignalPart extends BlockEntity implements IManipulate {

    @TagField("blockRotation")
    private int blockRotate;
    @TagField("id")
    private final String id;
    @TagField("texturePath")
    private String texturePath;

    @TagField("offset")
    private Vec3d offset = Vec3d.ZERO;

    public TileSignalPart(final String id, final int rot) {
        blockRotate = rot;
        this.id = id;
    }

    @Override
    public ItemStack onPick() {
        final ItemStack is = new ItemStack(LOSItems.ITEM_SIGNAL_PART, 1);
        final TagCompound tag = is.getTagCompound();
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
        if ("null".equals(texturePath)) return null;
        else return texturePath;
    }

    public String getId() {
        return id;
    }

    public void setTexturePath(final String texturePath) {
        if (texturePath == null) this.texturePath = "null";
        else this.texturePath = texturePath;
        markDirty();
    }

    @Override
    public void setOffset(final Vec3d vec) {
        offset = vec;
        try {
            save(new TagCompound().setVec3d("offset", vec));
        } catch (final Exception e) {

        }
    }

    @Override
    public Vec3d getOffset() {
        return offset;
    }

    @Override
    public void setRotation(final int rotation) {
        blockRotate = rotation;
        try {
            save(new TagCompound().setInteger("blockRotation", rotation));
        } catch (final Exception e) {

        }
    }

    @Override
    public int getRotation() {
        return blockRotate;
    }
}
