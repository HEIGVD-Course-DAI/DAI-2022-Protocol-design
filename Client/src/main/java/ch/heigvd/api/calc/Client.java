package ch.heigvd.api.calc;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
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
        Socket client = null;
        BufferedReader is = null;

        try {
            client = new Socket("ip oscar", 3333);
            stdin = new BufferedReader(new InputStreamReader(System.in));

            PrintWriter out = new PrintWriter(client.getOutputStream(), true,  StandardCharsets.UTF_8);
            is = new BufferedReader(new InputStreamReader(client.getInputStream(), StandardCharsets.UTF_8));

            out.println("WELCOME");

            String welcome = stdin.readLine();
            System.out.println(welcome);
            String operations = stdin.readLine();
            System.out.println(operations);

            String input = "";
            String read = "";
            while (true) {
                input = stdin.readLine();
                input = input.toUpperCase();

                if (input.equals("END CONNECTION")) break;

                out.println(input);
                read = is.readLine();
                System.out.println(read);
            }
            out.println("END CONNECTION");
            client.close();
        } catch (IOException e) {
            LOG.log(Level.SEVERE, null, e);
        }

        stdin = new BufferedReader(new InputStreamReader(System.in));

    }
}
