package net.landofrails.landofsignals.packet;

import cam72cam.mod.math.Vec3d;
import cam72cam.mod.math.Vec3i;
import cam72cam.mod.net.Packet;
import cam72cam.mod.serialization.TagField;
import net.landofrails.landofsignals.utils.IManipulate;

public class ManipulatorToClientPacket extends Packet {

    @TagField("movement")
    private Vec3d offset;
    @TagField("blockPos")
    private Vec3i blockPos;
    @TagField("rotation")
    private int rotation;
    @TagField("scaling")
    private Vec3d scaling;
    @TagField("cascade")
    private Boolean cascade;

    public ManipulatorToClientPacket() {

    }

    public ManipulatorToClientPacket(final Vec3i blockPos, final Vec3d offset, final int rotation, final Vec3d scaling, boolean cascade) {
        this.blockPos = blockPos;
        this.offset = offset;
        this.rotation = rotation;
        this.scaling = scaling;
        this.cascade = cascade;
    }

    @Override
    protected void handle() {

        IManipulate.applyChanges(getWorld(), blockPos, cascade, offset, rotation, scaling);
    }

}
