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

import javax.annotation.Nullable;
import java.util.List;

@SuppressWarnings("java:S116")
public class TileSignalBox extends BlockEntity {

    @TagField("UuidTileTop")
    private Vec3i TileSignalPartPos;

    @TagField("redstone")
    private int redstone = 0;
    @TagField("noRedstone")
    private int noRedstone = 0;

    @Nullable
    private Integer lastRedstone = null;

    private TileSignalPart tileSignalPart;
    private TileSignalPartAnimated tileSignalPartAnimated;

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
            TileSignalPart tempSignalPart = player.getWorld().getBlockEntity(TileSignalPartPos, TileSignalPart.class);
            TileSignalPartAnimated tempSignalPartAnimated = player.getWorld().getBlockEntity(TileSignalPartPos, TileSignalPartAnimated.class);
            if (tempSignalPart != null) {
                new SignalBoxTileSignalPartPacket(tempSignalPart, getPos()).sendToPlayer(player);
                return true;
            } else if (tempSignalPartAnimated != null) {
                new SignalBoxTileSignalPartPacket(tempSignalPartAnimated, getPos()).sendToPlayer(player);
                return true;
            }
        }
        return false;
    }

    @Override
    public void onNeighborChange(Vec3i neighbor) {
        if (lastRedstone != null && lastRedstone == getWorld().getRedstone(getPos())) {
            return;
        } else {
            lastRedstone = getWorld().getRedstone(getPos());
        }

        if (getWorld().isServer && TileSignalPartPos != null) {
            TileSignalPart tempTileSignalPart = getWorld().getBlockEntity(TileSignalPartPos, TileSignalPart.class);
            if (tempTileSignalPart != null) {
                List<String> states = LOSBlocks.BLOCK_SIGNAL_PART.getStates(tempTileSignalPart.getId());
                if (redstone >= states.size() || noRedstone >= states.size()) {
                    redstone = 0;
                    noRedstone = 0;
                }
                if (getWorld().getRedstone(getPos()) > 0)
                    //Redstone
                    tempTileSignalPart.setTexturePath(states.get(redstone));
                else
                    //No redstone
                    tempTileSignalPart.setTexturePath(states.get(noRedstone));
            } else if (TileSignalPartPos != null) {
                TileSignalPartAnimated tempAnimatedPart = getWorld().getBlockEntity(TileSignalPartPos, TileSignalPartAnimated.class);
                if (tempAnimatedPart != null) {
                    List<String> states = LOSBlocks.BLOCK_SIGNAL_PART_ANIMATED.getStates(tempAnimatedPart.getId());
                    if (getWorld().getRedstone(getPos()) > 0) {
                        //Redstone
                        String temp;
                        if (redstone < states.size()) temp = states.get(redstone);
                        else
                            temp = LOSBlocks.BLOCK_SIGNAL_PART_ANIMATED.getAniamtions(tempAnimatedPart.getId()).keySet().toArray(new String[0])[redstone - states.size()];
                        tempAnimatedPart.setAnimationOrStateTexture(temp);
                    } else {
                        //No redstone
                        String temp;
                        if (noRedstone < states.size()) temp = states.get(noRedstone);
                        else
                            temp = LOSBlocks.BLOCK_SIGNAL_PART_ANIMATED.getAniamtions(tempAnimatedPart.getId()).keySet().toArray(new String[0])[noRedstone - states.size()];
                        tempAnimatedPart.setAnimationOrStateTexture(temp);
                    }
                }
            }
        }
    }

    @Override
    public void onBreak() {
        super.onBreak();
        if (TileSignalPartPos != null) {
            TileSignalPart entity = getWorld().getBlockEntity(TileSignalPartPos, TileSignalPart.class);
            if (entity != null) {
                entity.setTexturePath(LOSBlocks.BLOCK_SIGNAL_PART.getStates(entity.getId()).get(0));
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

    public TileSignalPartAnimated getTileSignalPartAnimated() {
        return tileSignalPartAnimated;
    }

    public void setTileSignalPartAnimated(TileSignalPartAnimated tileSignalPartAnimated) {
        this.tileSignalPartAnimated = tileSignalPartAnimated;
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
