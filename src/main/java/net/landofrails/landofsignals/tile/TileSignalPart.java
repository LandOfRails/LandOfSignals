package net.landofrails.landofsignals.tile;

import cam72cam.mod.ModCore;
import cam72cam.mod.block.BlockEntity;
import cam72cam.mod.entity.Player;
import cam72cam.mod.entity.boundingbox.IBoundingBox;
import cam72cam.mod.item.ItemStack;
import cam72cam.mod.math.Vec3d;
import cam72cam.mod.math.Vec3i;
import cam72cam.mod.serialization.SerializationException;
import cam72cam.mod.serialization.TagCompound;
import cam72cam.mod.serialization.TagField;
import cam72cam.mod.text.PlayerMessage;
import cam72cam.mod.util.Facing;
import net.landofrails.landofsignals.LOSBlocks;
import net.landofrails.landofsignals.LOSItems;
import net.landofrails.landofsignals.packet.SignalUpdatePacket;
import net.landofrails.landofsignals.serialization.MapVec3iStringMapper;
import net.landofrails.landofsignals.utils.IManipulate;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("java:S1133")
public class TileSignalPart extends BlockEntity implements IManipulate {

    private static final String MISSING = "MISSING";

    @TagField("blockRotation")
    private int blockRotate;
    @TagField("id")
    private final String id;
    @TagField("texturePath")
    private String texturePath;
    // for server only
    @TagField(value = "senderSignalStates", mapper = MapVec3iStringMapper.class)
    private Map<Vec3i, String> senderSignalStates = new HashMap<>();

    @TagField("offset")
    private Vec3d offset = Vec3d.ZERO;

    public TileSignalPart(final String id, final int rot) {
        this.blockRotate = rot;
        this.id = id;
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

    public String getState() {
        if ("null".equals(texturePath)) return null;
        else return texturePath;
    }

    public String getId() {
        return id;
    }

    public void setState(final String state) {
        if (state == null) this.texturePath = "null";
        else this.texturePath = state;
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
     * server-only
     *
     * @param pos   Sender position
     * @param state Current sender state
     */
    public void updateSignals(Vec3i pos, String state) {
        ModCore.debug("Pos: %s, State: %s", pos.toString(), state);

        senderSignalStates.put(pos, state);

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
        senderSignalStates.remove(pos);
        refreshSignals(true);
    }

    private void refreshSignals(boolean updateClients) {
        String[] states = LOSBlocks.BLOCK_SIGNAL_PART.getContentpackSignals().get(id).getStates();
        boolean first = true;
        String lastState = null;
        for (String state : states) {
            if (first == true || senderSignalStates.containsValue(state)) {
                lastState = state;
                first = false;
            }
        }
        this.texturePath = lastState;

        if (updateClients) {
            new SignalUpdatePacket(getPos(), texturePath).sendToAll();
        }
    }

    @Override
    public void load(TagCompound nbt) throws SerializationException {

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

    }

    @Override
    public boolean onClick(Player player, Player.Hand hand, Facing facing, Vec3d hit) {
        if (getWorld().isServer)
            player.sendMessage(PlayerMessage.direct("Current texturePath: " + texturePath));
        return true;
    }
}
