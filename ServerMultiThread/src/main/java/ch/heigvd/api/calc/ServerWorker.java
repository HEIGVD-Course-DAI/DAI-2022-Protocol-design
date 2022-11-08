package ch.heigvd.api.calc;

import java.io.*;
import java.net.Socket;
import java.text.DecimalFormat;
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
    public ServerWorker(Socket clientSocket) {

        // Log output on a single line
        System.setProperty("java.util.logging.SimpleFormatter.format", "%4$s: %5$s%6$s%n");
        this.clientSocket = clientSocket;

    }

    /**
     * Run method of the thread.
     */
    @Override
    public void run()  {
        final String ERROR_MSG = "ERREUR entree non valide\n";
        final String regexCalcul = "^CALCUL ([+-]?)(\\d+(.\\d)?) ([+*\\-]) ([+-]?)([\\d]+(.[\\d])?)$";
        Pattern patternCalcul = Pattern.compile(regexCalcul);

        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            String line;
            out.write("BONJOUR, ceci est une calculatrice supportant les operations + - * /,  veuillez ajouter un espace entre les operandes, ex : 5 + 4\n" );
            out.flush();
            LOG.info("Reading until client sends QUIT");
            while (!(clientSocket.isClosed())) {
                line = in.readLine();
                if (line.equals("QUIT")) {
                    LOG.info("Connection will close");
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
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}