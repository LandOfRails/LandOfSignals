package net.landofrails.landofsignals.packet;

import cam72cam.mod.math.Vec3i;
import cam72cam.mod.net.Packet;
import cam72cam.mod.serialization.TagField;
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

    public SignalBoxGuiToClientPacket(final int textureNameRedstone, final int textureNameNoRedstone, final Vec3i pos) {
        this.textureNameRedstone = textureNameRedstone;
        this.textureNameNoRedstone = textureNameNoRedstone;
        this.pos = pos;
    }

    @Override
    protected void handle() {
        final TileSignalBox box = getWorld().getBlockEntity(pos, TileSignalBox.class);
		if (box != null) {
			box.setRedstone(textureNameRedstone);
			box.setNoRedstone(textureNameNoRedstone);
		}
    }
}
