package net.landofrails.landofsignals.packet;

import cam72cam.mod.block.BlockEntity;
import cam72cam.mod.math.Vec3d;
import cam72cam.mod.math.Vec3i;
import cam72cam.mod.net.Packet;
import cam72cam.mod.serialization.TagField;
import net.landofrails.landofsignals.utils.IManipulate;

import java.util.ArrayList;

public class ManipulatorToServerPacket extends Packet {

    @TagField("offset")
    private Vec3d offset;
    @TagField("blockPos")
    private Vec3i blockPos;
    @TagField("rotation")
    private int rotation;
    @TagField("scaling")
    private Vec3d scaling;
    @TagField("cascade")
    private Boolean cascade;

    public ManipulatorToServerPacket() {

    }

    public ManipulatorToServerPacket(final Vec3d offset, final int rotation, final Vec3d scaling, final Vec3i blockPos, boolean cascade) {
        this.offset = offset;
        this.rotation = rotation;
        this.scaling = scaling;
        this.blockPos = blockPos;
        this.cascade = cascade;
    }

    @Override
    protected void handle() {
        final ArrayList<Vec3i> blockPosList = new ArrayList<>();
        if (Boolean.TRUE.equals(cascade)) {
            //UP
            int i = 0;
            while (getWorld().getBlockEntity(blockPos.up(i), BlockEntity.class) instanceof IManipulate) {
                blockPosList.add(blockPos.up(i));
                i++;
            }
            //DOWN
            int j = 0;
            while (getWorld().getBlockEntity(blockPos.down(j), BlockEntity.class) instanceof IManipulate) {
                blockPosList.add(blockPos.down(j));
                j++;
            }
        } else {
            blockPosList.add(blockPos);
        }

        for (final Vec3i bp : blockPosList) {
            final BlockEntity block = getWorld().getBlockEntity(bp, BlockEntity.class);
            if (block instanceof IManipulate) {
                final IManipulate manipulate = (IManipulate) block;
                manipulate.setOffset(offset);
                manipulate.setRotation(rotation);
                manipulate.setScaling(scaling);
            }
        }
    }
}
