package ch.heigvd.api.calc;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Calculator server implementation - single threaded
 */
public class Server {

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
        /* TODO: implement the receptionist server here.
         *  The receptionist just creates a server socket and accepts new client connections.
         *  For a new client connection, the actual work is done by the handleClient method below.
         */
        final int LISTEN_PORT = 3333;

        ServerSocket serverSocket = null;
        Socket clientSocket = null;

        try {
            serverSocket = new ServerSocket(LISTEN_PORT);

            clientSocket = serverSocket.accept();
            handleClient(clientSocket);
            clientSocket.close();
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, ex.getMessage());
        } finally {
            try {
                if (clientSocket != null) {
                    clientSocket.close();
                }
                if (serverSocket != null) {
                    serverSocket.close();
                }
            } catch (IOException ex) {
                LOG.log(Level.SEVERE, ex.getMessage());
            }
        }
    }

    /**
     * Handle a single client connection: receive commands and send back the result.
     *
     * @param clientSocket with the connection with the individual client.
     */
    private void handleClient(Socket clientSocket) {

        /* TODO: implement the handling of a client connection according to the specification.
         *   The server has to do the following:
         *   - initialize the dialog according to the specification (for example send the list
         *     of possible commands)
         *   - In a loop:
         *     - Read a message from the input stream (using BufferedReader.readLine)
         *     - Handle the message
         *     - Send to result to the client
         */
        final String AVAILABLE  = "Available operations:";
        final String ADD        = "ADD n1 n2";
        final String SUB        = "SUB n1 n2";
        final String MULT       = "MULT n1 n2";
        final String DIV        = "DIV n1 n2";
        final String END        = "END";

        final String RESULT     = "RESULT ";
        final String ERROR      = "ERROR UNKNOWN OPERATION";
        final String ERROR_PARA = "ERROR WRONG PARAMETERS";
        final String QUIT       = "END OF CONNECTION";

        BufferedWriter writer = null;
        BufferedReader reader = null;

        try {
            writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream(), StandardCharsets.UTF_8));
            reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), StandardCharsets.UTF_8));

            writer.write(String.format("%s\n%s\n%s\n%s\n%s\n%s\n", AVAILABLE, ADD, SUB, MULT, DIV, END));
            writer.flush();

            while (true) {
                String[] args = reader.readLine().trim().split(" ");
                String output = RESULT;

                int firstOperand;
                int secondOperand;
                try {
                    if (args[0].equals("QUIT")) {
                        output = QUIT;
                    } else {
                        firstOperand = Integer.parseInt(args[1]);
                        secondOperand = Integer.parseInt(args[2]);

                        switch (args[0]) {
                            case "ADD":
                                output += (firstOperand + secondOperand);
                                break;
                            case "SUB":
                                output += (firstOperand - secondOperand);
                                break;
                            case "MULT":
                                output += (firstOperand * secondOperand);
                                break;
                            case "DIV":
                                output += (firstOperand / secondOperand);
                                break;
                            default:
                                output = ERROR;
                                break;
                        }
                    }
                } catch (Exception ex) {
                    output = ERROR_PARA;
                }

                writer.write(String.format("%s\n", output));
                writer.flush();
            }
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, ex.getMessage());
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException ex) {
                LOG.log(Level.SEVERE, ex.getMessage());
            }
        }
    }
}