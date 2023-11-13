package net.landofrails.landofsignals.tile;

import cam72cam.mod.block.BlockEntity;
import cam72cam.mod.block.IRedstoneProvider;
import cam72cam.mod.entity.Player;
import cam72cam.mod.entity.boundingbox.IBoundingBox;
import cam72cam.mod.item.ItemStack;
import cam72cam.mod.math.Vec3d;
import cam72cam.mod.serialization.TagCompound;
import cam72cam.mod.serialization.TagField;
import cam72cam.mod.util.Facing;
import net.landofrails.landofsignals.LOSBlocks;
import net.landofrails.landofsignals.LOSItems;
import net.landofrails.landofsignals.utils.IManipulate;

public class TileCustomLever extends BlockEntity implements IManipulate, IRedstoneProvider {

    @TagField("blockRotation")
    private int blockRotate;
    @TagField("id")
    private String id;
    @TagField("offset")
    private Vec3d offset = Vec3d.ZERO;
    @TagField("scaling")
    private Vec3d scaling = new Vec3d(1,1,1);

    @TagField("active")
    private boolean active = false;

    public TileCustomLever(String id, int rot) {
        this.blockRotate = rot;
        this.id = id;
    }

    @Override
    public boolean onClick(Player player, Player.Hand hand, Facing facing, Vec3d hit) {
        active = !active;
        // Updates the own coordinate
        getWorld().notifyNeighborsOfStateChange(getPos(), LOSBlocks.BLOCK_CUSTOM_LEVER, true);
        // Update coordinate below
        getWorld().notifyNeighborsOfStateChange(getPos().offset(Facing.DOWN), LOSBlocks.BLOCK_CUSTOM_LEVER, true);
        markDirty();
        return true;
    }

    @Override
    public ItemStack onPick() {
        ItemStack is = new ItemStack(LOSItems.ITEM_CUSTOM_LEVER, 1);
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
        this.scaling = scaling;
    }

    @Override
    public Vec3d getScaling() {
        return scaling;
    }

    @Override
    public int getStrongPower(final Facing from) {
        return active ? 15 : 0;
    }

    @Override
    public int getWeakPower(final Facing from) {
        return active ? 15 : 0;
    }

    public boolean isActive(){
        return active;
    }
}
