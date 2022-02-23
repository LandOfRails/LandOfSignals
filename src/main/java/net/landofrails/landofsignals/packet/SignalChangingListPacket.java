package net.landofrails.landofsignals.packet;

import cam72cam.mod.math.Vec3i;
import cam72cam.mod.net.Packet;
import cam72cam.mod.serialization.TagField;

import java.util.UUID;

public class SignalChangingListPacket extends Packet {

    @TagField("uuid")
    private UUID uuid;
    @TagField("pos")
    private Vec3i pos;
    @TagField("remove")
    private boolean remove;

    public SignalChangingListPacket() {
    }

    public SignalChangingListPacket(final UUID uuid, final Vec3i pos, final boolean remove) {
        this.uuid = uuid;
        this.pos = pos;
        this.remove = remove;
    }

    @Override
    protected void handle() {
    }
}
