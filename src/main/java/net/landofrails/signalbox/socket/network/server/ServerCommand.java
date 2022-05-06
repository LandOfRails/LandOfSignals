package net.landofrails.signalbox.socket.network.server;

import net.landofrails.signalbox.socket.network.common.Command;

public enum ServerCommand implements Command {
    HANDSHAKE, CONNECTED, ERROR_CONNECTION, DISCONNECTED, REJECT_CONNECTION
}