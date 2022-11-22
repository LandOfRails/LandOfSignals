package net.landofrails.landofsignals.packet;

import cam72cam.mod.math.Vec3i;
import cam72cam.mod.net.Packet;
import cam72cam.mod.serialization.TagField;
import net.landofrails.landofsignals.tile.TileSignalBox;

public class SignalBoxGuiToServerPacket extends Packet {

    @TagField("pos")
    private Vec3i pos;

    @TagField("signalGroupId")
    private String groupId;
    @TagField("activeGroupState")
    private String activeGroupState;
    @TagField("inactiveGroupState")
    private String inactiveGroupState;

    public SignalBoxGuiToServerPacket() {
    }

    public SignalBoxGuiToServerPacket(final TileSignalBox tsb) {
        this.pos = tsb.getPos();
        this.groupId = tsb.getGroupId();
        this.activeGroupState = tsb.getActiveGroupState();
        this.inactiveGroupState = tsb.getInactiveGroupState();

    }

    @Override
    protected void handle() {
        final TileSignalBox box = getWorld().getBlockEntity(pos, TileSignalBox.class);

        box.setGroupId(groupId);
        box.setActiveGroupState(activeGroupState);
        box.setInactiveGroupState(inactiveGroupState);

        final SignalBoxGuiToClientPacket packet = new SignalBoxGuiToClientPacket(box);
        packet.sendToAll();
        box.updateSignals(true);
    }
}
