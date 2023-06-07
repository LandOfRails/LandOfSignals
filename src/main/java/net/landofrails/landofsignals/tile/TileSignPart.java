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
import cam72cam.mod.util.Facing;
import net.landofrails.landofsignals.LOSBlocks;
import net.landofrails.landofsignals.LOSItems;
import net.landofrails.landofsignals.packet.SignTextPacket;
import net.landofrails.landofsignals.utils.IManipulate;

import java.util.Set;

public class TileSignPart extends BlockEntity implements IManipulate {

    private static final String MISSING = "MISSING";

    @TagField("blockRotation")
    private int blockRotate;
    @TagField("id")
    private final String id;
    @TagField
    private String signText;
    @TagField("offset")
    private Vec3d offset = Vec3d.ZERO;
    @TagField("scaling")
    private Vec3d scaling = new Vec3d(1,1,1);

    public TileSignPart(String id, int rot) {
        this.blockRotate = rot;
        this.id = id;
    }

    @Override
    public boolean onClick(Player player, Player.Hand hand, Facing facing, Vec3d hit) {
        if (!LOSBlocks.BLOCK_SIGN_PART.isWritable(id)) {
            return false;
        }
        if (player.getWorld().isServer) {
            SignTextPacket.openTextBox(player, getPos(), signText);
        }
        return true;
    }

    @Override
    public ItemStack onPick() {
        ItemStack is = new ItemStack(LOSItems.ITEM_SIGN_PART, 1);
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
    public void setOffset(Vec3d vec) {
        offset = vec;
        try {
            save(new TagCompound().setVec3d("offset", vec));
        } catch (Exception ignored) {
            // Can be ignored
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
        } catch (Exception ignored) {
            // Can be ignored
        }
    }

    @Override
    public int getRotation() {
        return getBlockRotate();
    }

    @Override
    public void setScaling(Vec3d scaling) {
        this.scaling = scaling;
    }

    @Override
    public Vec3d getScaling() {
        return scaling;
    }

    public void setText(String signText) {
        this.signText = signText;
        try {
            save(getData());
        } catch (Exception ignored) {
            // Can be ignored
        }
    }

    public String getText() {
        return signText;
    }

    @Override
    public void load(TagCompound nbt) throws SerializationException {

        if (!nbt.hasKey("version"))
            nbt.setInteger("version", 1);

        if (nbt.hasKey("id") && !nbt.getString("id").contains(":") && !nbt.getString("id").equalsIgnoreCase(MISSING)) {

            String signalId = nbt.getString("id");
            Set<String> keys = LOSBlocks.BLOCK_SIGN_PART.getContentpackSigns().keySet();

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
            ModCore.info("Converting signpart-tile %s from id \"%s\" to \"%s\".", pos.toString(), signalId, nbt.getString("id"));

            save(nbt);

        }
    }
}
