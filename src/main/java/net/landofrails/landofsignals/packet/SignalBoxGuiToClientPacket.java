package net.landofrails.landofsignals.packet;

import cam72cam.mod.math.Vec3i;
import cam72cam.mod.net.Packet;
import cam72cam.mod.serialization.TagField;
import net.landofrails.landofsignals.LandOfSignals;
import net.landofrails.landofsignals.tile.TileSignalBox;

public class SignalBoxGuiToClientPacket extends Packet {

    @TagField("textureNameRedstone")
    private int textureNameRedstone;
    @TagField("textureNameNoRedstone")
    private int textureNameNoRedstone;
    @TagField("pos")
    private Vec3i pos;

    public SignalBoxGuiToClientPacket() {
    }

    public SignalBoxGuiToClientPacket(int textureNameRedstone, int textureNameNoRedstone, Vec3i pos) {
        this.textureNameRedstone = textureNameRedstone;
        this.textureNameNoRedstone = textureNameNoRedstone;
        this.pos = pos;
    }

    @Override
    protected void handle() {
        TileSignalBox box = getWorld().getBlockEntity(pos, TileSignalBox.class);
        if (box != null) {
            box.setRedstone(textureNameRedstone);
            box.setNoRedstone(textureNameNoRedstone);
        } else {
            if (pos != null) {
                LandOfSignals.debug("TileSignalBox was not loaded: [xyz: %f, %f, %f] not found!", pos.x, pos.y, pos.z);
            } else {
                LandOfSignals.warn("TileSignalBox was not loaded: \"pos\" is not set!");
            }
        }
    }
}
