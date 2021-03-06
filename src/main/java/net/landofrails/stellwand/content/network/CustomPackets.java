package net.landofrails.stellwand.content.network;

import cam72cam.mod.net.Packet;
import cam72cam.mod.net.PacketDirection;

public class CustomPackets {

	public static void register() {
		// client to server
		Packet.register(ChangeHandHeldItem::new,
				PacketDirection.ClientToServer);
		Packet.register(ChangeSenderModes::new, PacketDirection.ClientToServer);
		// Server to client
		Packet.register(ChangeSignalModes::new, PacketDirection.ServerToClient);
		Packet.register(ConnectionSenderSignal::new,
				PacketDirection.ServerToClient);
	}

}
