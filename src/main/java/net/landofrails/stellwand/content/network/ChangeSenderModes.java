package net.landofrails.stellwand.content.network;

import cam72cam.mod.math.Vec3i;
import cam72cam.mod.net.Packet;
import cam72cam.mod.serialization.TagField;
import net.landofrails.stellwand.content.entities.storage.BlockSenderStorageEntity;

public class ChangeSenderModes extends Packet {

	@TagField("pos")
	private Vec3i pos;

	@TagField("modePowerOff")
	private String modePowerOff;

	@TagField("modePowerOn")
	private String modePowerOn;

	public ChangeSenderModes() {

	}

	public ChangeSenderModes(Vec3i pos, String modePowerOff,
			String modePowerOn) {
		this.pos = pos;
		this.modePowerOff = modePowerOff;
		this.modePowerOn = modePowerOn;
	}

	@Override
	protected void handle() {
		BlockSenderStorageEntity sender = getWorld().getBlockEntity(pos,
				BlockSenderStorageEntity.class);
		sender.modePowerOff = modePowerOff;
		sender.modePowerOn = modePowerOn;
		sender.markDirty();
		// TODO: Refresh?
	}

}
