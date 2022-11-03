package ch.heigvd.api.calc;

import java.io.*;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Calculator client implementation
 */

public class Client {


    private static final Logger LOG = Logger.getLogger(Client.class.getName());
    private static final int BUFFER_SIZE = 1024;
    private static byte[] buffer = new byte[BUFFER_SIZE];
    private static final String IP = "localhost";
    private static final int PORT = 420;

    /**
     * Main function to run client
     *
     * @param args no args required
     */

    public static void main(String[] args) {
        // Log output on a single line
        System.setProperty("java.util.logging.SimpleFormatter.format", "%4$s: %5$s%6$s%n");

        try {
            Socket clientSocket = new Socket(IP, PORT);
            InputStream fromServer = clientSocket.getInputStream();
            OutputStream toServer = clientSocket.getOutputStream();

            //welcome message from the server
            LOG.log(Level.INFO, "Response sent by the server: ");
            int read = fromServer.read(buffer);
            String response = new String(buffer, 0, read);
            LOG.log(Level.INFO, response);

            while (true) {

                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                String input = br.readLine();
                if (input.equals("exit")) break;
                input = input + "\n";
                toServer.write(input.getBytes());
                toServer.flush();

                //get server respones to query
                read = fromServer.read(buffer);
                response = new String(buffer, 0, read);
                LOG.log(Level.INFO, response);
            }
            clientSocket.close();
        } catch (IOException e) {
            throw new RuntimeException("Error could create a socket form " + IP + " on port " + PORT + ".\nError:", e);
        }
    }
}
