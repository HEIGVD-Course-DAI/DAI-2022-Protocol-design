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

        // Login Information
        final int PORT_NUMBER = 4000;
        final String HOST = "localhost";

        // Log output on a single line
        System.setProperty("java.util.logging.SimpleFormatter.format", "%4$s: %5$s%6$s%n");
        BufferedReader stdin = null;
        Socket clientSocket = null;
        BufferedWriter writer = null;
        BufferedReader reader = null;

        try {
            // Throws an exception if it fails to connect
            clientSocket = new Socket(HOST, PORT_NUMBER);
            System.out.println("Connection établie, suivez les opérations proposées par le serveur");
            System.out.println("Ecrivez \"QUIT\" pour fermer la connexion, informations du serveur :");

            // Buffer to receive and send information to the server
            writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream(), StandardCharsets.UTF_8));
            reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), StandardCharsets.UTF_8));
            stdin = new BufferedReader(new InputStreamReader(System.in));

            String send = "";
            // Loop until user requests end
            while (!send.equals("QUIT")) {
                System.out.println(reader.readLine());
                send = stdin.readLine();

                if (!send.equals("QUIT")) {
                    send = "CALCUL " + send;;
                }
                // Send to server
                writer.write(send + "\n");
                writer.flush();
            };

        } catch (IOException ex) {
            LOG.log(Level.SEVERE, ex.getMessage());
        } finally {
            // Use DumbHttpClient.java as example
            try {
                if(writer != null) writer.close();
            } catch (IOException ex) {
                LOG.log(Level.SEVERE, ex.getMessage());
            }
            try {
                if(reader != null) reader.close();
            } catch (IOException ex) {
                LOG.log(Level.SEVERE, ex.getMessage());
            }
            try {
                if(clientSocket != null) clientSocket.close();
                System.out.println("Connexion fermée");
            } catch (IOException ex) {
                LOG.log(Level.SEVERE, ex.getMessage());

            }
        }
    }
}
