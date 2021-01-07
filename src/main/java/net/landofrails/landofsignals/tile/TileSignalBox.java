package net.landofrails.landofsignals.tile;

import cam72cam.mod.block.BlockEntity;
import cam72cam.mod.entity.Player;
import cam72cam.mod.entity.boundingbox.IBoundingBox;
import cam72cam.mod.item.ItemStack;
import cam72cam.mod.math.Vec3d;
import cam72cam.mod.math.Vec3i;
import cam72cam.mod.serialization.TagField;
import cam72cam.mod.util.Facing;
import net.landofrails.landofsignals.LOSGuis;
import net.landofrails.landofsignals.LOSItems;
import net.landofrails.landofsignals.utils.Static;

import java.util.UUID;

public class TileSignalBox extends BlockEntity {

    @TagField("UuidTileTop")
    private UUID UUIDTileTop;

    @TagField("redstone")
    private int redstone = 0;
    @TagField("noRedstone")
    private int noRedstone = 0;

    @Override
    public ItemStack onPick() {
        return new ItemStack(LOSItems.ITEM_SIGNAL_BOX, 1);
    }

    @Override
    public IBoundingBox getBoundingBox() {
        return IBoundingBox.BLOCK;
    }

    @Override
    public boolean onClick(Player player, Player.Hand hand, Facing facing, Vec3d hit) {
        if (UUIDTileTop != null) {
            LOSGuis.SIGNAL_BOX.open(player, getPos());
            return true;
        } else return false;
    }

    @Override
    public void onNeighborChange(Vec3i neighbor) {
        if (UUIDTileTop != null) {
            TileTop entity = getWorld().getBlockEntity(Static.listTopBlocks.get(UUIDTileTop), TileTop.class);
            if (entity != null) {
                if (getWorld().getRedstone(neighbor) > 0) {
                    //Redstone
                    entity.setTexturePath(Static.listTopModels.get(entity.getBlock())._4().get(redstone));
                } else {
                    //No redstone
                    entity.setTexturePath(Static.listTopModels.get(entity.getBlock())._4().get(noRedstone));
                }
            } else {
                System.out.println(Static.listTopBlocks.get(UUIDTileTop).toString());
            }
        }
    }

    public void setUUID(UUID uuid) {
        this.UUIDTileTop = uuid;
        markDirty();
    }

    public UUID getUUIDTileTop() {
        return UUIDTileTop;
    }

    public int getRedstone() {
        return redstone;
    }

    public void setRedstone(int redstone) {
        this.redstone = redstone;
    }

    public int getNoRedstone() {
        return noRedstone;
    }

    public void setNoRedstone(int noRedstone) {
        this.noRedstone = noRedstone;
    }
}
