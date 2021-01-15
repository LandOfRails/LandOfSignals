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

import java.util.List;
import java.util.UUID;

@SuppressWarnings("java:S116")
public class TileSignalBox extends BlockEntity {

    @TagField("UuidTileTop")
    private UUID UUIDTileSignalPart;

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
        if (player.getWorld().isClient && UUIDTileSignalPart != null && Static.changingSignalPartList.containsKey(UUIDTileSignalPart)) {
            LOSGuis.SIGNAL_BOX.open(player, getPos());
            return true;
        } else return false;
    }

    @Override
    public void onNeighborChange(Vec3i neighbor) {
        if (getWorld().isServer && UUIDTileSignalPart != null && Static.changingSignalPartList.containsKey(UUIDTileSignalPart)) {
            TileSignalPart entity = getWorld().getBlockEntity(Static.changingSignalPartList.get(UUIDTileSignalPart), TileSignalPart.class);
            if (entity != null) {
                List<String> states = entity.getBlock().getStates();
                if (redstone >= states.size() || noRedstone >= states.size()) {
                    redstone = 0;
                    noRedstone = 0;
                }
                if (getWorld().getRedstone(neighbor) > 0)
                    //Redstone
                    entity.setTexturePath(states.get(redstone));
                else
                    //No redstone
                    entity.setTexturePath(states.get(noRedstone));
            }
        }
    }

    public void setUUID(UUID uuid) {
        this.UUIDTileSignalPart = uuid;
        markDirty();
    }

    public UUID getUUIDTileSignalPart() {
        return UUIDTileSignalPart;
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
