package net.landofrails.landofsignals.tile;

import cam72cam.mod.ModCore;
import cam72cam.mod.block.BlockEntity;
import cam72cam.mod.entity.Player;
import cam72cam.mod.entity.boundingbox.IBoundingBox;
import cam72cam.mod.item.ItemStack;
import cam72cam.mod.math.Vec3d;
import cam72cam.mod.math.Vec3i;
import cam72cam.mod.serialization.TagCompound;
import cam72cam.mod.serialization.TagField;
import cam72cam.mod.util.Facing;
import net.landofrails.api.contentpacks.v2.complexsignal.ContentPackSignalGroup;
import net.landofrails.landofsignals.LOSBlocks;
import net.landofrails.landofsignals.LOSItems;
import net.landofrails.landofsignals.packet.GuiSignalPrioritizationToClientPacket;
import net.landofrails.landofsignals.packet.SignalUpdatePacket;
import net.landofrails.landofsignals.serialization.MapStringStringArrayMapper;
import net.landofrails.landofsignals.serialization.MapStringStringMapper;
import net.landofrails.landofsignals.serialization.MapVec3iStringStringMapper;
import net.landofrails.landofsignals.utils.IManipulate;
import net.landofrails.landofsignals.utils.LandOfSignalsUtils;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("java:S1133")
public class TileComplexSignal extends BlockEntity implements IManipulate {

    @TagField("blockRotation")
    private int blockRotate;
    @TagField("id")
    private final String id;
    @TagField(value = "signalGroupStates", mapper = MapStringStringMapper.class)
    private Map<String, String> signalGroupStates = new HashMap<>();
    // for server only
    @TagField(value = "senderSignalGroupStates", mapper = MapVec3iStringStringMapper.class)
    private Map<Vec3i, Map.Entry<String, String>> senderSignalGroupStates = new HashMap<>();
    @TagField(value = "orderedGroupStates", mapper = MapStringStringArrayMapper.class)
    private Map<String, String[]> orderedGroupStates;

    @TagField("offset")
    private Vec3d offset = Vec3d.ZERO;

    public TileComplexSignal(final String id, final int rot) {
        blockRotate = rot;
        this.id = id;
    }

    @Override
    public boolean onClick(Player player, Player.Hand hand, Facing facing, Vec3d hit) {

        if (player.isCrouching() || LandOfSignalsUtils.isLandOfSignalsItem(player.getHeldItem(hand))) {
            return false;
        }
        if (!getWorld().isServer) {
            return true;
        }

        GuiSignalPrioritizationToClientPacket.sendToPlayer(player, this);

        return true;
    }

    @Override
    public ItemStack onPick() {
        final ItemStack is = new ItemStack(LOSItems.ITEM_COMPLEX_SIGNAL, 1);
        final TagCompound tag = is.getTagCompound();
        tag.setString("itemId", id);
        is.setTagCompound(tag);
        return is;
    }

    @Override
    public IBoundingBox getBoundingBox() {
        return IBoundingBox.BLOCK.offset(offset);
    }

    @Override
    public IBoundingBox getRenderBoundingBox() {
        return IBoundingBox.BLOCK.offset(offset);
    }

    public int getBlockRotate() {
        return blockRotate;
    }

    public String getId() {
        return id;
    }

    @Override
    public void setOffset(final Vec3d vec) {
        offset = vec;
        try {
            save(new TagCompound().setVec3d("offset", vec));
        } catch (final Exception ignored) {
            // Nothing you can do now
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
            // Nothing you can do now
        }
    }

    @Override
    public int getRotation() {
        return getBlockRotate();
    }

    public Map<String, String[]> getOrderedGroupStates() {
        if (this.orderedGroupStates == null) {
            Map<String, ContentPackSignalGroup> allGroups = LOSBlocks.BLOCK_COMPLEX_SIGNAL.getAllGroupStates(id);
            this.orderedGroupStates = new HashMap<>();
            allGroups.forEach((groupId, group) -> {
                Set<String> stateIds = group.getStates().keySet();
                orderedGroupStates.put(groupId, stateIds.toArray(new String[0]));
            });
        }
        return this.orderedGroupStates;
    }

    public void setOrderedGroupStates(Map<String, String[]> orderedGroupStates) {
        this.orderedGroupStates = orderedGroupStates;
    }

    /**
     * client-only
     *
     * @param signalGroupStates Map of Groups and each State
     */
    public void setSignalGroupStates(Map<String, String> signalGroupStates) {
        this.signalGroupStates = signalGroupStates;
    }

    /**
     * client-only
     *
     * @return Map<Group, Groupstate>
     */
    @SuppressWarnings("java:S1134")
    public Map<String, String> getSignalGroupStates() {

        // CHECK: Is there a more performance-friendly way of doing this?
        if (this.signalGroupStates.isEmpty()) {
            refreshSignals(false);
        }

        return this.signalGroupStates;
    }

    /**
     * server-only
     *
     * @param pos        Sender position
     * @param groupId    Selected sender groupId
     * @param groupState Current sender groupstate
     */
    public void updateSignals(Vec3i pos, String groupId, String groupState) {
        ModCore.debug("Pos: %s, GroupId: %s, State: %s", pos.toString(), groupId, groupState);

        senderSignalGroupStates.put(pos, new AbstractMap.SimpleEntry<>(groupId, groupState));

        refreshSignals(true);
    }

    /**
     * server-only
     */
    public void updateSignals() {
        refreshSignals(true);
    }

    /**
     * server-only
     *
     * @param pos Sender position
     */
    public void removeSignal(Vec3i pos) {
        senderSignalGroupStates.remove(pos);
        refreshSignals(true);
    }

    private void refreshSignals(boolean updateClients) {
        Map<String, String[]> signals = getOrderedGroupStates();
        signals.forEach((signalGroupId, states) -> {
            String lastState = null;
            for (String state : states) {
                if (lastState == null || senderSignalGroupStates.containsValue(new AbstractMap.SimpleEntry<>(signalGroupId, state))) {
                    lastState = state;
                }
            }
            signalGroupStates.put(signalGroupId, lastState);
        });

        if (updateClients) {
            new SignalUpdatePacket(getPos(), signalGroupStates).sendToAll();
        }
    }

}
