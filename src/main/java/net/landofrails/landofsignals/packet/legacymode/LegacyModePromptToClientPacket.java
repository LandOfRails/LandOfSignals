package net.landofrails.landofsignals.packet.legacymode;

import cam72cam.mod.math.Vec3i;
import cam72cam.mod.net.Packet;
import cam72cam.mod.serialization.TagField;
import net.landofrails.landofsignals.gui.GuiLegacyModeSelection;

public class LegacyModePromptToClientPacket extends Packet {

    @TagField("blockPos")
    private Vec3i blockPos;
    @TagField("itemId")
    private String itemId;
    @TagField("rotation")
    private Integer rotation;

    public LegacyModePromptToClientPacket() {

    }

    public LegacyModePromptToClientPacket(Vec3i blockPos, String itemId, int rotation) {
        this.blockPos = blockPos;
        this.itemId = itemId;
        this.rotation = rotation;
    }

    @Override
    protected void handle() {

        GuiLegacyModeSelection.open(getPlayer(), blockPos, itemId, rotation);

    }
}
