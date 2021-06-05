package net.landofrails.stellwand.content.network;

import cam72cam.mod.math.Vec3i;
import cam72cam.mod.net.Packet;
import cam72cam.mod.serialization.TagField;
import net.landofrails.stellwand.Stellwand;
import net.landofrails.stellwand.content.entities.storage.BlockSenderStorageEntity;
import net.landofrails.stellwand.content.entities.storage.BlockSignalStorageEntity;
import net.landofrails.stellwand.content.guis.CustomGuis;

public class OpenSenderGui extends Packet {

	@TagField("senderPos")
	private Vec3i senderPos;

	@TagField("signalTile")
	private BlockSignalStorageEntity signalTile;

	public OpenSenderGui() {

	}

	public OpenSenderGui(Vec3i senderPos, BlockSignalStorageEntity signalTile) {
		this.senderPos = senderPos;
		this.signalTile = signalTile;
	}

	@Override
	protected void handle() {

		Stellwand.info("Opening SelectSenderModesGui");

		BlockSenderStorageEntity sender = getWorld().getBlockEntity(senderPos, BlockSenderStorageEntity.class);
		sender.setSignal(signalTile);
		CustomGuis.selectSenderModes.open(getPlayer(), senderPos);

	}

}
