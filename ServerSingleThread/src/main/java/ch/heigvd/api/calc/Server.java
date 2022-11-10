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
    private BufferedReader in;
    private BufferedWriter out;
    private final static Logger LOG = Logger.getLogger(Server.class.getName());

    /**
     * Main function to start the server
     */
    public static void main(String[] args) {
        // Log output on a single line
        System.setProperty("java.util.logging.SimpleFormatter.format", "%4$s: %5$s%6$s%n");

        (new Server()).start();
    }

    /**
     * Start the server on a listening socket.
     */
    private void start() {
        ServerSocket serverSocket;
        Socket clientSocket;
        try {
            serverSocket = new ServerSocket(1313);
            while (true) {
                clientSocket = serverSocket.accept();
                handleClient(clientSocket);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    /**
     * Handle a single client connection: receive commands and send back the result.
     *
     * @param clientSocket with the connection with the individual client.
     */
    private void handleClient(Socket clientSocket) {


        try{
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), "UTF-8"));
            out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            while (!clientSocket.isClosed()) {
                String message = in.readLine();
                System.out.println("message received: " + message);
                if(message.startsWith("CALC")){
                    String[] parts = message.split(" ");
                    if(parts.length != 4){
                        sendError("The CALC command must follow the format CALC <number> <operator> <number>");
                    }else{
                        try{
                            int a = Integer.parseInt(parts[1]);
                            int b = Integer.parseInt(parts[3]);
                            sendResult(a,b,parts[2]);
                        }catch (NumberFormatException e){
                            sendError("the numbers must be integers");
                        }
                    }
                }
                else if (message.startsWith("CLOSE")){
                    in.close();
                    out.close();
                }
                else if(message.startsWith("WELCOME")){
                    sendWelcome();
                }
                else if(message.startsWith("HELP")){
                    sendCommands();
                }
                else{
                    sendError("invalid command");
                }

            }

        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void sendWelcome(){
        try {
            out.write("WELCOME !\n");
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
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void sendCommands(){
        try {
            out.write("COMMANDS :\n");
            out.write("WELCOME : display the welcome message\n");
            out.write("CALC <number> <operator> <number> : available operators are ADD, SUB, MUL and DIV\n");
            out.write("CLOSE : close the connection\n");
            out.write("HELP : display the list of commands\n");
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