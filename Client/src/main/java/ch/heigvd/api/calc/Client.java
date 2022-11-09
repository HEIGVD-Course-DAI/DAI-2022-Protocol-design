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
        String response;
        try {
            if (client.connect("localhost", 2022)) {
                System.out.println(client.read());
                while (client.isConnected()) {
                    //Write request
                    client.write(stdin.readLine());

                    //Read response
                    response = client.read();
                    if(response.contains("OK INFO CONNECTION_CLOSED")){
                        System.out.println("Connexion closed.");
                        break;
                    }
                    System.out.println(response);
                }
            }
        } catch (IOException ioe) {
            System.err.println("Error reading the input");
        } catch (RuntimeException re) {
            System.err.println(re.getMessage());
        }
    }

    /**
     * Connexion status to the server
     *
     * @return if the client is not closed
     */
    public boolean isConnected() {
        return !socket.isClosed();
    }

    /**
     * Writes the request and waits for the response
     *
     * @param request the request to write
     */
    public void write(String request) {
        try {
            //Don't forget the endline, otherwise the server won't read the request
            os.write(request.concat("\n"));
            os.flush();
        } catch (IOException ioe_os) {
            throw new RuntimeException("An I/O error occurred writing the request");
        }
    }

    public String read() throws IOException {
        try {
            StringBuilder response = new StringBuilder();
            while(is.ready())
                response.append(is.readLine().concat("\n"));
            return response.toString();
        } catch (IOException ioe_is) {
            throw new IOException("An I/O error occurred reading the response");
        }
    }

    /**
     * Connects to the host server
     *
     * @param host The host address
     * @param port The host port
     * @return Weather the connection has been established or not
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
