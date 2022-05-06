package net.landofrails.signalbox.socket.network.server;

import net.landofrails.signalbox.socket.network.client.ClientCommand;
import net.landofrails.signalbox.socket.network.common.Command;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

public class Server {

    /*
     * The server socket that accepts connections.
     */
    private ServerSocket serverSocket;

    /*
     * where all running clients are saved, and mapped by their client id's.
     */
    private Map<Integer, ConnectionToClient> clients;

    /*
     * all incoming messages are queued here.
     */
    private LinkedBlockingQueue<Message> messages;

    /*
     * the listening port, specified by subclass.
     */
    private int port;

    /*
     * The amount of message handling threads.
     */
    private int messageHandlingThreadCount;

    /*
     * whether the server is active.
     */
    private volatile boolean running;

    private volatile boolean alive;

    /*
     * Limit of allowed connections, negative means infinite.
     */
    private int clientLimit;
    /*
     * where listeners are saved.
     */
    private List<ServerListener> listeners;

    /**
     * Maximum time allowed for new connection to hang, on authentication and
     * initialization.
     */
    public static final int TIMEOUT = 10000;

    /**
     * Constructs a {@code Server} listening for clients on specified port, and
     * specified amount of message handling threads. If the given thread count
     * is less than one, it will automatically be adjusted to 1.
     *
     * <p>
     * Please Note: Setting the thread count to more than one, may cause the
     * listeners to receive messages asynchronously. Use synchronization as
     * needed, or reconsider using multiple threads.
     *
     * <p>
     * The server will start running when the {@code start()} method is invoked. It can be stopped, by
     * invoking {@code shutDown()}.
     *
     * @param port                       The port to listen for.
     * @param messageHandlingThreadCount The amount of message handling threads, reading asynchronously
     *                                   for incoming messages.
     */
    public Server(int port, int messageHandlingThreadCount, ServerListener serverListener) {
        this.port = port;
        clients = Collections.synchronizedMap(new HashMap<>());
        messages = new LinkedBlockingQueue<>();
        listeners = Collections.synchronizedList(new ArrayList<>());
        this.messageHandlingThreadCount = Math.max(1, messageHandlingThreadCount);
        clientLimit = -1;
        alive = true;

        addServerListener(serverListener);
    }

    /**
     * Constructs a {@code Server} listening for clients on specified port, with
     * 1 message handling thread.
     *
     * <p>
     * The server will start running when the {@code start()} method is invoked. It can be stopped, by
     * invoking {@code shutDown()}.
     *
     * @param port The port to listen for.
     */
    public Server(int port, ServerListener serverListener) {
        this(port, 1, serverListener);
    }

    /**
     * Initialization on every received message. A subclass may return a
     * different object (e.g. after decode), that will be forwarded to the
     * listeners. If the returned value is {@code null}, the message will not be
     * forwarded to the listeners.
     *
     * <p>
     * This method will never give a {@code null} value as an argument.
     *
     * <p>
     * The current implementation simply returns the all messages, as received.
     *
     * @param id  The client id that sent this message.
     * @param msg The received message.
     * @return The message that should be forwarded to the listeners.
     */
    protected Object messageReceivedInit(int id, Object msg) {
        return msg;
    }

    /**
     * Commands that clients may send, may be initialized here. Commands are
     * interactions between server and client that is not meant to be shown for
     * the end-user, rather it is to update each other with back-side
     * information, or to request certain types of data.
     *
     * <p>
     * A subclass may return a different object (e.g. after decode), that will
     * be forwarded to the listeners. If the returned value is {@code null}, the
     * command will not be forwarded to the listeners.
     *
     * <p>
     * This method will never give a {@code null} value as an argument.
     *
     * <p>
     * The current implementation simply returns the all commands, as received.
     *
     * @param id  The client id that sent this command.
     * @param cmd The received command.
     * @return The command that should be forwarded to the listeners.
     */
    protected Command commandReceivedInit(int id, Command cmd) {
        return cmd;
    }

