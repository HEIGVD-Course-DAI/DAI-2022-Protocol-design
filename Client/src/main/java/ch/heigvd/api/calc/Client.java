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
    public static void main(String[] args) throws IOException{
        final int PORT_NUMBER = 4000;
        final String HOST = "localhost";
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

        Socket clientSocket = new Socket(HOST, PORT_NUMBER);


        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream(), StandardCharsets.UTF_8));
        BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), StandardCharsets.UTF_8));
        stdin = new BufferedReader(new InputStreamReader(System.in));

        String send;
        do {
            System.out.println(reader.readLine());
            send = stdin.readLine();
            if (!send.equals("QUIT")) {
                send = "CALCUL " + send;
            }
            writer.write(send + "\n");
            writer.flush();
        }while (!send.equals("QUIT"));

        writer.close();
        reader.close();
        clientSocket.close();
    }
}
