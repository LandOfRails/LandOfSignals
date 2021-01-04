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
    @TagField("texturePath")
    private String texturePath = null;
    @TagField("UUID")
    private java.util.UUID UUID = java.util.UUID.randomUUID();

    private boolean activated = false;

    public TileTop(float rot, String block) {
        this.blockRotate = rot;
        this.block = block;
        Static.listTopBlocks.put(this.UUID, this);
    }

//    @Override
//    public void load(TagCompound nbt) throws SerializationException {
//        this.UUID = nbt.getUUID("UUID");
//        this.block = nbt.getString("blockName");
//    }

    @Override
    public ItemStack onPick() {
        return new ItemStack(Static.listTopModels.get(block)._3(), 1);
    }

    @Override
    public IBoundingBox getBoundingBox() {
        return IBoundingBox.BLOCK;
    }

    public String getTexturePath() {
        return texturePath;
    }

    public void setTexturePath(String texturePath) {
        this.texturePath = texturePath;
        markDirty();
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

    public java.util.UUID getUUID() {
        return UUID;
    }
}
