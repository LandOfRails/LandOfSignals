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
import java.util.Set;

@SuppressWarnings("java:S1133")
public class TileSignalPart extends BlockEntity implements IManipulate {

    private static final String MISSING = "MISSING";

    @TagField("version")
    private Integer version;
    @TagField("blockRotation")
    private int blockRotate;
    @TagField("id")
    private final String id;
    @TagField("texturePath")
    @Deprecated
    @SuppressWarnings("java:S1123")
    private String texturePath;
    @TagField(value = "signalGroupStates", mapper = MapStringStringMapper.class)
    private Map<String, String> signalGroupStates = new HashMap<>();
    // for server only
    @TagField(value = "senderSignalGroupStates", mapper = MapVec3iStringStringMapper.class)
    private Map<Vec3i, Map.Entry<String, String>> senderSignalGroupStates = new HashMap<>();
    @TagField(value = "legacyMode", typeHint = LegacyMode.class)
    private LegacyMode legacyMode;

    @TagField("offset")
    private Vec3d offset = Vec3d.ZERO;

    public TileSignalPart(final String id, final int rot, LegacyMode legacyMode) {
        blockRotate = rot;
        this.id = id;
        this.legacyMode = legacyMode;
    }

    @Override
    public ItemStack onPick() {
        final ItemStack is = new ItemStack(LOSItems.ITEM_SIGNAL_PART, 1);
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

    /**
     * @return the old texturepath
     * @deprecated (1.0.0, Only needed for backwards compatability)
     */
    @Deprecated
    public String getTexturePath_depr() {
        if ("null".equals(texturePath)) return null;
        else return texturePath;
    }

    public String getId() {
        return id;
    }

    /**
     * @param texturePath The texturepath provided by the contentpack
     * @deprecated (1.0.0, Only needed for backwards compatability)
     */
    @Deprecated
    public void setTexturePath_depr(final String texturePath) {
        if (texturePath == null) this.texturePath = "null";
        else this.texturePath = texturePath;
        markDirty();
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

        if (legacyMode == LegacyMode.ON) {
            senderSignalGroupStates.clear();
        }
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
        if (legacyMode == LegacyMode.OFF) {
            senderSignalGroupStates.remove(pos);
        }
        refreshSignals(true);
    }

    private void refreshSignals(boolean updateClients) {
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

        if (updateClients) {
            new SignalUpdatePacket(getPos(), signalGroupStates).sendToAll();
        }
    }

    @Override
    public void load(TagCompound nbt) throws SerializationException {

        if (!nbt.hasKey("version"))
            nbt.setInteger("version", 1);

        if (nbt.hasKey("id") && !nbt.getString("id").contains(":") && !nbt.getString("id").equalsIgnoreCase(MISSING)) {

            String signalId = nbt.getString("id");
            Set<String> keys = LOSBlocks.BLOCK_SIGNAL_PART.getContentpackSignals().keySet();

            String wantedKey = null;

            for (String key : keys) {
                if (!key.equalsIgnoreCase(MISSING)) {
                    String keyBlockId = key.split(":")[1];
                    if (keyBlockId.equalsIgnoreCase(signalId)) {
                        if (wantedKey == null) {
                            wantedKey = key;
                        } else {
                            wantedKey = null;
                            break;
                        }
                    }
                }
            }

            if (wantedKey != null) {
                nbt.setString("id", wantedKey);
            } else {
                nbt.setString("id", MISSING);
            }

            Vec3i pos = this.getPos();
            ModCore.info("Converting signalpart-tile %s from id \"%s\" to \"%s\".", pos.toString(), signalId, nbt.getString("id"));

            save(nbt);

        }

        TileSignalPartMigratorV1 tileSignalPartMigratorV1 = TileSignalPartMigratorV1.getInstance();
        if (tileSignalPartMigratorV1.shouldBeMigrated(nbt)) {
            tileSignalPartMigratorV1.migrate(nbt);
        }

    }
}