    /**
     * This method is called by server, every time a new client tries to
     * connect. a subclass may exchange any information - as passwords - from or
     * to client, as long {Client#connectionInit} is overridden
     * accordingly. The subclass may, as well, do here any initialization
     * required for each new connection. Examples include, wrapping the streams
     * (e.g. with {@code javax.crypto.CipherOutputStream} - for network
     * security), which is allowed, as long the top-most stream is an Object
     * stream.
     *
     * <p>
     * In any event, where a subclass want's to reject a specific connection, it
     * should return {@code null}.
     *
     * <p>
     * Please note: At this stage of the client connection-life, it is
     * technically not running, so {@code send()} <em>will <b>not</b> send</em>.
     * A subclass should manually send with the stream's methods. Remember to
     * flush and reset the output stream after writing, (or use the
     * {@code force()} convenience method).
     *
     * <p>
     * Also note, that the socket has been set to time out if it blocks for
     * {@linkplain this.TIMEOUT} milliseconds on a read. We strongly advise not to
     * change this behavior; all time consuming outputs (e.g reading from
     * {@code System.in}, and send to client), should be avoided in this method.
     *
     * <p>
     * This method may throw either an {@code IOException}, or a
     * {@code ClassNotFoundException}. If any if these occur, the connection
     * will be shut down.
     *
     * <p>
     * The current implementation of this method simply returns a new instance
     * of {@code ConnectionToClient}. A subclass may return a custom version of
     * that class.
     *
     * @param id     Client's id number.
     * @param socket The socket connected to client.
     * @param in     Input stream to read input from client.
     * @param out    Output stream to write output to client.
     * @return A {@code ConnectionToClient}, or {@code null} to reject
     * connection.
     */
    protected ConnectionToClient connectionInit(int id, Socket socket, ObjectInputStream in, ObjectOutputStream out)
            throws IOException, ClassNotFoundException {
        return new ConnectionToClient(id, socket, in, out);
    }

    /**
     * Returns the connection to client {@code n}.
     *
     * @param id The client id.
     * @return The connection for requested id.
     */
    public ConnectionToClient getClient(int id) {
        return clients.get(id);
    }

    /**
     * Returns a {@code Collection} of all active clients.
     *
     * @return A {@code Collection} of all active clients.
     */
    public Collection<ConnectionToClient> getClients() {
        return clients.values();
    }

    /**
     * Returns whether there is an active client with this id.
     *
     * @param id The id to check for.
     * @return Whether is contains an active client for this id.
     */
    public boolean containsId(int id) {
        return clients.containsKey(id);
    }

    /**
     * Returns the amount of threads that handles incoming messages,
     * asynchronously.
     *
     * @return The amount of threads that handles incoming messages,
     * asynchronously.
     */
    public int getMessageHandlingThreadCount() {
        return messageHandlingThreadCount;
    }

    /**
     * Sends a serializable message for the specified client, may be a command,
     * too.
     *
     * @param msg The message to be sent.
     * @param id  The clients id to whom to send to.
     * @return {@code true} if the sending went through without errors.
     */
    public boolean send(Serializable msg, int id) {
        if (!running() || msg == null)
            return false;
        ConnectionToClient ctc = getClient(id);
        if (ctc == null)
            return false;
        return ctc.send(msg);
    }

    /**
     * Sends a serializable message to all active clients, may be a command,
     * too.
     *
     * @param msg The message to be sent.
     * @return {@code true} only if message went through to "all" clients.
     */
    public boolean sendToAll(Serializable msg) {
        if (!running())
            return false;
        boolean passed = true;
        for (ConnectionToClient c : getClients())
            passed &= c.send(msg);
        return passed; // all went through
    }

    /**
     * Returns the listening port of the server socket.
     *
     * @return The listening port of the server socket.
     */
    public int getPort() {
        return port;
    }

    private volatile boolean started = false;

