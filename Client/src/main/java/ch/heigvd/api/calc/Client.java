package ch.heigvd.api.calc;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
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

        stdin = new BufferedReader(new InputStreamReader(System.in));

        Socket socket = null;
        try {
            //Initializes the connexion with the server
            socket = new Socket("192.168.1.150", 9999);

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true, StandardCharsets.UTF_8);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));

            //Starts the connexion
            out.println("💡");

            String operations = in.readLine();
            if (!operations.startsWith("📃")) {
                System.out.println("Expected the list of operations but received : " + operations);
                return;
            }
            //Prints the list of operations available
            System.out.println(operations);

            //Sends a heartbeat every 10 seconds to keep the connexion alive
            Timer timer = new Timer(10000, evt -> {
                out.println("💓");
            });
            timer.setRepeats(true);
            timer.setCoalesce(true);
            timer.start();

            //Sends the lines written in the console to the server
            while (true) {
                String toSend = stdin.readLine();
                //If ❌ is used, we close the current connexion with the server
                if (toSend.equals("❌")) {
                    break;
                }

                out.println(toSend);
                String read = in.readLine();

                //If we receive ❌, we must stop the connexion
                if (read.equals("❌")) {
                    break;
                }

                //Prints the result
                System.out.println(read);
            }

            //Sends the stop connexion to the server
            out.println("❌");
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
