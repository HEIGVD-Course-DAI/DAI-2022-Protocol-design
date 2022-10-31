package ch.heigvd.api.calc;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Calculator client implementation
 */
public class Client {

    private static final Logger LOG = Logger.getLogger(Client.class.getName());

    /**
     * Main function to run client
     *
     * @param args no args required
     */
    public static void main(String[] args) {
        // Log output on a single line
        System.setProperty("java.util.logging.SimpleFormatter.format", "%4$s: %5$s%6$s%n");

        BufferedReader stdin = null;

        /* TODO: Implement the client here, according to your specification
         *   The client has to do the following:
         *   - connect to the server
         *   - initialize the dialog with the server according to your specification
         *   - In a loop:
         *     - read the command from the user on stdin (already created)
         *     - send the command to the server
         *     - read the response line from the server (using BufferedReader.readLine)
         */
        final int LISTEN_PORT = 3333;
        final String QUIT = "END OF CONNECTION";

        Socket clientSocket = null;
        BufferedWriter writer = null;
        BufferedReader reader = null;

        try {
            clientSocket = new Socket("127.0.0.1", LISTEN_PORT);
            writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream(), StandardCharsets.UTF_8));
            reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), StandardCharsets.UTF_8));
            stdin = new BufferedReader(new InputStreamReader(System.in));

            String line;
            do {
                line = reader.readLine();
                System.out.println(line);
            } while (!line.equals("END"));

            boolean reading = true;
            while (reading) {
                writer.write(stdin.readLine() + "\n");
                writer.flush();

                if((line = reader.readLine()).equals(QUIT)) {
                    reading = false;
                }

                System.out.println(line);
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
                if (clientSocket != null) {
                    clientSocket.close();
                }
            } catch (IOException ex) {
                LOG.log(Level.SEVERE, ex.getMessage());
            }
        }
    }
}
