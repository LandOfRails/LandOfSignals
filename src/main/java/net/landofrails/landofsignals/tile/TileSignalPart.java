package net.landofrails.landofsignals.tile;

import cam72cam.mod.block.BlockEntity;
import cam72cam.mod.entity.boundingbox.IBoundingBox;
import cam72cam.mod.item.ItemStack;
import cam72cam.mod.math.Vec3d;
import cam72cam.mod.math.Vec3i;
import cam72cam.mod.serialization.SerializationException;
import cam72cam.mod.serialization.TagCompound;
import cam72cam.mod.serialization.TagField;
import net.landofrails.landofsignals.blocks.BlockSignalPart;
import net.landofrails.landofsignals.utils.IManipulate;
import net.landofrails.landofsignals.utils.Static;

public class TileSignalPart extends BlockEntity implements IManipulate {

    @TagField("blockRotation")
    private int blockRotate;
    @TagField("block")
    private final String blockName;
    @TagField("texturePath")
    private String texturePath = null;

    @TagField("offset")
    private Vec3d offset = Vec3d.ZERO;

    @TagField("pos")
    private final Vec3i pos;

    private BlockSignalPart block;

    public TileSignalPart(int rotation, String blockName, Vec3i pos) {
        this.blockRotate = rotation;
        this.blockName = blockName;
        this.pos = pos;
        block = Static.blockSignalPartList.get(blockName);
    }

    @Override
    public void load(TagCompound nbt) throws SerializationException {
        super.load(nbt);
        this.block = Static.blockSignalPartList.get(blockName);
    }

    @Override
    public ItemStack onPick() {
        return new ItemStack(block.getItem(), 1);
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

    public BlockSignalPart getBlock() {
        return block;
    }

    public String getTexturePath() {
        if (texturePath != null && texturePath.equals("null")) return null;
        else return texturePath;
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
}
