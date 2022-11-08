package ch.heigvd.api.calc;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
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
    public static void main(String[] args) throws IOException {
        // Log output on a single line
        System.setProperty("java.util.logging.SimpleFormatter.format", "%4$s: %5$s%6$s%n");

        (new Server()).start();
    }

    /**
     * Start the server on a listening socket.
     */
    private void start() throws IOException {
        final int PORT_NUMBER = 4000;



        /* TODO: implement the receptionist server here.
         *  The receptionist just creates a server socket and accepts new client connections.
         *  For a new client connection, the actual work is done by the handleClient method below.
         */

        // Create the server socket
        ServerSocket serverSocket = null;
        serverSocket = new ServerSocket(PORT_NUMBER);

        while (true) {
                Socket clientSocket = serverSocket.accept();
                handleClient(clientSocket);
                clientSocket.close();
        }

    }



        /**
     * Handle a single client connection: receive commands and send back the result.
     *
     * @param clientSocket with the connection with the individual client.
     */
    private void handleClient(Socket clientSocket) throws IOException {

        final String ERROR_MSG = "ERREUR wrong input\n";
        //  (\d+\.?\d*) ([+\-*/]) (\d+\.?\d*)
        final String regexCalcul = "^CALCUL (\\d+(.\\d)?) ([+*/\\-]) ([\\d]+(.[\\d])?)$";
        Pattern patternCalcul = Pattern.compile(regexCalcul);

        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        String line;
        out.write("BONJOUR listes des operations disponibles : + - * / \n" );
        out.flush();
        LOG.info("Reading until client sends QUIT");
        while (!(clientSocket.isClosed())) {
            line = in.readLine();
            if (line.equals("QUIT")) {
                break;
            }

            Matcher matcherCalcul = patternCalcul.matcher(line);
            if (matcherCalcul.find()) {

                float operand1 = Float.parseFloat(matcherCalcul.group(1));
                float operand2 = Float.parseFloat(matcherCalcul.group(4));
                float result = 0;
                switch (matcherCalcul.group(3).charAt(0)) {
                    case '+':
                        result = operand1 + operand2;
                        break;
                    case '-':
                        result = operand1 - operand2;
                        break;
                    case '*':
                        result = operand1 * operand2;
                        break;
                    case '/':
                        result = operand1 / operand2;
                        break;
                    default:
                        break;
                }

                out.flush();
                out.write("RESULT " + result + '\n');
                out.flush();
            } else {
                out.write(ERROR_MSG);
                out.flush();
            }
        }
        in.close();
        out.close();
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