package net.landofrails.landofsignals.packet.legacymode;

import cam72cam.mod.ModCore;
import cam72cam.mod.math.Vec3i;
import cam72cam.mod.net.Packet;
import cam72cam.mod.serialization.TagField;
import net.landofrails.landofsignals.LOSBlocks;
import net.landofrails.landofsignals.configs.LegacyMode;

public class LegacyModePromptBlockPacket extends Packet {

    @TagField("blockPos")
    private Vec3i blockPos;
    @TagField("itemId")
    private String itemId;
    @TagField("blockRotation")
    private Integer blockRotation;
    @TagField(value = "legacyMode", typeHint = LegacyMode.class)
    private LegacyMode legacyMode;

    public LegacyModePromptBlockPacket() {

    }

    public LegacyModePromptBlockPacket(Vec3i blockPos, String itemId, Integer blockRotation, LegacyMode legacyMode) {
        this.blockPos = blockPos;
        this.itemId = itemId;
        this.blockRotation = blockRotation;
        this.legacyMode = legacyMode;
    }

    @Override
    protected void handle() {
        LOSBlocks.BLOCK_SIGNAL_PART.setId(itemId);
        LOSBlocks.BLOCK_SIGNAL_PART.setRot(blockRotation);
        LOSBlocks.BLOCK_SIGNAL_PART.setLegacyMode(legacyMode);
        getWorld().setBlock(blockPos, LOSBlocks.BLOCK_SIGNAL_PART);
        // If client -> server, send to all clients
        if (!getWorld().isClient) {
            ModCore.warn("Sende LegacyModePromptBlockPacket zu allen Clients.");
            this.sendToAll();
        }

    }
}
