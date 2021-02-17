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

    public ManipulatorToServerPacket(Vec3d offset, int rotation, Vec3i blockPos) {
        this.offset = offset;
        this.rotation = rotation;
        this.blockPos = blockPos;
    }

    @Override
    protected void handle() {
        BlockEntity block = getWorld().getBlockEntity(blockPos, BlockEntity.class);
        if (block instanceof IManipulate) {
            IManipulate manipulate = (IManipulate) block;
            manipulate.setOffset(manipulate.getOffset().add(offset));
            manipulate.setRotation(rotation);
        }
    }
}
