package net.landofrails.stellwand.content.network;

import cam72cam.mod.math.Vec3i;
import cam72cam.mod.net.Packet;
import cam72cam.mod.serialization.TagField;
import net.landofrails.stellwand.content.entities.storage.BlockSenderStorageEntity;

public class ConnectionSenderSignal extends Packet {

	@TagField
	private Vec3i senderPos;
	@TagField
	private Vec3i signalId;
	@TagField
	private boolean disconnect = false;

	public ConnectionSenderSignal() {
		
	}

	public ConnectionSenderSignal(Vec3i senderPos, Vec3i signalId,
			boolean... disconnect) {
		this.senderPos = senderPos;
		this.signalId = signalId;
		if (disconnect.length != 0)
			this.disconnect = disconnect[0];
	}

	@Override
	protected void handle() {
		BlockSenderStorageEntity storageEntity = getWorld()
				.getBlockEntity(senderPos, BlockSenderStorageEntity.class);
		if (!disconnect) {
			storageEntity.signals.add(signalId);
		} else {
			storageEntity.signals.remove(signalId);
		}
	}
 
}
