package net.landofrails.landofsignals.tile;

import cam72cam.mod.block.BlockEntityTickable;
import cam72cam.mod.entity.boundingbox.IBoundingBox;
import cam72cam.mod.item.ItemStack;
import cam72cam.mod.math.Vec3d;
import cam72cam.mod.serialization.TagCompound;
import cam72cam.mod.serialization.TagField;
import net.landofrails.landofsignals.LOSBlocks;
import net.landofrails.landofsignals.LOSItems;
import net.landofrails.landofsignals.utils.AnimationHandler;
import net.landofrails.landofsignals.utils.IManipulate;

public class TileSignalPartAnimated extends BlockEntityTickable implements IManipulate {

    @TagField("blockRotation")
    private int blockRotate;
    @TagField("id")
    private String id;
    @TagField("texturePath")
    private String animationOrTextureName;

    @TagField("offset")
    private Vec3d offset = Vec3d.ZERO;

    @TagField
    private boolean active;
    private float partRotate;

    AnimationHandler animationHandler;

    public TileSignalPartAnimated(final String id, final int rot) {
        blockRotate = rot;
        this.id = id;
        if (id != null)
            animationHandler = new AnimationHandler(LOSBlocks.BLOCK_SIGNAL_PART_ANIMATED.getAniamtions(id));
    }

    @Override
    public void update() {
        if (active && partRotate <= LOSBlocks.BLOCK_SIGNAL_PART_ANIMATED.getAnimation(id, "wing1").get(0).getRotate()[0]) {
            partRotate++;
        } else if (!active && partRotate >= 0) {
            partRotate--;
        }
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
        return IBoundingBox.BLOCK;
    }

    @Override
    public IBoundingBox getRenderBoundingBox() {
        return IBoundingBox.BLOCK.offset(offset);
    }

    public int getBlockRotate() {
        return blockRotate;
    }

    public String getAnimationOrTextureName() {
        if ("null".equals(animationOrTextureName)) return null;
        else return animationOrTextureName;
    }

    public String getId() {
        return id;
    }

    @SuppressWarnings("unused")
    public void setAnimationOrStateTexture(final String name) {
        if (name == null) animationOrTextureName = "null";
        else animationOrTextureName = name;
        markDirty();
    }

    @Override
    public void setOffset(final Vec3d vec) {
        offset = vec;
        try {
            save(new TagCompound().setVec3d("offset", vec));
        } catch (final Exception e) {
            // Nothing you can do now.
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
            // Nothing you can do now.
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

    @SuppressWarnings("unused")
    public void setActive(final boolean active) {
        this.active = active;
        markDirty();
    }

    public float getPartRotate() {
        return partRotate;
    }
}
