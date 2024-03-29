package net.landofrails.stellwand.content.network;

import cam72cam.mod.net.Packet;
import cam72cam.mod.net.PacketDirection;

public class CustomPackets {

    private CustomPackets() {

    }

    // @formatter:off
    public static void register() {
        // client to server
        Packet.register(ChangeSenderModes::new, PacketDirection.ClientToServer);

        // Server to client
        Packet.register(ChangeSignalModes::new, PacketDirection.ServerToClient);
        Packet.register(OpenSenderGui::new, PacketDirection.ServerToClient);
        Packet.register(ServerMessagePacket::new, PacketDirection.ServerToClient);

        // Both directions
        Packet.register(ChangeHandHeldItem::new, PacketDirection.ClientToServer);
        Packet.register(ChangeHandHeldItem::new, PacketDirection.ServerToClient);
    }
    // @formatter:on

}
