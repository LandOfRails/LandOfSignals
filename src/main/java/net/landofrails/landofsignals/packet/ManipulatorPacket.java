package net.landofrails.landofsignals.packet;

import cam72cam.mod.block.BlockEntity;
import cam72cam.mod.entity.Player;
import cam72cam.mod.math.Vec3d;
import cam72cam.mod.math.Vec3i;
import cam72cam.mod.net.Packet;
import cam72cam.mod.serialization.TagField;
import net.landofrails.landofsignals.utils.IManipulate;

public class ManipulatorPacket extends Packet {

    @TagField("movement")
    private Vec3d movement;
    @TagField("mainPos")
    private Vec3d mainPos;
    @TagField("player")
    private Player player;
    @TagField("blockPos")
    private Vec3i blockPos;

    public ManipulatorPacket() {

    }

    public ManipulatorPacket(Vec3d mainPos, Vec3d movement, Player player, Vec3i blockPos) {
        this.mainPos = mainPos;
        this.movement = movement;
        this.player = player;
        this.blockPos = blockPos;
    }

    @Override
    protected void handle() {
        player.setPosition(mainPos.add(movement));
        BlockEntity block = getWorld().getBlockEntity(blockPos, BlockEntity.class);
        if (block instanceof IManipulate) {
            ((IManipulate) block).setOffset(movement);
        }
    }
}
