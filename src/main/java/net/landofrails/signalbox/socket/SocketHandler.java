package net.landofrails.signalbox.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketHandler {

    private static boolean running;
    private static ServerSocket serverSocket;
    private static Socket socket;
    private static InputStream is;
    private static OutputStream os;

    public SocketHandler() {
        if (running) return;
        running = true;

        Thread thread = new Thread(() -> {
            try {
                serverSocket = new ServerSocket(4343);
                socket = serverSocket.accept();
                is = socket.getInputStream();
                os = socket.getOutputStream();

                while (!socket.isClosed()) {
                    String message = Receive();
                    System.out.println(message);
                    switch (message) {
                        default:
                            break;
                    }
                    Send(message);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
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