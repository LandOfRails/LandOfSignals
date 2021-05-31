package net.landofrails.landofsignals.packet;

import cam72cam.mod.math.Vec3i;
import cam72cam.mod.net.Packet;
import cam72cam.mod.serialization.TagField;
import net.landofrails.landofsignals.tile.TileSignalBox;

public class TileSignalBoxToClient extends Packet {

	@TagField("signalBox")
	TileSignalBox signalBox;

	@TagField("pos")
	Vec3i pos;

	public TileSignalBoxToClient() {

	}

	public TileSignalBoxToClient(TileSignalBox signalBox, Vec3i pos) {
		this.signalBox = signalBox;
		this.pos = pos;
	}

	@Override
	protected void handle() {
		getWorld().setBlockEntity(pos, signalBox);
	}

}
