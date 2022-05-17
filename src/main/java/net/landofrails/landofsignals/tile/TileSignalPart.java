package net.landofrails.landofsignals.tile;

import cam72cam.mod.ModCore;
import cam72cam.mod.block.BlockEntity;
import cam72cam.mod.entity.boundingbox.IBoundingBox;
import cam72cam.mod.item.ItemStack;
import cam72cam.mod.math.Vec3d;
import cam72cam.mod.math.Vec3i;
import cam72cam.mod.serialization.SerializationException;
import cam72cam.mod.serialization.TagCompound;
import cam72cam.mod.serialization.TagField;
import net.landofrails.api.contentpacks.v2.signal.ContentPackSignalGroup;
import net.landofrails.landofsignals.LOSBlocks;
import net.landofrails.landofsignals.LOSItems;
import net.landofrails.landofsignals.configs.LegacyMode;
import net.landofrails.landofsignals.contentpacks.migration.TileSignalPartMigratorV1;
import net.landofrails.landofsignals.packet.SignalUpdatePacket;
import net.landofrails.landofsignals.serialization.MapStringStringMapper;
import net.landofrails.landofsignals.serialization.MapVec3iStringStringMapper;
import net.landofrails.landofsignals.utils.IManipulate;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

public class TileSignalPart extends BlockEntity implements IManipulate {

    @TagField("version")
    private Integer version = 2;
    @TagField("blockRotation")
    private int blockRotate;
    @TagField("id")
    private final String id;
    @TagField("texturePath")
    @Deprecated
    private String texturePath = null;
    @TagField(value = "signalGroupStates", mapper = MapStringStringMapper.class)
    private Map<String, String> signalGroupStates = new HashMap<>();
    // for server only
    @TagField(value = "senderSignalGroupStates", mapper = MapVec3iStringStringMapper.class)
    private Map<Vec3i, Map.Entry<String, String>> senderSignalGroupStates = new HashMap<>();
    @TagField(value = "legacyMode", typeHint = LegacyMode.class)
    private LegacyMode legacyMode;

    @TagField("offset")
    private Vec3d offset = Vec3d.ZERO;

    public TileSignalPart(String id, int rot, LegacyMode legacyMode) {
        this.blockRotate = rot;
        this.id = id;
        this.legacyMode = legacyMode;
    }

    @Override
    public ItemStack onPick() {
        ItemStack is = new ItemStack(LOSItems.ITEM_SIGNAL_PART, 1);
        TagCompound tag = is.getTagCompound();
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

    @Deprecated
    public String getTexturePath_depr() {
        if (texturePath != null && texturePath.equals("null")) return null;
        else return texturePath;
    }

    public String getId() {
        return id;
    }

    @Deprecated
    public void setTexturePath_depr(String texturePath) {
        if (texturePath == null) this.texturePath = "null";
        else this.texturePath = texturePath;
        markDirty();
    }

    @Override
    public void setOffset(Vec3d vec) {
        offset = vec;
        try {
            save(new TagCompound().setVec3d("offset", vec));
        } catch (Exception ignored) {

        }
    }

    @Override
    public Vec3d getOffset() {
        return offset;
    }

    @Override
    public void setRotation(int rotation) {
        this.blockRotate = rotation;
        try {
            save(new TagCompound().setInteger("blockRotation", rotation));
        } catch (Exception e) {

        }
    }

    @Override
    public int getRotation() {
        return blockRotate;
    }

    /**
     * client-only
     *
     * @param signalGroupStates
     */
    public void setSignalGroupStates(Map<String, String> signalGroupStates) {
        this.signalGroupStates = signalGroupStates;
    }

    /**
     * client-only
     *
     * @return Map<Group, Groupstate>
     */
    public Map<String, String> getSignalGroupStates() {

        // FIXME not performance-friendly
        if (this.signalGroupStates.isEmpty()) {
            Map<String, ContentPackSignalGroup> signals = LOSBlocks.BLOCK_SIGNAL_PART.getContentpackSignals().get(id).getSignals();
            signals.forEach((signalGroupId, group) ->
                    this.signalGroupStates.putIfAbsent(signalGroupId, group.getStates().keySet().iterator().next())
            );
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

        if (legacyMode == LegacyMode.ON) {
            senderSignalGroupStates.clear();
        }
        senderSignalGroupStates.put(pos, new AbstractMap.SimpleEntry<>(groupId, groupState));

        refreshSignals();
    }

    /**
     * server-only
     */
    public void updateSignals() {
        refreshSignals();
    }

    /**
     * server-only
     *
     * @param pos Sender position
     */
    public void removeSignal(Vec3i pos) {
        if (legacyMode == LegacyMode.OFF) {
            senderSignalGroupStates.remove(pos);
        }
        refreshSignals();
    }

    private void refreshSignals() {
        Map<String, ContentPackSignalGroup> signals = LOSBlocks.BLOCK_SIGNAL_PART.getContentpackSignals().get(id).getSignals();
        signals.forEach((signalGroupId, group) -> {
            String lastState = null;
            for (String state : group.getStates().keySet()) {
                if (lastState == null || senderSignalGroupStates.containsValue(new AbstractMap.SimpleEntry<>(signalGroupId, state))) {
                    lastState = state;
                }
            }
            signalGroupStates.put(signalGroupId, lastState);
        });


        new SignalUpdatePacket(getPos(), signalGroupStates).sendToAll();
    }

    @Override
    public void load(TagCompound nbt) throws SerializationException {

        if (!nbt.hasKey("version"))
            nbt.setInteger("version", 1);

        TileSignalPartMigratorV1 tileSignalPartMigratorV1 = TileSignalPartMigratorV1.getInstance();
        if (tileSignalPartMigratorV1.shouldBeMigrated(nbt)) {
            tileSignalPartMigratorV1.migrate(nbt);
        }

    }
}
