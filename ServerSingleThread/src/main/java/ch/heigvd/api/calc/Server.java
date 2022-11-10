package ch.heigvd.api.calc;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Calculator server implementation - single threaded
 */
public class Server {

    private final static Logger LOG = Logger.getLogger(Server.class.getName());

    static int port = 12345;
    static char OP[] = {'+', '-', '*', '/'};

    /**
     * Main function to start the server
     */
    public static void main(String[] args) {
        // Log output on a single line
        System.setProperty("java.util.logging.SimpleFormatter.format", "%4$s: %5$s%6$s%n");

        (new Server()).start();
    }

    /**
     * @param operation
     * @return result of operation or "WRONGSYNTAX" on error or "DIV0" on div by 0
     */
    private String caluclate(String operation) {
        String wrongOp = "400 - WRONGSYNTAX";
        String div0 = "601 - DIV0";
        String wrongNum = "602 - INPUT2BIG";
        if (operation.length() < 3)
            return wrongOp;

        int nbOperator = 0;
        char operator = '_';
        for (int i = 0; i < operation.length(); ++i) {
            for (char op : OP)
                if (operation.charAt(i) == op) {
                    operator = operation.charAt(i);
                    ++nbOperator;
                }
        }
        if (nbOperator != 1)
            return wrongOp;

        String int1Str = operation.substring(0, operation.indexOf(operator));
        String int2Str = operation.substring(operation.indexOf(operator) + 1);
        long int1 = 0;
        long int2 = 0;
        try {
            int1 = Long.parseLong(int1Str);
            int2 = Long.parseLong(int2Str);
        } catch (NumberFormatException e){
            return wrongNum;
        }
        if (int2 == 0)
            return div0;

        if (operator == OP[0])
            return Long.toString((int1 + int2));
        if (operator == OP[1])
            return Long.toString((int1 - int2));
        if (operator == OP[2])
            return Long.toString((int1 * int2));
        if (operator == OP[3])
            return Long.toString((int1 / int2));

        return wrongOp;
    }

    /**
     * Start the server on a listening socket.
     */
    private void start() {
        /* TODO: implement the receptionist server here.
         *  The receptionist just creates a server socket and accepts new client connections.
         *  For a new client connection, the actual work is done by the handleClient method below.
         */
        ServerSocket serverSocket;
        Socket clientSocket = null;
        BufferedReader in = null;
        BufferedWriter out = null;

        try {
            serverSocket = new ServerSocket(Server.port);
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
            return;
        }
        while (true) {
            try {
                LOG.log(Level.INFO, "Single-threaded: Waiting for a new client on port {" + Server.port + "}", Server.port);
                clientSocket = serverSocket.accept();
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), "UTF-8"));
                out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream(), "UTF-8"));
                String line;

                out.write("OP " + OP[0] + " " + OP[1] + " " + OP[2] + " " + OP[3] + " END\n");
                out.flush();
                LOG.info("Reading until client sends END or closes the connection...");
                while ((line = in.readLine()) != null) {
                    LOG.info("Client Sent " + line);
                    // ===================   REMETTRE LE \n après le END et - 6 v car pour test avec netcat  ===========
                    if ((line.length() < 4) || !(line.substring(line.length() - 5).equalsIgnoreCase("\" END"))) {
                        out.write("ERROR 400 WSYNTAX END\n");
                        out.flush();
                        break;
                    } else {
                        if (line.substring(0, 6).equalsIgnoreCase("CALC \"")) {
                            // ===================   REMETTRE LE          - 6 v car pour test avec netcat  ===========
                            String operation = line.substring(6, line.length() - 5);
                            LOG.info("CALC RECIEVED " + operation);
                            String result = caluclate(operation);
                            LOG.info("RESULT = " + result);

                            if (result == "WRONGSYNTAX") {
                                out.write("ERROR 400 WSYNTAX END\n");
                                out.flush();
                            } else if (result == "DIV0") {
                                out.write("ERROR 601 DIV0 END\n");
                                out.flush();
                            } else {
                                out.write("RESULT " + result + " END\n");
                                out.flush();
                            }

                        } else {
                            out.write("ERROR 402 EXPECTCALC END\n");
                            out.flush();
                        }
                        out.write("QUIT END\n");
                        out.flush();
                        break;
                    }
                }

            } catch (IOException ex) {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException ex1) {
                        LOG.log(Level.SEVERE, ex1.getMessage(), ex1);
                    }
                }
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException ex1) {
                        LOG.log(Level.SEVERE, ex1.getMessage(), ex1);
                    }
                }
                if (clientSocket != null) {
                    try {
                        clientSocket.close();
                    } catch (IOException ex1) {
                        LOG.log(Level.SEVERE, ex1.getMessage(), ex1);
                    }
                }
                LOG.log(Level.SEVERE, ex.getMessage(), ex);
            }
        }
    }

}
/**
 * Handle a single client connection: receive commands and send back the result.
 *
 * @param clientSocket with the connection with the individual client.
 */


    /*
    private void handleClient() {

        /* TODO: implement the handling of a client connection according to the specification.
         *   The server has to do the following:
         *   - initialize the dialog according to the specification (for example send the list
         *     of possible commands)
         *   - In a loop:
         *     - Read a message from the input stream (using BufferedReader.readLine)
         *     - Handle the message
         *     - Send to result to the client
         */
        /*
        while (true) {
            try {
                LOG.log(Level.INFO, "Single-threaded: Waiting for a new client on port {" + Server.port + "}", Server.port);
                clientSocket = serverSocket.accept();
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), "UTF-8"));
                out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream(), "UTF-8"));
                String line;

                out.write("Welcome to the Single-Threaded Server.\nAvaliable OP are " + OP[0] + " " + OP[1] + "\n");
                out.flush();
                LOG.info("Reading until client sends END or closes the connection...");
                while ((line = in.readLine()) != null) {
                    if (line.length() < 3) {
                    } else if (line.substring(line.length() - 3).equalsIgnoreCase("END")) {
                        break;
                    }
                    out.write("> " + line.toUpperCase() + "\n");
                    out.flush();
                }

            } catch (IOException ex) {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException ex1) {
                        LOG.log(Level.SEVERE, ex1.getMessage(), ex1);
                    }
                }
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException ex1) {
                        LOG.log(Level.SEVERE, ex1.getMessage(), ex1);
                    }
                }
                if (clientSocket != null) {
                    try {
                        clientSocket.close();
                    } catch (IOException ex1) {
                        LOG.log(Level.SEVERE, ex1.getMessage(), ex1);
                    }
                }
                LOG.log(Level.SEVERE, ex.getMessage(), ex);
            }
        }

    }
    */
