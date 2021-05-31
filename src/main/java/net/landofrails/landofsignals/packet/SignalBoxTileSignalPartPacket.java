package net.landofrails.landofsignals.packet;

import cam72cam.mod.math.Vec3i;
import cam72cam.mod.net.Packet;
import cam72cam.mod.serialization.TagField;
import net.landofrails.landofsignals.LOSGuis;
import net.landofrails.landofsignals.tile.TileSignalBox;
import net.landofrails.landofsignals.tile.TileSignalPart;

public class SignalBoxTileSignalPartPacket extends Packet {

    @TagField("tileSignalPart")
    TileSignalPart tileSignalPart;
    @TagField("posSignalBox")
    Vec3i posSignalBox;

    public SignalBoxTileSignalPartPacket() {
    }

	public SignalBoxTileSignalPartPacket(TileSignalPart tileSignalPart, Vec3i posSignalBox) {
        this.tileSignalPart = tileSignalPart;
        this.posSignalBox = posSignalBox;
    }

    @Override
    protected void handle() {
        getWorld().getBlockEntity(posSignalBox, TileSignalBox.class).setTileSignalPart(tileSignalPart);
		LOSGuis.SIGNAL_BOX.open(getPlayer(), posSignalBox);
    }
}
