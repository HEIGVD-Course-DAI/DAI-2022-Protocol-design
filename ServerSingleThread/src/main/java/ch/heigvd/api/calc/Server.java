package ch.heigvd.api.calc;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Calculator server implementation - single threaded
 */
public class Server {
    private final int PORT = 4433;

    private final static Logger LOG = Logger.getLogger(Server.class.getName());

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

        // TODO try catch

        // Heavily inspired from example "StreamingTimeServer"
        ServerSocket serverSocket;
        Socket clientSocket;

        serverSocket = new ServerSocket(PORT);

        while (true) {
            // Wait for a connection
            clientSocket = serverSocket.accept();
            handleClient(clientSocket);

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
        final String OPERATORS_REGEX = " [+\\-*/] ";
        BufferedReader reader;
        BufferedWriter writer;

        reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        // Read the operation
        String operation = reader.readLine();
        // TODO Check it

        // Split its parts
        String[] parts = operation.split(OPERATORS_REGEX);
        if(parts.length != 2)
        {
            clientSocket.close();
            return;
        }

        writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

        double operand1 = Double.parseDouble(parts[0]);
        double operand2 = Double.parseDouble(parts[1]);
        // apply the operation depending on the operator
        switch (operation.charAt(parts[0].length() + 1)) {
            case '+':
                writer.write(operand1 + operand2 + "\n");
                break;
            case '-':
                writer.write(operand1 - operand2 + "\n");
                break;
            case '*':
                writer.write(operand1 * operand2 + "\n");
                break;
            case '/':
                writer.write(operand1 / operand2 + "\n");
                break;
        }
        writer.flush();
        clientSocket.close();

    }
}