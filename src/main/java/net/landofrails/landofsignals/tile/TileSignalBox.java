package net.landofrails.landofsignals.tile;

import cam72cam.mod.block.BlockEntity;
import cam72cam.mod.entity.Player;
import cam72cam.mod.entity.boundingbox.IBoundingBox;
import cam72cam.mod.item.ItemStack;
import cam72cam.mod.math.Vec3d;
import cam72cam.mod.math.Vec3i;
import cam72cam.mod.serialization.SerializationException;
import cam72cam.mod.serialization.TagCompound;
import cam72cam.mod.serialization.TagField;
import cam72cam.mod.util.Facing;
import net.landofrails.api.contentpacks.v2.signal.ContentPackSignalGroup;
import net.landofrails.landofsignals.LOSBlocks;
import net.landofrails.landofsignals.LOSItems;
import net.landofrails.landofsignals.packet.SignalBoxTileSignalPartPacket;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.Map;

@SuppressWarnings("java:S116")
public class TileSignalBox extends BlockEntity {

    @TagField("UuidTileTop")
    private Vec3i tileSignalPartPos;

    @TagField("signalGroupId")
    private String groupId;

    @TagField("activeGroupState")
    private String activeGroupState;

    @TagField("inactiveGroupState")
    private String inactiveGroupState;

    /**
     * Needs to stay for backwards compatibility
     */
    @TagField("redstone")
    private Integer redstone = null;
    @TagField("noRedstone")
    private Integer noRedstone = null;
    /**
     * ^^^
     */

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

                if (redstone != null && noRedstone != null) {
                    Map.Entry<String, ContentPackSignalGroup> group = LOSBlocks.BLOCK_SIGNAL_PART.getAllGroupStates(tempTileSignalPart.getId()).entrySet().iterator().next();
                    groupId = group.getKey();
                    Iterator<String> stateIdIterator = group.getValue().getStates().keySet().iterator();
                    int index = 0;
                    while (stateIdIterator.hasNext()) {
                        String stateId = stateIdIterator.next();
                        if (index == redstone) {
                            activeGroupState = stateId;
                        }
                        if (index == noRedstone) {
                            inactiveGroupState = stateId;
                        }
                        index++;
                    }
                    redstone = null;
                    noRedstone = null;
                }

                String groupState = currentRedstone > 0 ? activeGroupState : inactiveGroupState;
                tempTileSignalPart.updateSignals(getPos(), groupId, groupState);

                // Old
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

        if (!getWorld().isBlockLoaded(tileSignalPartPos))
            getWorld().keepLoaded(tileSignalPartPos);

        TileSignalPart tile = getWorld().getBlockEntity(tileSignalPartPos, TileSignalPart.class);
        if (refreshSignalBoxSignal) {
            String groupState = getWorld().getRedstone(getPos()) > 0 ? activeGroupState : inactiveGroupState;
            tile.updateSignals(getPos(), groupId, groupState);
        } else {
            tile.updateSignals();
        }
    }

    @Override
    public void load(TagCompound nbt) throws SerializationException {

        // TODO Decide what to do.
        // redstone and noRedstone is available
        // Signal can be available, not safe though

        super.load(nbt);
    }
}
