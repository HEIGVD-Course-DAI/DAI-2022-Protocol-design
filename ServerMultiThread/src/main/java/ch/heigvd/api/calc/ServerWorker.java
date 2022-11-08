package ch.heigvd.api.calc;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Calculator worker implementation
 */
public class ServerWorker implements Runnable {
    private final String HANDELED_OPERATORS = "ADD,MULT,SUB,DIV";
    private final String ENCODING = "UTF-8";
    private final String FORMAT = "OPERATOR,OP1,OP2 or OP1,OPERATOR,OP2";
    private final String welcomeMessage = "Welcome to the calculator server.\n" +
            "Syntax must be:" + FORMAT + "\nValid operations are: " + HANDELED_OPERATORS + "\nPlease enter a command:";
    private final String errorMessage = "Invalid command: syntax can be one of the following:" + FORMAT
            + "Valid operation are " + HANDELED_OPERATORS + "\nPlease enter a command:";

    private final String exitMessage = "Bye bye!";

    private final static Logger LOG = Logger.getLogger(ServerWorker.class.getName());
    private BufferedReader fromClient;
    private BufferedWriter toClient;
    private Socket clientSocket;

    /**
     * Instantiation of a new worker mapped to a socket
     *
     * @param clientSocket connected to worker
     */
    public ServerWorker(Socket clientSocket) {
        // Log output on a single line
        System.setProperty("java.util.logging.SimpleFormatter.format", "%4$s: %5$s%6$s%n");
        this.clientSocket = clientSocket;
        Server.clientCount++;
        try {
            fromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), ENCODING));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            toClient = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream(), ENCODING));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Run method of the thread.
     */
    @Override
    public void run() {
        //welcome client
        try {
            toClient.write(welcomeMessage, 0, welcomeMessage.length());
            toClient.flush();
            LOG.log(Level.INFO, "Sent data to client, doing a pause...");

            //read client input
            while (clientSocket.isConnected()) {
                String command = fromClient.readLine();
                if(command == null || command.equals("exit")){

                    //say goodbye to client
                    LOG.log(Level.INFO, "closing connection: ");
                    toClient.write(exitMessage, 0, exitMessage.length());
                    toClient.flush();
                    LOG.log(Level.INFO, "Client disconnected");
                    clientSocket.close();

                    break;
                }
                LOG.log(Level.INFO, "Received command: " + command);
                String response = handleCommand(command) + "\nexit to quit or enter a new command:";

                // Send response to client
                toClient.write(response, 0, response.length());
                toClient.flush();
                LOG.log(Level.INFO, "Sending response: " + response);

            }

            Server.clientCount--;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Handle a single command from the client.
     *
     * @param command received from the client.
     * @return the result of the command.
     */
    private String handleCommand(String command) {
        command = command.replace("\n", "");
        String[] commands = command.split(" ");
        if (commands.length != 3) {
            return errorMessage;
        }
        //basic notation
        int n1;
        int n2;
        try {
            n1 = Integer.parseInt(commands[0]);
            n2 = Integer.parseInt(commands[2]);
            return operationResult(commands[1], n1, n2);
        } catch (NumberFormatException e) {
            //do nothing
        }
        //Polish notation
        try {
            n1 = Integer.parseInt(commands[1]);
            n2 = Integer.parseInt(commands[2]);
            return operationResult(commands[0], n1, n2);
        } catch (NumberFormatException e) {
            //do nothing
        }
        return errorMessage;
    }

    /**
     * Handle a single operation from the client.
     *
     * @param operation received from the client.
     * @return the result of the operation.
     */
    public String operationResult(String operation, int n1, int n2) {
        switch (operation) {
            case "ADD":
                return String.valueOf(n1 + n2);
            case "SUB":
                return String.valueOf(n1 - n2);
            case "MULT":
                return String.valueOf(n1 * n2);
            case "DIV":
                return String.valueOf(n1 / n2);
            default:
                return errorMessage;
        }
    }
}