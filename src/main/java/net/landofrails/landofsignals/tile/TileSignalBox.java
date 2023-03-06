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
import net.landofrails.landofsignals.LOSBlocks;
import net.landofrails.landofsignals.LOSItems;
import net.landofrails.landofsignals.packet.SignalBoxTileSignalPartPacket;
import net.landofrails.landofsignals.utils.Static;

import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings({"java:S116", "java:S1134", "java:S1133"})
public class TileSignalBox extends BlockEntity {

    @TagField("id")
    private String id;

    @TagField("blockRotation")
    private int blockRotate;

    @TagField("UuidTileTop")
    @Nullable
    private Vec3i tileSignalPartPos = Vec3i.ZERO;

    @TagField()
    @Nullable
    private Byte signalType; // null = none, 0 = simple (old), 1 = complex

    @TagField("signalGroupId")
    @Nullable
    private String groupId;

    @TagField("activeGroupState")
    private String activeGroupState;

    @TagField("inactiveGroupState")
    private String inactiveGroupState;


    @TagField("redstone")
    private Integer redstone = null; // stays for backwards compatability
    @TagField("noRedstone")
    private Integer noRedstone = null; // stays for backwards compatability

    @Nullable
    private Integer lastRedstone;

    private TileSignalPart tileSignalPart;
    private TileComplexSignal tileComplexSignal;

    public TileSignalBox(String id, int rot) {
        this.id = id;
        this.blockRotate = rot;
    }

    @Override
    public ItemStack onPick() {
        ItemStack is = new ItemStack(LOSItems.ITEM_SIGNAL_BOX, 1);
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
    public boolean onClick(Player player, Player.Hand hand, Facing facing, Vec3d hit) {
        if (!player.getHeldItem(hand).is(LOSItems.ITEM_CONNECTOR) && !player.isCrouching() && player.getWorld().isServer && tileSignalPartPos != null) {
            TileSignalPart tempSignalPart = player.getWorld().getBlockEntity(tileSignalPartPos, TileSignalPart.class);
            if (tempSignalPart != null) {
                refreshOldRedstoneVariables();
                new SignalBoxTileSignalPartPacket(tempSignalPart, this, getPos()).sendToPlayer(player);
                return true;
            }
        }
        return false;
    }

    @Override
    public void onNeighborChange(final Vec3i neighbor) {
        int currentRedstone = getWorld().getRedstone(getPos());
        if (lastRedstone != null && lastRedstone == currentRedstone) {
            return;
        } else {
            lastRedstone = currentRedstone;
        }

        if (tileSignalPartPos != null && signalType != null) {
            refreshOldRedstoneVariables();

            if (getWorld().isServer) {
                if (signalType == 0) {
                    final TileSignalPart tempTileSignalPart = getWorld().getBlockEntity(tileSignalPartPos, TileSignalPart.class);
                    if (tempTileSignalPart != null) {
                        String state = currentRedstone > 0 ? activeGroupState : inactiveGroupState;
                        tempTileSignalPart.updateSignals(getPos(), state);
                    }
                } else if (signalType == 1) {
                    final TileComplexSignal tempTileComplexSignal = getWorld().getBlockEntity(tileSignalPartPos, TileComplexSignal.class);
                    if (tempTileComplexSignal != null) {
                        String groupState = currentRedstone > 0 ? activeGroupState : inactiveGroupState;
                        tempTileComplexSignal.updateSignals(getPos(), groupId, groupState);
                    }
                }
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

    public void setTileSignalPartPos(final Vec3i pos) {
        tileSignalPartPos = pos;
        markDirty();
    }

    public void setTileSignalPart(final TileSignalPart tileSignalPart) {
        this.tileComplexSignal = null;
        this.tileSignalPart = tileSignalPart;
    }

    public TileSignalPart getTileSignalPart() {
        return tileSignalPart;
    }

    public void setTileComplexSignal(TileComplexSignal tileComplexSignal) {
        this.tileSignalPart = null;
        this.tileComplexSignal = tileComplexSignal;
    }

    public TileComplexSignal getTileComplexSignal() {
        return tileComplexSignal;
    }

    /**
     * @return old textureIndex for active redstone signal
     * @deprecated (1.0.0, Only here for backwards compatability)
     */
    @Deprecated
    public Integer getRedstone() {
        return redstone;
    }

    /**
     * @param redstone old texture-index
     * @deprecated (1.0.0, Only here for backwards compatability)
     */
    @Deprecated
    public void setRedstone(final Integer redstone) {
        this.redstone = redstone;
    }

    /**
     * @return old textureIndex for inactive redstone signal
     * @deprecated (1.0.0, Only here for backwards compatability)
     */
    @Deprecated
    public Integer getNoRedstone() {
        return noRedstone;
    }

    /**
     * @param noRedstone old texture-index
     * @deprecated (1.0.0, Only here for backwards compatability)
     */
    @Deprecated
    public void setNoRedstone(final Integer noRedstone) {
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

        if (signalType == null) {
            return;
        }

        if (signalType == 0) {
            TileSignalPart tile = getWorld().getBlockEntity(tileSignalPartPos, TileSignalPart.class);
            if (refreshSignalBoxSignal) {
                String state = getWorld().getRedstone(getPos()) > 0 ? activeGroupState : inactiveGroupState;
                tile.updateSignals(getPos(), state);
            } else {
                tile.updateSignals();
            }
        } else if (signalType == 1) {
            TileComplexSignal tile = getWorld().getBlockEntity(tileSignalPartPos, TileComplexSignal.class);
            if (refreshSignalBoxSignal) {
                String groupState = getWorld().getRedstone(getPos()) > 0 ? activeGroupState : inactiveGroupState;
                tile.updateSignals(getPos(), groupId, groupState);
            } else {
                tile.updateSignals();
            }
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

    private void refreshOldRedstoneVariables() {

        if (redstone == null || noRedstone == null) {
            return;
        }

        // Needs to be simple (old) signal
        final TileSignalPart tempTileSignalPart = getWorld().getBlockEntity(tileSignalPartPos, TileSignalPart.class);

        if (tempTileSignalPart == null) {
            tileSignalPartPos = null;
            groupId = null;
            activeGroupState = null;
            inactiveGroupState = null;
            redstone = null;
            noRedstone = null;
            return;
        }

        signalType = 0;

        String[] states = LOSBlocks.BLOCK_SIGNAL_PART.getAllStates(tempTileSignalPart.getId());
        int index = 0;
        for (String state : states) {
            if (index == redstone) {
                activeGroupState = state;
            }
            if (index == noRedstone) {
                inactiveGroupState = state;
            }
            index++;
        }
        redstone = null;
        noRedstone = null;
    }

}
