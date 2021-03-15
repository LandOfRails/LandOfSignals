package net.landofrails.stellwand.content.network;

import cam72cam.mod.math.Vec3i;
import cam72cam.mod.net.Packet;
import cam72cam.mod.serialization.TagField;
import net.landofrails.stellwand.content.entities.storage.BlockSignalStorageEntity;

public class ChangeSignalMode extends Packet {

	@TagField
	private Vec3i pos;

	@TagField
	private String mode;

	public ChangeSignalMode() {

	}

	public ChangeSignalMode(Vec3i pos, String mode) {
		this.pos = pos;
		this.mode = mode;
	}

	@Override
	protected void handle() {
		BlockSignalStorageEntity signal = getWorld().getBlockEntity(pos, BlockSignalStorageEntity.class);
		if (signal != null) {
			signal.setMode(mode);
		}
	}

}
