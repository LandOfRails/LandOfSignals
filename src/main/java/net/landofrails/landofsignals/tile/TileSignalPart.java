package net.landofrails.landofsignals.tile;

import cam72cam.mod.block.BlockEntity;
import cam72cam.mod.item.ItemStack;
import cam72cam.mod.math.Vec3i;
import cam72cam.mod.serialization.SerializationException;
import cam72cam.mod.serialization.TagCompound;
import cam72cam.mod.serialization.TagField;
import net.landofrails.landofsignals.blocks.BlockSignalPart;
import net.landofrails.landofsignals.utils.Static;

public class TileSignalPart extends BlockEntity {

    @TagField("blockRotation")
    private int blockRotate;
    @TagField("block")
    private String blockName;
    @TagField("texturePath")
    private String texturePath = null;

    @TagField("pos")
    private final Vec3i pos;
    @TagField("UUID")
    private java.util.UUID UUID = java.util.UUID.randomUUID();

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
        this.UUID = nbt.getUUID("UUID");
        if (block.getStates().size() > 1)
            Static.changingSignalPartList.put(this.UUID, this.pos);
    }

    @Override
    public ItemStack onPick() {
        return new ItemStack(block.getItem(), 1);
    }

    @Override
    public void onBreak() {
        super.onBreak();
        if (block.getStates().size() > 1)
            Static.changingSignalPartList.remove(UUID);
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

    public java.util.UUID getUUID() {
        return UUID;
    }
}
