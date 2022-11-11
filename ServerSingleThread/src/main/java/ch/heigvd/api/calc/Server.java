package ch.heigvd.api.calc;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Calculator server implementation - single threaded
 */
public class Server {

    private final static Logger LOG = Logger.getLogger(Server.class.getName());
    ServerSocket serverSocket;
    String CRLF = "\r\n";


    /**
     * Main function to start the server
     */
    public static void main(String[] args) throws IOException {
        // Log output on a single line
        System.setProperty("java.util.logging.SimpleFormatter.format", "%4$s: %5$s%6$s%n");

        (new Server()).start();
    }

    /**
     * Start the server on a listening socket.
     */
    private void start() throws IOException {
        /* TODO: implement the receptionist server here.
         *  The receptionist just creates a server socket and accepts new client connections.
         *  For a new client connection, the actual work is done by the handleClient method below.
         */
        final int LISTEN_PORT = 9999;
        serverSocket = new ServerSocket(LISTEN_PORT);
        System.out.println("Server listening on port: " + LISTEN_PORT);

        // Boucle pour garder le serveur "allumé"
        while(true) {
            handleClient(serverSocket.accept());
        }
    }

    /**
     * Handle a single client connection: receive commands and send back the result.
     *
     * @param clientSocket with the connection with the individual client.
     */
    private void handleClient(Socket clientSocket) throws IOException {

        /* TODO: implement the handling of a client connection according to the specification.
         *   The server has to do the following:
         *   - initialize the dialog according to the specification (for example send the list
         *     of possible commands)
         *   - In a loop:
         *     - Read a message from the input stream (using BufferedReader.readLine)
         *     - Handle the message
         *     - Send to result to the client
         */
        System.out.println("user connected");
        BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), "UTF-8"));
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream(), "UTF-8"));

        // Afficher le message de bienvenue
        String welcome = "WELCOME" + CRLF +
                "- AVAILABLE OPERATIONS " + CRLF;
        writer.write(welcome);
        writer.flush();

        // Continuer la connexion tant que l'utilisateur ne rentre pas QUIT CRLF
        String message = "";
        while(!Objects.equals(message, "QUIT" + CRLF) && !Objects.equals(message, "QUIT")) {
            message = reader.readLine();

            if(message.startsWith("COMPUTE")) {
                int v1;
                int v2;
                message.split(" ");

            }
        }

        System.out.println("Connexion fermée !");
        writer.close();
        reader.close();
        clientSocket.close();
    }
}