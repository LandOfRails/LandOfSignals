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

    public SignalBoxGuiToClientPacket() {
    }

    public SignalBoxGuiToClientPacket(TileSignalBox tsb) {
        this.pos = tsb.getPos();
        this.groupId = tsb.getGroupId();
        this.activeGroupState = tsb.getActiveGroupState();
        this.inactiveGroupState = tsb.getInactiveGroupState();

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
        
    }
}
