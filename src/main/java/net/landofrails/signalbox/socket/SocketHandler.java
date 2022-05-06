package net.landofrails.signalbox.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.management.ManagementFactory;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketHandler {

    private static boolean running;
    private static ServerSocket serverSocket;
    private static Socket socket;
    private static InputStream is;
    private static OutputStream os;

    public static void startServer() {
        if (running) return;
        running = true;

        Thread thread = new Thread(() -> {
            try {
                serverSocket = new ServerSocket(4343);
                socket = serverSocket.accept();
                is = socket.getInputStream();
                os = socket.getOutputStream();
                Send(ManagementFactory.getRuntimeMXBean().getName().split("@")[0]);

                while (true) {
                    String message = Receive();
                    System.out.println(message);
                    Send(message);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

//            Server server = new Server(4343, new Logger());
//            server.addServerListener(new ServerListener() {
//                @Override
//                public void clientConnected(Server server, Server.ConnectionToClient client) {
//                    System.out.println("Client connected - " + client.getClientId());
//                    server.sendToAll(ManagementFactory.getRuntimeMXBean().getName().split("@")[0]);
//                }
//
//                @Override
//                public void messageReceived(Server server, Server.ConnectionToClient client, Object msg) {
//                    server.sendToAll(msg.toString());
//                }
//
//                @Override
//                public void commandReceived(Server server, Server.ConnectionToClient client, Command cmd) {
//
//                }
//
//                @Override
//                public void clientDisconnected(Server server, Server.ConnectionToClient client) {
//                    System.out.println("Client disconnected - " + client.getClientId());
//                    //TODO Handle web ui offline
//                }
//
//                @Override
//                public void messageSent(Server server, Server.ConnectionToClient toClient, Object msg) {
//
//                }
//
//                @Override
//                public void commandSent(Server server, Server.ConnectionToClient toClient, Command cmd) {
//
//                }
//            });
//            if (server.start()) System.out.println("SocketServer online.");
//            while (server.running()) {
//
//            }
        });
        thread.start();
    }

    public static void Send(String message) {
        if (socket == null) return;

        byte[] toSendBytes = message.getBytes();
        int toSendLen = toSendBytes.length;
        byte[] toSendLenBytes = new byte[4];
        toSendLenBytes[0] = (byte) (toSendLen & 0xff);
        toSendLenBytes[1] = (byte) ((toSendLen >> 8) & 0xff);
        toSendLenBytes[2] = (byte) ((toSendLen >> 16) & 0xff);
        toSendLenBytes[3] = (byte) ((toSendLen >> 24) & 0xff);
        try {
            os.write(toSendLenBytes);
            os.write(toSendBytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String Receive() {
        if (socket == null) return null;
        // Receiving
        int len;
        byte[] receivedBytes;
        try {
            byte[] lenBytes = new byte[4];
            is.read(lenBytes, 0, 4);
            len = (((lenBytes[3] & 0xff) << 24) | ((lenBytes[2] & 0xff) << 16) |
                    ((lenBytes[1] & 0xff) << 8) | (lenBytes[0] & 0xff));
            receivedBytes = new byte[len];
            is.read(receivedBytes, 0, len);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new String(receivedBytes, 0, len);
    }

    public static String SendReceive(String message) {
        Send(message);
        return Receive();
    }
}