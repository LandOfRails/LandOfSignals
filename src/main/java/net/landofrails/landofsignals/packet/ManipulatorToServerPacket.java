package net.landofrails.landofsignals.packet;

import cam72cam.mod.block.BlockEntity;
import cam72cam.mod.math.Vec3d;
import cam72cam.mod.math.Vec3i;
import cam72cam.mod.net.Packet;
import cam72cam.mod.serialization.TagField;
import net.landofrails.landofsignals.utils.IManipulate;

public class ManipulatorToServerPacket extends Packet {

    @TagField("offset")
    private Vec3d offset;
    @TagField("blockPos")
    private Vec3i blockPos;
    @TagField("rotation")
    private int rotation;

    public ManipulatorToServerPacket() {

    }

    public ManipulatorToServerPacket(final Vec3d offset, final int rotation, final Vec3i blockPos) {
        this.offset = offset;
        this.rotation = rotation;
        this.blockPos = blockPos;
    }

    @Override
    protected void handle() {
        final BlockEntity block = getWorld().getBlockEntity(blockPos, BlockEntity.class);
        if (block instanceof IManipulate) {
            final IManipulate manipulate = (IManipulate) block;
            manipulate.setOffset(offset);
            manipulate.setRotation(rotation);
        }
    }
}
