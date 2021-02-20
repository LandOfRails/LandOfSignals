package net.landofrails.landofsignals.packet;

import cam72cam.mod.block.BlockEntity;
import cam72cam.mod.entity.Player;
import cam72cam.mod.math.Vec3d;
import cam72cam.mod.math.Vec3i;
import cam72cam.mod.net.Packet;
import cam72cam.mod.serialization.TagField;
import net.landofrails.landofsignals.utils.IManipulate;

import java.util.Collections;
import java.util.List;

public class ManipulatorToClientPacket extends Packet {

    @TagField("movement")
    private Vec3d movement;
    @TagField("mainPos")
    private Vec3d mainPos;
    @TagField("player")
    private Player player;
    @TagField("blockPos")
    private List<Vec3i> blockPos;
    @TagField("gui")
    private boolean gui;
    @TagField("rotation")
    private int rotation;

    public ManipulatorToClientPacket() {

    }

    public ManipulatorToClientPacket(Vec3d mainPos, Vec3d movement, Player player, List<Vec3i> blockPos) {
        this.mainPos = mainPos;
        this.movement = movement;
        this.player = player;
        this.blockPos = blockPos;
        gui = false;
    }

    public ManipulatorToClientPacket(Vec3d offset, int rotation, Vec3i blockPos) {
        this.movement = offset;
        this.blockPos = Collections.singletonList(blockPos);
        this.rotation = rotation;
        gui = true;
    }

    @Override
    protected void handle() {
        if (!gui) {
            player.setPosition(mainPos);
            for (Vec3i bp : blockPos) {
                BlockEntity block = getWorld().getBlockEntity(bp, BlockEntity.class);
                if (block instanceof IManipulate) {
                    IManipulate manipulate = (IManipulate) block;
                    manipulate.setOffset(manipulate.getOffset().add(movement));
                }
            }
        } else {
            for (Vec3i bp : blockPos) {
                BlockEntity block = getWorld().getBlockEntity(bp, BlockEntity.class);
                if (block instanceof IManipulate) {
                    IManipulate manipulate = (IManipulate) block;
                    manipulate.setOffset(movement);
                    manipulate.setRotation(rotation);
                }
            }
        }
    }
}
