package ch.heigvd.api.calc;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Calculator server implementation - multi-thread
 */
public class Server {

    private final static Logger LOG = Logger.getLogger(Server.class.getName());

    /**
     * Main function to start the server
     */
    public static void main(String[] args) throws IOException, InterruptedException {
        // Log output on a single line
        System.setProperty("java.util.logging.SimpleFormatter.format", "%4$s: %5$s%6$s%n");

        (new Server()).start();
    }

    /**
     * Start the server on a listening socket.
     */
    private void start() throws IOException, InterruptedException {

        /* TODO: implement the receptionist server here.
         *  The receptionist just creates a server socket and accepts new client connections.
         *  For a new client connection, the actual work is done in a new thread
         *  by a new ServerWorker.
         */
        ServerSocket serverSocket = new ServerSocket(7777);
        Socket clientSocket = null;
        while(true){
            System.out.println("Waiting client...");
            clientSocket = serverSocket.accept();
            System.out.println("client trouvé...");
            ServerWorker worker = new ServerWorker(clientSocket);
            Thread thread = new Thread(worker);
            thread.start();
        }
    }
}
