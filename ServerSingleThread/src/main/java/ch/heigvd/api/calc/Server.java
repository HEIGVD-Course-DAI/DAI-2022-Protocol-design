package ch.heigvd.api.calc;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DecimalFormat;
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

        final String ERROR_MSG = "ERREUR entree non valide\n";
        //  (\d+\.?\d*) ([+\-*/]) (\d+\.?\d*)
        final String regexCalcul = "^CALCUL ([+-]?)(\\d+(.\\d)?) ([+*\\-]) ([+-]?)([\\d]+(.[\\d])?)$";
        Pattern patternCalcul = Pattern.compile(regexCalcul);

        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        String line;
        out.write("BONJOUR, ceci est une calculatrice supportant les operations + - * /,  veuillez ajouter un espace entre les operandes, ex : 5 + 4\n" );
        out.flush();
        LOG.info("Reading until client sends QUIT");
        while (!(clientSocket.isClosed())) {
            line = in.readLine();
            if (line.equals("QUIT")) {
                LOG.info("Closing connection");
                break;
            }

            Matcher matcherCalcul = patternCalcul.matcher(line);
            if (matcherCalcul.find()) {

                float operand1 = Float.parseFloat(matcherCalcul.group(2));
                float operand2 = Float.parseFloat(matcherCalcul.group(6));
                float result = 0;
                switch (matcherCalcul.group(4).charAt(0)) {
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
                DecimalFormat f = new DecimalFormat("##.00");
                out.write("RESULT " + f.format(result) + '\n');
                out.flush();
            } else {
                out.write(ERROR_MSG);
                out.flush();
            }
        }
        in.close();
        out.close();
    }
}