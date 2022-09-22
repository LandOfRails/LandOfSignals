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
import net.landofrails.landofsignals.utils.Static;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SuppressWarnings("java:S116")
public class TileSignalBox extends BlockEntity {

    @TagField("id")
    private String id;

    @TagField("blockRotation")
    private int blockRotate;

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
     * <p>
     * FIXME Can I read this in the NBT on TileSignalBox#load()?
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

    public TileSignalBox(String id, int rot) {
        this.id = id;
        this.blockRotate = rot;
    }

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

    public String getId() {
        return this.id;
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
    public Integer getRedstone() {
        return redstone;
    }

    @Deprecated
    public void setRedstone(Integer redstone) {
        this.redstone = redstone;
    }

    @Deprecated
    public Integer getNoRedstone() {
        return noRedstone;
    }

    @Deprecated
    public void setNoRedstone(Integer noRedstone) {
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

    public int getBlockRotate() {
        return blockRotate;
    }

    @Override
    public void load(TagCompound nbt) throws SerializationException {

        final String KEYWORD_ID = "id";

        if (!nbt.hasKey(KEYWORD_ID)) {
            nbt.setString(KEYWORD_ID, Static.SIGNALBOX_DEFAULT_FQ);
        } else if (!nbt.getString(KEYWORD_ID).contains(":")) {
            String oldId = nbt.getString(KEYWORD_ID);
            List<String> possibleIds = LOSBlocks.BLOCK_SIGNAL_BOX.getContentpackSignalboxes().keySet().stream().filter(iterationId -> iterationId.endsWith(oldId)).collect(Collectors.toList());
            String newId = Static.MISSING;
            if (possibleIds.size() == 1) {
                newId = possibleIds.get(0);
            }
            nbt.setString(KEYWORD_ID, newId);
        }
    }
}
