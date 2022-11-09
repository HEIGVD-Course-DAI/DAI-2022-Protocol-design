// Co-author : Yann Merk
// NOT TESTED, SEE SINGLE THREADED INSTEAD
package ch.heigvd.api.calc;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Calculator worker implementation
 */
public class ServerWorker implements Runnable {

    Socket clientSocket;

    private final static Logger LOG = Logger.getLogger(ServerWorker.class.getName());

    /**
     * Instantiation of a new worker mapped to a socket
     *
     * @param clientSocket connected to worker
     */
    public ServerWorker(Socket clientSocket) {
        // Log output on a single line
        System.setProperty("java.util.logging.SimpleFormatter.format", "%4$s: %5$s%6$s%n");

        /* TODO: prepare everything for the ServerWorker to run when the
         *   server calls the ServerWorker.run method.
         *   Don't call the ServerWorker.run method here. It has to be called from the Server.
         */

        this.clientSocket = clientSocket;

    }

    /**
     * Run method of the thread.
     */
    @Override
    public void run(){

        /* TODO: implement the handling of a client connection according to the specification.
         *   The server has to do the following:
         *   - initialize the dialog according to the specification (for example send the list
         *     of possible commands)
         *   - In a loop:
         *     - Read a message from the input stream (using BufferedReader.readLine)
         *     - Handle the message
         *     - Send to result to the client
         */

        try {

            final String OPERATORS_REGEX = " [+\\-*/] ";
            BufferedReader reader;
            BufferedWriter writer;

            reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            // Read the operation
            String operation = reader.readLine();
            // TODO Check it

            // Split its parts
            String[] parts = operation.split(OPERATORS_REGEX);
            if (parts.length != 2) {
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
        catch (IOException exception)
        {}

    }
}