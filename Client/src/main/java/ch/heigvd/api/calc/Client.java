package ch.heigvd.api.calc;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

/**
 * Calculator client implementation
 */
public class Client {

    //private static final Logger LOG = Logger.getLogger(Client.class.getName());

    public static void execute(BufferedReader stdin, BufferedReader in, BufferedWriter out) {
        /* DONE
         *     - read the command from the user on stdin (already created)
         *     - send the command to the server
         *     - read the response line from the server (using BufferedReader.readLine)
         *
         * TODO Test functionality once the server is done
         */

        try {
            String input = stdin.readLine();
            out.write(input);
            out.flush();
            String result = in.readLine();
            System.out.println("" + input + " = " + result);
        } catch (Exception e) {
            throw new RuntimeException("Couldn't communicate with server.");
        }


    }

    /**
     * Main function to run client
     *
     * @param args no args required
     */
    public static void main(String[] args) {
        // Log output on a single line
        System.setProperty("java.util.logging.SimpleFormatter.format", "%4$s: %5$s%6$s%n");

        BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));

        /* DONE: Implement the client here, according to your specification
         *   The client has to do the following:
         *   - connect to the server
         *   - initialize the dialog with the server according to your specification
         *   - In a loop:
         *     - read the command from the user on stdin (already created)
         *     - send the command to the server
         *     - read the response line from the server (using BufferedReader.readLine)
         */

        boolean running = true;

        try (Socket socket = new Socket("127.0.0.1", 1313);
             BufferedReader fromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             BufferedWriter toServer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))) {
            while (running) {
                execute(stdin, fromServer, toServer);
                System.out.println("Do you want to execute another calculation ? (y/n): ");
                running = stdin.readLine().equalsIgnoreCase("y");
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
