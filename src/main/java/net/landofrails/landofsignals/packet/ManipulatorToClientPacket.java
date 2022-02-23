package net.landofrails.landofsignals.packet;

import cam72cam.mod.block.BlockEntity;
import cam72cam.mod.math.Vec3d;
import cam72cam.mod.math.Vec3i;
import cam72cam.mod.net.Packet;
import cam72cam.mod.serialization.TagField;
import net.landofrails.landofsignals.utils.IManipulate;

import java.util.ArrayList;

public class ManipulatorToClientPacket extends Packet {

    @TagField("movement")
    private Vec3d movement;
    @TagField("mainPos")
    private Vec3d mainPos;
    @TagField("blockPos")
    private Vec3i blockPos;
    @TagField("gui")
    private boolean gui;
    @TagField("rotation")
    private int rotation;
    @TagField("sneak")
    private boolean sneak;

    public ManipulatorToClientPacket() {

    }

    public ManipulatorToClientPacket(final Vec3d mainPos, final Vec3d movement, final Vec3i blockPos, final boolean sneak) {
        this.mainPos = mainPos;
        this.movement = movement;
        this.blockPos = blockPos;
        gui = false;
        this.sneak = sneak;
    }

    public ManipulatorToClientPacket(final Vec3d offset, final int rotation, final Vec3i blockPos, final boolean sneak) {
        movement = offset;
        this.blockPos = blockPos;
        this.rotation = rotation;
        gui = true;
        this.sneak = sneak;
    }

    @Override
    protected void handle() {

        final ArrayList<Vec3i> blockPosList = new ArrayList<>();
        if (!sneak) {
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
        } else blockPosList.add(blockPos);

        if (!gui) {
            getPlayer().setPosition(mainPos);
            for (final Vec3i bp : blockPosList) {
                final BlockEntity block = getWorld().getBlockEntity(bp, BlockEntity.class);
                if (block instanceof IManipulate) {
                    final IManipulate manipulate = (IManipulate) block;
                    manipulate.setOffset(manipulate.getOffset().add(movement));
                }
            }
        } else {
            for (final Vec3i bp : blockPosList) {
                final BlockEntity block = getWorld().getBlockEntity(bp, BlockEntity.class);
                if (block instanceof IManipulate) {
                    final IManipulate manipulate = (IManipulate) block;
                    manipulate.setOffset(movement);
                    manipulate.setRotation(rotation);
                }
            }
        }
    }
}
