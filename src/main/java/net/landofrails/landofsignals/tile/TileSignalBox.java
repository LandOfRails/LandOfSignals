package net.landofrails.landofsignals.tile;

import cam72cam.mod.block.BlockEntity;
import cam72cam.mod.entity.Player;
import cam72cam.mod.entity.boundingbox.IBoundingBox;
import cam72cam.mod.item.ItemStack;
import cam72cam.mod.math.Vec3d;
import cam72cam.mod.math.Vec3i;
import cam72cam.mod.serialization.TagField;
import cam72cam.mod.util.Facing;
import net.landofrails.landofsignals.LOSBlocks;
import net.landofrails.landofsignals.LOSItems;
import net.landofrails.landofsignals.packet.SignalBoxTileSignalPartPacket;

import java.util.List;

@SuppressWarnings("java:S116")
public class TileSignalBox extends BlockEntity {

    @TagField("UuidTileTop")
    private Vec3i TileSignalPartPos;

    @TagField("redstone")
    private int redstone = 0;
    @TagField("noRedstone")
    private int noRedstone = 0;

    private TileSignalPart tileSignalPart;

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
        if (!player.getHeldItem(hand).is(LOSItems.ITEM_CONNECTOR) && !player.isCrouching() && player.getWorld().isServer && TileSignalPartPos != null) {
            new SignalBoxTileSignalPartPacket(player.getWorld().getBlockEntity(TileSignalPartPos, TileSignalPart.class), getPos()).sendToAllAround(player.getWorld(), player.getPosition(), 1);
            return true;
        } else return false;
    }

    @Override
    public void onNeighborChange(Vec3i neighbor) {
        if (getWorld().isServer && TileSignalPartPos != null) {
            TileSignalPart entity = getWorld().getBlockEntity(TileSignalPartPos, TileSignalPart.class);
            if (entity != null) {
                List<String> states = LOSBlocks.BLOCK_SIGNAL_PART.getStates(entity.getId());
                if (redstone >= states.size() || noRedstone >= states.size()) {
                    redstone = 0;
                    noRedstone = 0;
                }
                if (getWorld().getRedstone(getPos()) > 0)
                    //Redstone
                    entity.setTexturePath(states.get(redstone));
                else
                    //No redstone
                    entity.setTexturePath(states.get(noRedstone));
            }
        }
    }

    public void setTileSignalPartPos(Vec3i pos) {
        this.TileSignalPartPos = pos;
        markDirty();
    }

    public void setTileSignalPart(TileSignalPart tileSignalPart) {
        this.tileSignalPart = tileSignalPart;
    }

    public TileSignalPart getTileSignalPart() {
        return tileSignalPart;
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
