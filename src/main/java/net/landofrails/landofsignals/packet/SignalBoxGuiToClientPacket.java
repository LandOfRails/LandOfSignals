package net.landofrails.landofsignals.packet;

import cam72cam.mod.math.Vec3i;
import cam72cam.mod.net.Packet;
import cam72cam.mod.serialization.TagField;
import net.landofrails.landofsignals.tile.TileSignalBox;

public class SignalBoxGuiToClientPacket extends Packet {

    @TagField("pos")
    private Vec3i pos;

    @TagField("signalGroupId")
    private String groupId;
    @TagField("activeGroupState")
    private String activeGroupState;
    @TagField("inactiveGroupState")
    private String inactiveGroupState;

    // TODO Remove old
    @TagField("textureNameRedstone")
    private int textureNameRedstone;
    @TagField("textureNameNoRedstone")
    private int textureNameNoRedstone;

    public SignalBoxGuiToClientPacket() {
    }

    @Deprecated
    public SignalBoxGuiToClientPacket(int textureNameRedstone, int textureNameNoRedstone, Vec3i pos) {
        this.textureNameRedstone = textureNameRedstone;
        this.textureNameNoRedstone = textureNameNoRedstone;
        this.pos = pos;
    }

    public SignalBoxGuiToClientPacket(TileSignalBox tsb) {
        this.pos = tsb.getPos();
        this.groupId = tsb.getGroupId();
        this.activeGroupState = tsb.getActiveGroupState();
        this.inactiveGroupState = tsb.getInactiveGroupState();

        // TODO Remove
        this.textureNameNoRedstone = tsb.getNoRedstone();
        this.textureNameRedstone = tsb.getRedstone();
        // Old

    }

    @Override
    protected void handle() {

        if (!getWorld().isBlockLoaded(pos)) {
            getWorld().keepLoaded(pos);
        }

        TileSignalBox box = getWorld().getBlockEntity(pos, TileSignalBox.class);

        if (box == null) {
            return;
        }

        box.setGroupId(groupId);
        box.setActiveGroupState(activeGroupState);
        box.setInactiveGroupState(inactiveGroupState);

        // TODO Remove old
        box.setRedstone(textureNameRedstone);
        box.setNoRedstone(textureNameNoRedstone);
        //
    }
}