    /**
     * Starts the server, and returns if it has successfully started.
     *
     * <p>
     * If the server has been shut down once (dead) or running already, it will
     * not be started, and will return {@code false}.
     *
     * @return If the server has been started successfully.
     * @return
     */
    public boolean start() {
        if (!isAlive() || running())
            return false;

        /*
         * double check to avoid unnecessary synchronization. (Saw it in
         * Effective Java, by Joshua Bloch)
         */
        if (started)
            return false;
        synchronized (this) {
            if (started)
                return false;
            started = true;
        }

        try {
            serverSocket = new ServerSocket(this.port, 10);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        running = true;

        Thread acception = new Thread(new Acception(), "Client acception thread");

        for (int i = 0; i < messageHandlingThreadCount; i++) {
            Thread t = new Thread(new MessageHandling(), "Received messages handler thread #" + i);
            t.setDaemon(true);
            t.start();
        }

        acception.setDaemon(true);
        acception.start();

        return true;
    }

    /**
     * Blocks the calling thread, until the server has been shut down or
     * unsynced.
     *
     * @throws InterruptedException If the thread has been interrupted while waiting.
     */
    public void sync() throws InterruptedException {
        if (!running)
            return;
        synchronized (this) {
            if (!running)
                return;
            wait();
        }
    }

    /**
     * Blocks the calling thread for the given time, until the server has been
     * shut down or unsynced.
     *
     * @param timeout The maximum amount of milliseconds to block.
     * @throws InterruptedException If the thread has been interrupted while waiting.
     */
    public void sync(int timeout) throws InterruptedException {
        if (!running)
            return;
        synchronized (this) {
            if (!running)
                return;
            wait(timeout);
        }
    }

    /**
     * Unblocks all blocked threads, which called {@code sync()}.
     */
    synchronized public void unsync() {
        notifyAll();
    }

    /**
     * Stops accepting new connections, and clears the accept mechanism
     * resources. The existing connections are preserved. There is no way to
     * accept again with this instance, once stopped.
     *
     * <p>
     * In case a user chooses to limit the number of accepted clients, they
     * should rather use {@code setClientLimit()} since old clients may
     * disconnect, and in turn allow new clients;
     */
    public void stopAccepting() {
        try {
            serverSocket.close();
        } catch (IOException e) {
        }
        serverSocket = null;
    }

    /**
     * Rejects incoming connections, if the server reached the given limit. If
     * clients disconnects, new connections will be allowed again. To stop
     * accepting permanently use {@code stopAccepting()}. Use any negative to
     * allow infinite connections.
     *
     * <p>
     * Note: Even if the connection will eventually be rejected, as it reached
     * the limit; it will still call {@code connectionInit()} (this has been
     * coded this way, not to block the client, as it waits some data in its
     * {@code connectionInit()}). The listeners will <i>not</i> be notified.
     *
     * <p>
     * This method will only affect new connections. Setting the limit to lower
     * than currently connected clients, will only ensure not to accept new
     * until it reaches that number. Existing clients are preserved.
     */
    public void setClientLimit(int limit) {
        clientLimit = limit;
    }

    public int getClientLimit() {
        return clientLimit;
    }

    /**
     * Successfully shuts down this server and all its clients. Once shut down
     * the server can not be restarted. A new instance must be created.
     */
    public void shutDown() {
        if (!isAlive())
            return;

        synchronized (this) {
            if (!isAlive())
                return;
            alive = false;
            running = false;
            unsync();
        }

        for (ConnectionToClient c : getClients())
            c.localShutDown();
    }

    /**
     * Returns if the server is currently running. This may be changed either by
     * the {@code shutDown()} method, or by any error occurring to the server.
     *
     * @return If the server is currently running.
     */
    public boolean running() {
        return running;
    }

    /**
     * Returns if the server is eligible to start, or has started already. A
     * dead server, means it has shut down and may not start again.
     *
     * @return If the server has not ever shut down.
     */
    public boolean isAlive() {
        return alive;
    }

    /**
     * Registers a {@code ServerListener} to listen for server events.
     *
     * @param sl The {@code ServerListener} to register.
     */
    public void addServerListener(ServerListener sl) {
        listeners.add(sl);
    }

    /**
     * Unregisters a {@code ServerListener} from getting server events.
     *
     * @param sl The {@code ServerListener} to remove.
     */
    public void removeServerListener(ServerListener sl) {
        listeners.remove(sl);
    }

    /**
     * Convenience method to force written objects on the stream, since objects
     * operate weirdly on ObjectOutputStreams.
     */
    protected static void force(ObjectOutputStream out) throws IOException {
        out.flush();
        out.reset();
    }

    /*
     * Randomly picks ID numbers for newly connected clients.
     */
    private static Random rand = new Random();

    /*
     * Takes care of accepting new clients.
     */
    private class Acception implements Runnable {

        public void run() {
            while (running() && !serverSocket.isClosed()) {
                Socket socket = null;
                InputStream in = null;
                OutputStream out = null;
                try {
                    socket = serverSocket.accept();
                    out = socket.getOutputStream();
                    in = socket.getInputStream();
//                    authenticate(socket, in, out);

                } catch (IOException e) {
                    try {
                        if (out != null)
                            out.close();
                    } catch (IOException e1) {
                    }
                } catch (RuntimeException rte) {
                    rte.printStackTrace();
                    shutDown(); // Programmers error, why continue ??
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            }
        }

        /*
         * Makes sure the client is actually "my client" implementation. Runs on
         * separate thread, since foreign unknown clients may not provide the
         * required information and may hang acception thread.
         */
        private void authenticate(final Socket socket, final ObjectInputStream in, final ObjectOutputStream out) {
            Thread authentication = new Thread("Authentication thread") {
                public void run() {
                    if (!running())
                        return;

                    ConnectionToClient ctc = null;
                    int id = 0;

                    try {
                        socket.setSoTimeout(TIMEOUT); // don't let foreign
                        // clients
                        // hang too long.
                    } catch (SocketException e) {
                        e.printStackTrace();
                        return;
                    }

                    try {
                        out.writeObject(ServerCommand.HANDSHAKE);
                        force(out);
                        Object handShake = in.readObject();

                        if (handShake == ClientCommand.HANDSHAKE) {

                            while (id == 0 || containsId(id))
                                id = rand.nextInt();

                            out.writeInt(id);
                            force(out);

                            ctc = connectionInit(id, socket, in, out);
                        }
                        if (ctc != null && (clientLimit < 0 || clientLimit > clients.size())) {
                            clients.put(id, ctc);
                            out.writeObject(ServerCommand.CONNECTED);
                            force(out);
                        } else {
                            // includes, handShake != ClientCommand.HANDSHAKE
                            if (!socket.isClosed()) {
                                out.writeObject(ServerCommand.REJECT_CONNECTION);
                                force(out);
                            }
                            socket.close();
                            return;
                        }

                    } catch (Throwable t) {
                        System.out.println(t);
                        try {
                            if (!socket.isClosed())
                                out.writeObject(ServerCommand.ERROR_CONNECTION);
                            in.close();
                            out.close();
                        } catch (IOException e) {
                        }
                        return;
                    }

                    try {
                        socket.setSoTimeout(0);
                    } catch (SocketException e) {
                    }

                    ctc.localStart();
                    for (ServerListener sl : listeners)
                        sl.clientConnected(Server.this, ctc);
                }
            };

            authentication.setDaemon(true);
            authentication.start();

        }

    }

    /*
     * A thread that constantly takes (or blocks until available) messages from
     * the message queue. It then forwards it to the right method, either
     * command or message.
     */
    private class MessageHandling implements Runnable {
        public void run() {
            while (running())
                try {
                    Message m = messages.take();
                    if (m == null)
                        continue;
                    if (m.msg instanceof Command) {
                        m.msg = commandReceivedInit(m.id, (Command) m.msg);

                        if (m.msg != null)
                            for (ServerListener sl : listeners)
                                sl.commandReceived(Server.this, getClient(m.id), (Command) m.msg);

                    } else {
                        m.msg = messageReceivedInit(m.id, m.msg);

                        if (m.msg != null)
                            for (ServerListener sl : listeners)
                                sl.messageReceived(Server.this, getClient(m.id), m.msg);
                    }
                } catch (InterruptedException e) {
                } catch (RuntimeException rte) {
                    rte.printStackTrace();
                    shutDown();
                } catch (Throwable t) {
                    t.printStackTrace();
                }
        }

    }

    /**
     * This class represents a connection to a single client. When created it
     * will start running and listen for data received from client. Once it is
     * shut down, it cannot be started again. A new instance must be created.
     */
    public class ConnectionToClient {

        private ObjectInputStream in;
        private ObjectOutputStream out;
        private Socket socket;
        private int clientId;
        private volatile boolean localRunning;
        private volatile boolean localAlive;

        /**
         * Constructs a new instance of a client connection.
         *
         * @param id     The clients unique id.
         * @param socket The client's socket, must be open.
         * @param in     The object input stream, for reading client input.
         * @param out    The object output stream for sending data to client.
         */
        public ConnectionToClient(int id, Socket socket, ObjectInputStream in, ObjectOutputStream out) {
            if (socket.isClosed())
                throw new IllegalStateException("Socket for client " + id + " is closed.");
            clientId = id;
            this.socket = socket;
            this.in = in;
            this.out = out;
            localAlive = true;
        }

        private volatile boolean localStarted = false;

        private void localStart() {
            if (!localAlive || localRunning)
                return;

            if (localStarted)
                return;
            synchronized (this) {
                if (localStarted)
                    return;
                started = true;
                localRunning = true;
            }

            Thread read = new Thread(new Reading(), "Client: " + clientId + " reading thread");
            read.setDaemon(true);
            read.start();

        }

        /**
         * Returns the clients id for this connection.
         *
         * @return The clients id for this connection.
         */
        public int getClientId() {
            return clientId;
        }

        /**
         * Returns the input stream.
         *
         * @return The input stream.
         */
        protected ObjectInputStream getInputStream() {
            return in;
        }

        /**
         * Returns the output stream.
         *
         * @return The output stream.
         */
        protected ObjectOutputStream getOutputStream() {
            return out;
        }

        /**
         * Returns the socket connecting to the client.
         *
         * @return The socket connecting to the client.
         */
        protected Socket getSocket() {
            return socket;
        }

        /**
         * Returns if the client is currently running. This may be changed
         * either by the {@code shutDown()} or {@code localShutDown()} methods,
         * or by any error occurring to this client.
         *
         * @return if the client is currently running.
         */
        public boolean localRunning() {
            return localRunning;
        }

        /**
         * Sends given serializable object to this client.
         *
         * @param msg The message to be sent, may be a command too.
         * @return If message has been sent successfully.
         */
        protected boolean send(Serializable msg) {
            if (!localRunning || socket.isClosed())
                return false;

            msg = sendInit(msg);
            if (msg == null)
                return false;

            try {
                out.writeObject(msg);
                force(out);

                if (msg instanceof Command)
                    for (ServerListener sl : listeners)
                        sl.commandSent(Server.this, this, (Command) msg);
                else
                    for (ServerListener sl : listeners)
                        sl.messageSent(Server.this, this, msg);

                return true;
            } catch (IOException e) {
                localShutDown();
                return false;
            }
        }

        /**
         * A subclass may work with an object asked to send, here (e.g. encode).
         * Only the returned object will be sent. Returning {@code null} will
         * successfully abort the sending of this message.
         *
         * <p>
         * This implementation simply returns the same object, given as
         * argument.
         *
         * @param msg The message asked to be sent.
         * @return The actual message to send.
         */
        protected Serializable sendInit(Serializable msg) {
            return msg;
        }

        /**
         * A subclass may do any initialization in this method, that will be
         * called in the event the client is shut down. This method will always
         * be called <em>after</em> the streams have been closed, it should be
         * used just for server-side stuff.
         *
         * <p>
         * The current implementation does nothing.
         */
        protected void disconnectionInit() {
        }

        /**
         * Shuts down this client only. All other clients may continue working
         * regularly.
         */
        public void localShutDown() {
            if (!localAlive)
                return;
            synchronized (this) {
                if (!localAlive)
                    return;
                localAlive = false;
            }

            try {
                if (!socket.isClosed())
                    out.writeObject(ServerCommand.DISCONNECTED);
            } catch (IOException e) {
            }
            try {
                in.close();
                out.close();
            } catch (IOException e) {
            }

            /*
             * makes sure diconnectionInit may not cause StackOverflowError if
             * they call shutDown()
             */
            boolean init = localRunning;

            localRunning = false;
            if (init) {
                disconnectionInit();
                for (ServerListener sl : listeners)
                    sl.clientDisconnected(Server.this, this);
            }

            clients.remove(clientId);

        }

        public int hashCode() {
            return clientId * 31;
        }

        /*
         * This class takes care of all incoming messages from this client.
         *
         */
        private class Reading implements Runnable {

            public void run() {
                try {
                    while (localRunning()) {
                        Object obj = null;
                        try {
                            obj = in.readObject();
                        } catch (IOException | ClassNotFoundException e) {
                            try {
                                if (!socket.isClosed()) {
                                    out.writeObject(ServerCommand.ERROR_CONNECTION);
                                    force(out);
                                }
                            } catch (IOException e1) {
                            }
                            localShutDown();
                            break;
                        }

                        try {
                            messages.put(new Message(obj, clientId));
                        } catch (InterruptedException e) {
                        }

                    }
                } catch (RuntimeException rte) {
                    rte.printStackTrace();
                    localShutDown();
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            }
        }

        @Override
        protected void finalize() {
            localShutDown();
        }
    }

    /*
     * A simple wrapper class, for server received messages.
     */
    private static class Message {
        Object msg;
        int id;

        Message(Object msg, int id) {
            this.msg = msg;
            this.id = id;
        }
    }

    @Override
    protected void finalize() {
        shutDown();
    }
}