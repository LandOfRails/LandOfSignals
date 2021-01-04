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
        LOSGuis.SIGNAL_BOX.open(player);
        return true;
    }

    @Override
    public void onNeighborChange(Vec3i neighbor) {
        if (UUIDTileTop != null) {
            TileTop entity = Static.listTopBlocks.get(UUIDTileTop);
            if (getWorld().getRedstone(neighbor) != 0) {
                entity.setTexturePath(Static.listTopModels.get(entity.getBlock())._4().get("GREEN"));
            } else {
                entity.setTexturePath(Static.listTopModels.get(entity.getBlock())._4().get("standard"));
            }
        }
    }

    public void setUUID(UUID uuid) {
        this.UUIDTileTop = uuid;
    }
}
