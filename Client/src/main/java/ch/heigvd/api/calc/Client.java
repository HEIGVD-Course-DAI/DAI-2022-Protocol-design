package ch.heigvd.api.calc;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.logging.Logger;

/**
 * Calculator client implementation
 */
public class Client {

    private static final String HOST = "127.0.0.1";
    private static final int PORT = 1313;

    private static boolean isConnected = true;


    private static final Logger LOG = Logger.getLogger(Client.class.getName());

    public static void execute(BufferedReader stdin, BufferedReader in, BufferedWriter out) {
        /* DONE
         *     - read the command from the user on stdin (already created)
         *     - send the command to the server
         *     - read the response line from the server (using BufferedReader.readLine)
         *
         * TODO Test functionality once the server is done
         */
        try {
            String input = "";
            input = stdin.readLine();
            if(input.equals("CLOSE")) {
                isConnected = false;
                return;
            }
            out.write(input + "\n");
            out.flush();
            char[] buffer = new char[1024];
            in.read(buffer, 0, 1024);
            for(int i = 0; i < 1024; i++) {
                if(buffer[i] == '$') {
                System.out.print(new String(buffer, 0, i));
                    break;
                }
            }
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

        try (Socket socket = new Socket(HOST, PORT);
             BufferedReader fromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             BufferedWriter toServer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))) {
            System.out.println("Please write WELCOME to start talking to the server.");
            do {
                execute(stdin, fromServer, toServer);
                /*
                System.out.println("Do you want to execute another calculation ? (y/n): ");
                running = stdin.readLine().equalsIgnoreCase("y");
                 */
            }while (isConnected);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
