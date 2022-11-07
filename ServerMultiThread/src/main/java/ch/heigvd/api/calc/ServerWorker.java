package ch.heigvd.api.calc;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Timer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Calculator worker implementation
 */
public class ServerWorker implements Runnable {

    private final static Logger LOG = Logger.getLogger(ServerWorker.class.getName());
    private BufferedReader in;
    private BufferedWriter out;
    /**
     * Instantiation of a new worker mapped to a socket
     *
     * @param clientSocket connected to worker
     */
    public ServerWorker(Socket clientSocket) {
        // Log output on a single line
        System.setProperty("java.util.logging.SimpleFormatter.format", "%4$s: %5$s%6$s%n");

        /* TODO: prepare everything for the ServerWorker to run when the
         *   server calls the ServerWorker.run method.
         *   Don't call the ServerWorker.run method here. It has to be called from the Server.
         */
        try {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), "UTF-8"));
            out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Run method of the thread.
     */
    @Override
    public void run() {

        /* TODO: implement the handling of a client connection according to the specification.
         *   The server has to do the following:
         *   - initialize the dialog according to the specification (for example send the list
         *     of possible commands)
         *   - In a loop:
         *     - Read a message from the input stream (using BufferedReader.readLine)
         *     - Handle the message
         *     - Send to result to the client
         */
        boolean connectionActive = true;
        boolean firstTime = true;
            while (connectionActive) {
                try{
                String message = in.readLine().toUpperCase();
                System.out.println("message received: " + message);
                if (firstTime) {
                    if (!message.equals("WELCOME")) {
                        sendError("invalid first command");
                    } else {
                        firstTime = false;
                        sendWelcome();
                    }
                }
                if(message.startsWith("CALC")){
                    String[] parts = message.split(" ");
                    if(parts.length != 4){
                        sendError("The CALC command must follow the format CALC <number> <operator> <number>");
                    }else{
                        try{
                            int a = Integer.parseInt(parts[2]);
                            int b = Integer.parseInt(parts[3]);
                            sendResult(a,b,parts[1]);
                        }catch (NumberFormatException e){
                            sendError("the numbers must be integers");
                        }
                    }
                }
                if(message.startsWith("CLOSE")){
                    connectionActive = false;
                    firstTime = true;
                }

            } catch(IOException e){
                throw new RuntimeException(e);
            }

        }
    }
    public void sendWelcome(){
        try {
            out.write("WELCOME !");
            out.newLine();
            out.flush();
            sendCommands();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void sendError(String s){
        try {
            out.write(s);
            out.newLine();
            out.flush();
            sendCommands();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void sendCommands(){
        try {
            out.write("COMMANDS :");
            out.newLine();
            out.write("WELCOME : display the welcome message");
            out.newLine();
            out.write("CALC <number> <operator> <number> : available operators are ADD, SUB, MUL and DIV");
            out.newLine();
            out.write("CLOSE : close the connection");
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void sendResult(int a, int b, String operator){
        try {
            switch (operator) {
                case "ADD":
                    out.write("RESULT : " + (a + b));
                    out.newLine();
                    out.flush();
                    break;
                case "SUB":
                    out.write("RESULT : " + (a - b));
                    out.newLine();
                    out.flush();
                    break;
                case "MUL":
                    out.write("RESULT : " + (a * b));
                    out.newLine();
                    out.flush();
                    break;
                case "DIV":
                    if (b == 0) {
                        sendError("Division by zero is not allowed");
                    } else {
                        out.write("RESULT : " + ((float) a / b));
                        out.newLine();
                        out.flush();
                    }
                    break;
                default:
                    sendError("invalid operator");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}