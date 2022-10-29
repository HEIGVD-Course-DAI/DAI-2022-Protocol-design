package ch.heigvd.api.calc;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    private void start() throws IOException {
        final int PORT_NUMBER = 4000;
        final String ERROR_MSG = "ERREUR wrong input";
        final String regexCalcul = "^CALCUL (\\d+\\.?\\d*) ([+\\-*/]) (\\d+\\.?\\d*)$";

        Pattern patternCalcul = Pattern.compile(regexCalcul);
        /* TODO: implement the receptionist server here.
         *  The receptionist just creates a server socket and accepts new client connections.
         *  For a new client connection, the actual work is done by the handleClient method below.
         */

        // Create the server socket
        ServerSocket serverSocket = null;
        serverSocket = new ServerSocket(PORT_NUMBER);

        while (true) {
                Socket clientSocket = serverSocket.accept();

                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                BufferedWriter out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                String line;
                out.write("BONJOUR listes des operations disponibles : " + Arrays.toString(OPERATIONS));
                LOG.info("Reading until client sends QUIT");
                while ((line = in.readLine()) != null) {
                    if (line.equals("QUIT")) {
                        break;
                    }

                    Matcher matcherCalcul = patternCalcul.matcher(line);
                    if (matcherCalcul.find()) {
                        float operand1 = Float.parseFloat(matcherCalcul.group(0));
                        float operand2 = Float.parseFloat(matcherCalcul.group(2));
                        float result = 0.F;
                        switch (matcherCalcul.group(1).charAt(0)) {
                            case '+' :
                                result = operand1 + operand2;
                                break;
                            case '-' :
                                result = operand1 - operand2;
                                break;
                            case '*' :
                                result = operand1 * operand2;
                                break;
                            case '/' :
                                result = operand1 / operand2;
                                break;
                            default :
                                break;
                        }

                        out.write("RESULT" + result);
                    } else {
                        out.write(ERROR_MSG);
                    }
                }
            clientSocket.close();
            in.close();
            out.close();
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

    }
}