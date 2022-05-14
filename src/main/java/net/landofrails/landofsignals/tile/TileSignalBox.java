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
    private Vec3i tileSignalPartPos;

    /**
     * TODO Remove
     */
    @Deprecated
    @TagField("redstone")
    private int redstone = 0;
    @Deprecated
    @TagField("noRedstone")
    private int noRedstone = 0;
    /**
     * ^^^
     */

    @TagField("signalGroupId")
    private String groupId;

    @TagField("activeGroupState")
    private String activeGroupState;

    @TagField("inactiveGroupState")
    private String inactiveGroupState;

    @Nullable
    private Integer lastRedstone = null;

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
        if (!player.getHeldItem(hand).is(LOSItems.ITEM_CONNECTOR) && !player.isCrouching() && player.getWorld().isServer && tileSignalPartPos != null) {
            TileSignalPart tempSignalPart = player.getWorld().getBlockEntity(tileSignalPartPos, TileSignalPart.class);
            if (tempSignalPart != null) {
                new SignalBoxTileSignalPartPacket(tempSignalPart, getPos()).sendToPlayer(player);
                return true;
            }
        }
        return false;
    }

    @Override
    public void onNeighborChange(Vec3i neighbor) {
        int currentRedstone = getWorld().getRedstone(getPos());
        if (lastRedstone != null && lastRedstone == currentRedstone) {
            return;
        } else {
            lastRedstone = currentRedstone;
        }

        if (getWorld().isServer && tileSignalPartPos != null) {
            TileSignalPart tempTileSignalPart = getWorld().getBlockEntity(tileSignalPartPos, TileSignalPart.class);
            if (tempTileSignalPart != null) {

                String groupState = currentRedstone > 0 ? activeGroupState : inactiveGroupState;
                tempTileSignalPart.updateSignals(getPos(), groupId, groupState);

                // Old
                List<String> states = LOSBlocks.BLOCK_SIGNAL_PART.getStates_depr(tempTileSignalPart.getId());
                if (redstone >= states.size() || noRedstone >= states.size()) {
                    redstone = 0;
                    noRedstone = 0;
                }
                if (getWorld().getRedstone(getPos()) > 0)
                    //Redstone
                    tempTileSignalPart.setTexturePath_depr(states.get(redstone));
                else
                    //No redstone
                    tempTileSignalPart.setTexturePath_depr(states.get(noRedstone));
            }
        }
    }

    @Override
    public void onBreak() {
        if (tileSignalPartPos == null) {
            return;
        }
        
        if (!getWorld().isBlockLoaded(tileSignalPartPos)) {
            getWorld().keepLoaded(tileSignalPartPos);
        }
        TileSignalPart entity = getWorld().getBlockEntity(tileSignalPartPos, TileSignalPart.class);
        if (entity != null) {
            entity.removeSignal(getPos());
            // TODO Remove old
            entity.setTexturePath_depr(LOSBlocks.BLOCK_SIGNAL_PART.getStates_depr(entity.getId()).get(0));
        }
    }

    public void setTileSignalPartPos(Vec3i pos) {
        tileSignalPartPos = pos;
        markDirty();
    }

    public void setTileSignalPart(TileSignalPart tileSignalPart) {
        this.tileSignalPart = tileSignalPart;
    }

    public TileSignalPart getTileSignalPart() {
        return tileSignalPart;
    }

    @Deprecated
    public int getRedstone() {
        return redstone;
    }

    @Deprecated
    public void setRedstone(int redstone) {
        this.redstone = redstone;
    }

    @Deprecated
    public int getNoRedstone() {
        return noRedstone;
    }

    @Deprecated
    public void setNoRedstone(int noRedstone) {
        this.noRedstone = noRedstone;
    }

    /**
     * @return groupId
     */
    public String getGroupId(String... defaultValue) {
        return groupId != null || defaultValue.length == 0 ? groupId : defaultValue[0];
    }

    /**
     * @param groupId GroupId
     */
    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    /**
     * @return activeGroupState
     */
    public String getActiveGroupState(String... defaultValue) {
        return activeGroupState != null || defaultValue.length == 0 ? activeGroupState : defaultValue[0];
    }

    /**
     * @param activeGroupState Active groupstate
     */
    public void setActiveGroupState(String activeGroupState) {
        this.activeGroupState = activeGroupState;
    }

    /**
     * @return inactiveGroupState
     */
    public String getInactiveGroupState(String... defaultValue) {
        return inactiveGroupState != null || defaultValue.length == 0 ? inactiveGroupState : defaultValue[0];
    }

    /**
     * @param inactiveGroupState Inactive groupstate
     */
    public void setInactiveGroupState(String inactiveGroupState) {
        this.inactiveGroupState = inactiveGroupState;
    }

    /**
     * server-only
     *
     * @param refreshSignalBoxSignal Refreshes signalbox signal
     */
    public void updateSignals(boolean refreshSignalBoxSignal) {

        TileSignalPart tile = getWorld().getBlockEntity(tileSignalPartPos, TileSignalPart.class);
        if (refreshSignalBoxSignal) {
            String groupState = getWorld().getRedstone(getPos()) > 0 ? activeGroupState : inactiveGroupState;
            tile.updateSignals(getPos(), groupId, groupState);
        } else {
            tile.updateSignals();
        }
    }
}
