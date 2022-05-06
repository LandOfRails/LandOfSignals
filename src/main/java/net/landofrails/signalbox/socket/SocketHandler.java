package net.landofrails.signalbox.socket;

import net.landofrails.signalbox.socket.network.common.Command;
import net.landofrails.signalbox.socket.network.common.Logger;
import net.landofrails.signalbox.socket.network.server.Server;
import net.landofrails.signalbox.socket.network.server.ServerListener;

import java.lang.management.ManagementFactory;

public class SocketHandler {

    private static boolean running;

    public static void startServer() {
        if (running) return;

        Thread thread = new Thread(() -> {
            Server server = new Server(4343, new Logger());
            server.addServerListener(new ServerListener() {
                @Override
                public void clientConnected(Server server, Server.ConnectionToClient client) {
                    System.out.println("Client connected - " + client.getClientId());
                    server.sendToAll(ManagementFactory.getRuntimeMXBean().getName().split("@")[0]);
                }

                @Override
                public void messageReceived(Server server, Server.ConnectionToClient client, Object msg) {
                    server.sendToAll(msg.toString());
                }

                @Override
                public void commandReceived(Server server, Server.ConnectionToClient client, Command cmd) {

                }

                @Override
                public void clientDisconnected(Server server, Server.ConnectionToClient client) {
                    System.out.println("Client disconnected - " + client.getClientId());
                    //TODO Handle web ui offline
                }

                @Override
                public void messageSent(Server server, Server.ConnectionToClient toClient, Object msg) {

                }

                @Override
                public void commandSent(Server server, Server.ConnectionToClient toClient, Command cmd) {

                }
            });
            if (server.start()) System.out.println("SocketServer online.");
            while (server.running()) {

            }
        });
        thread.start();
        running = true;
    }
}