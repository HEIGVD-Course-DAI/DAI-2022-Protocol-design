package ch.heigvd.api.calc;

import java.io.*;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.net.Socket;

/**
 * Calculator client implementation
 */
public class Client {
    private Socket socket;
    private BufferedReader is;
    private BufferedWriter os;

    /**
     * Main function to run client
     *
     * @param args no args required
     */
    public static void main(String[] args) {
        Client client = new Client();
        BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
        if (client.connect("localhost", 2022)) {
            while (client.isConnected()) {
                try {
                    System.out.println(client.write(stdin.readLine()));
                } catch (IOException ioe) {
                    System.err.println("Error reading the input");
                } catch (RuntimeException re) {
                    System.err.println(re.getMessage());
                    break;
                }
            }
        }
    }

    /**
     * Connecxion status to the server
     * @return
     */
    public boolean isConnected(){
        return !socket.isClosed();
    }

    /**
     * Writes the request and waits for the response
     *
     * @param request the request to write
     * @return The server response
     */
    public String write(String request) {
        try {
            //Don't forget the endline, otherwise the server won't read the request
            os.write(request.concat("\n"));
            os.flush();
        } catch (IOException ioe_os) {
            throw new RuntimeException("An I/O error occurred writing the request");
        }
        try {
            //Read the response
            return is.readLine();
        } catch (IOException ioe_is) {
            throw new RuntimeException("An I/O error occurred reading the response");
        }
    }

    /**
     * Connects to the host server
     *
     * @param host The host address
     * @param port The host port
     * @return Wether the connection has been established or not
     */
    boolean connect(String host, int port) {
        try {
            this.socket = new Socket(host, port);
            os = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(),
                    StandardCharsets.UTF_8));
            is = new BufferedReader(new InputStreamReader(socket.getInputStream(),
                    StandardCharsets.UTF_8));
            return true;
        } catch (UnknownHostException uhe) {
            System.err.println("Cannot connect to the host : " + host);
            return false;
        } catch (IOException ioe) {
            System.err.println("An I/O error occurred when creating the socket");
            return false;
        } catch (IllegalArgumentException iae) {
            System.err.println("The given port (" + port + ") is not allowed");
            return false;
        } catch (SecurityException se) {
            System.err.println("Security exception occurred during connexion");
            return false;
        }
    }
}
