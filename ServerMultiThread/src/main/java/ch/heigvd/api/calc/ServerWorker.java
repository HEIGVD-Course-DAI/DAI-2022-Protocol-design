package ch.heigvd.api.calc;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Calculator worker implementation
 */
public class ServerWorker implements Runnable {

    private final static Logger LOG = Logger.getLogger(ServerWorker.class.getName());

    private Socket clientSocket;
    private BufferedReader in = null;
    private BufferedWriter out = null;

    /**
     * Instantiation of a new worker mapped to a socket
     *
     * @param clientSocket connected to worker
     */
    public ServerWorker(Socket clientSocket) throws IOException {

        // Log output on a single line
        System.setProperty("java.util.logging.SimpleFormatter.format", "%4$s: %5$s%6$s%n");
        this.clientSocket = clientSocket;




        /* TODO: prepare everything for the ServerWorker to run when the
         *   server calls the ServerWorker.run method.
         *   Don't call the ServerWorker.run method here. It has to be called from the Server.
         */

    }

    /**
     * Run method of the thread.
     */
    @Override
    public void run()  {
        final String ERROR_MSG = "ERREUR wrong input";
        final String regexCalcul = "^CALCUL (\\d+(.\\d)?) ([+*/\\-]) ([\\d]+(.[\\d])?)$";
        Pattern patternCalcul = Pattern.compile(regexCalcul);

        try {
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
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
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