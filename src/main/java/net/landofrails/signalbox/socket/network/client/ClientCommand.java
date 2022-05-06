package net.landofrails.signalbox.socket.network.client;

import net.landofrails.signalbox.socket.network.common.Command;

public enum ClientCommand implements Command {
    HANDSHAKE, DISCONNECT
}