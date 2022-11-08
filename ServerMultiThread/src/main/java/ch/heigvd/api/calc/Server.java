package ch.heigvd.api.calc;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Calculator server implementation - multi-thread
 */
public class Server {
    private static final int PORT = 420;
    private final static int MAX_CLIENTS = 5;
    public static int clientCount = 0;
    private final String serverFullMessage = "Server is full, please try again later";
    private final static Logger LOG = Logger.getLogger(Server.class.getName());

    /**
     * Main function to start the server
     */
    public static void main(String[] args) {
        // Log output on a single line
        System.setProperty("java.util.logging.SimpleFormatter.format", "%4$s: %5$s%6$s%n");
        (new Server()).start();
    }

    /**
     * Start the server on a listening socket.
     */
    private void start() {
        ServerSocket serverSocket = null;
        try {
            // Create the server socket
            serverSocket = new ServerSocket(PORT);
            LOG.log(Level.INFO, "Server started on port " + PORT);

            // Waiting for new clients to connect
            while (true) {
                Socket clientSocket = serverSocket.accept();
                if (clientCount < MAX_CLIENTS) {
                    // Create new thread to handle this client
                    ServerWorker worker = new ServerWorker(clientSocket);
                    LOG.log(Level.INFO, "Client connected from " + clientSocket.getInetAddress());
                    Thread thread = new Thread(worker);
                    thread.start();
                } else {
                    //server is full
                    LOG.log(Level.INFO, "Server is full, client rejected");
                    BufferedWriter toClient = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream(), "utf-8"));
                    toClient.write(serverFullMessage, 0, serverFullMessage.length());
                    toClient.flush();
                    clientSocket.close();
                }

            }
        } catch (IOException e) {
            throw new RuntimeException("Error could not create a socket on port " + PORT + ".\nError:", e);
        } finally {
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    LOG.log(Level.SEVERE, "Error while closing the server socket", e);
                }
            }
        }

    }
}
