package net.landofrails.landofsignals.tile;

import cam72cam.mod.ModCore;
import cam72cam.mod.block.BlockEntity;
import cam72cam.mod.entity.boundingbox.IBoundingBox;
import cam72cam.mod.item.ItemStack;
import cam72cam.mod.math.Vec3d;
import cam72cam.mod.math.Vec3i;
import cam72cam.mod.serialization.TagCompound;
import cam72cam.mod.serialization.TagField;
import net.landofrails.api.contentpacks.v2.complexsignal.ContentPackSignalGroup;
import net.landofrails.landofsignals.LOSBlocks;
import net.landofrails.landofsignals.LOSItems;
import net.landofrails.landofsignals.packet.SignalUpdatePacket;
import net.landofrails.landofsignals.serialization.MapStringStringMapper;
import net.landofrails.landofsignals.serialization.MapVec3iStringStringMapper;
import net.landofrails.landofsignals.utils.IManipulate;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

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

    @TagField("offset")
    private Vec3d offset = Vec3d.ZERO;

    public TileComplexSignal(final String id, final int rot) {
        blockRotate = rot;
        this.id = id;
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
        Map<String, ContentPackSignalGroup> signals = LOSBlocks.BLOCK_COMPLEX_SIGNAL.getContentpackComplexSignals().get(id).getSignals();
        signals.forEach((signalGroupId, group) -> {
            String lastState = null;
            for (String state : group.getStates().keySet()) {
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
