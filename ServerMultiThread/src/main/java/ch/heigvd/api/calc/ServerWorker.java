package ch.heigvd.api.calc;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.Integer.parseInt;

/**
 * Calculator worker implementation
 */
public class ServerWorker implements Runnable {

    private final static Logger LOG = Logger.getLogger(ServerWorker.class.getName());

    private Socket clientSocket;
    private BufferedReader reader;
    private BufferedWriter writer;

    /**
     * Instantiation of a new worker mapped to a socket
     *
     * @param clientSocket connected to worker
     */
    public ServerWorker(Socket clientSocket) throws IOException {
        // Log output on a single line
        System.setProperty("java.util.logging.SimpleFormatter.format", "%4$s: %5$s%6$s%n");

        /* TODO: prepare everything for the ServerWorker to run when the
         *   server calls the ServerWorker.run method.
         *   Don't call the ServerWorker.run method here. It has to be called from the Server.
         */

        this.clientSocket = clientSocket;
        this.reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), "UTF-8"));
        this.writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream(), "UTF-8"));

    }

    /**
     * Run method of the thread.
     */
    @Override
    public void run() {

        /* TODO: implement the handling of a client connection according to the specification.
         *   The server has to do the following:
         *   - initialize the dialog according to the specification (for example send the list
         *     of possible commands)
         *   - In a loop:
         *     - Read a message from the input stream (using BufferedReader.readLine)
         *     - Handle the message
         *     - Send to result to the client
         */
        String line = null;
        try {
            line = reader.readLine();
            while (true) {
                System.out.println("message recu |" + line + "|");
                if (Objects.equals(line, "close")) {
                    break;
                }else if (Objects.equals(line, "open")) {
                    writer.write(("connection ok\n"));
                } else if (Objects.equals(line, "help")) {
                    writer.write("Bonjour, entrez votre calcul, les opérations supporée sont + - * / ex. |3 * 4 + 5|, " +
                            "mon développeur étant paresseux les paranthèse ne sont pas traitée ni la priorité des opérations\n");
                } else {
                    line = line.replaceAll("\\s+", "");
                    String[] val = line.split("[*/+-]");
                    List<Character> op = new ArrayList<>();
                    boolean formatOk = true;
                    for (int i = 0; i < line.length(); ++i) {
                        char c = line.charAt(i);
                        if (c == '+' || c == '-' || c == '*' || c == '/') {
                            op.add(c);
                            continue;
                        }
                        if (c >= '0' && c <= '9') continue;
                        formatOk = false;
                    }
                    if (val.length != op.size() + 1) formatOk = false;

                    System.out.println(Arrays.toString(val));
                    System.out.println(op);

                    if (formatOk) {
                        double res = parseInt(val[0]);
                        for (int i = 0; i < op.size(); ++i) {
                            double newVal = parseInt(val[i + 1]);
                            switch (op.get(i)) {
                                case '*':
                                    res *= newVal;
                                    break;
                                case '/':
                                    res /= newVal;
                                    break;
                                case '+':
                                    res += newVal;
                                    break;
                                case '-':
                                    res -= newVal;
                                    break;
                            }
                            System.out.println(res);
                        }
                        writer.write("resultat : " + res + "\n");
                    } else {
                        writer.write("format incorrect\n");
                    }

                }
                writer.flush();
                line = reader.readLine();
            }

            writer.close();
            reader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            clientSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}