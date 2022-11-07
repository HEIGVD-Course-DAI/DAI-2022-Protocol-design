package ch.heigvd.api.calc;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Calculator worker implementation
 */
public class ServerWorker implements Runnable {

    private final static Logger LOG = Logger.getLogger(ServerWorker.class.getName());
    private Socket clientSocket;
    private BufferedReader in = null;
    private BufferedWriter out = null;

    /**
     * Instantiation of a new worker mapped to a socket
     *
     * @param clientSocket connected to worker
     */
    public ServerWorker(Socket clientSocket) {
        // Log output on a single line
        System.setProperty("java.util.logging.SimpleFormatter.format", "%4$s: %5$s%6$s%n");
        this.clientSocket = clientSocket;
    }

    /**
     * Run method of the thread.
     */
    @Override
    public void run() {
        while (!clientSocket.isClosed()) {
            try {
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                String msg = in.readLine();
                System.out.println(msg);

                if (msg.startsWith("CLOSE")) {
                    out.write("Closing connexion. Goodbye Jarod!\n");
                    out.flush();
                    clientSocket.close();
                    in.close();
                    out.close();
                } else if(msg.startsWith("COMPUTE")) {
                    msg = msg.substring(7);
                    try{
                        Double result = processOperation(msg);
                        if (result.isNaN())
                            out.write("ERROR SYNTAX\n");
                        else
                            out.write("OK RESULT " + result + "\n");
                    }
                    catch(RuntimeException e){
                        out.write(e.getMessage());
                    }
                    out.flush();
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private Double processOperation(String msg)throws RuntimeException {
        Stack<Double> valStack = new Stack<>();
        Stack<Character> opStack = new Stack<>();

        for (int i = 0; i < msg.length(); ++i) {
            char c = msg.charAt(i);
            if (c == ' ' || c == '(') {
                //Do nothing
            } else if (Character.isDigit(c)) {
                // Concat all next digital char to compose a number
                int nextCharIndex = 0;
                StringBuilder s = new StringBuilder();
                while (Character.isDigit(msg.charAt(i + nextCharIndex))) {
                    s.append(msg.charAt(i + nextCharIndex));
                    nextCharIndex++;
                }
                //Increment the main index
                i += nextCharIndex - 1;
                // Push the number in the value stack
                valStack.push(Double.parseDouble(s.toString()));
                //Delete the temporary formed string
                s.delete(0, s.length());
            } else if (c == ')') {
                double v = valStack.pop();
                char op = opStack.pop();
                if (op == '+') {
                    v = valStack.pop() + v;
                } else if (op == '-') {
                    v = valStack.pop() - v;
                } else if (op == '*') {
                    v = valStack.pop() * v;
                } else if (op == '/') {
                    if(v == 0)
                        throw new RuntimeException("ERROR DEFINITION_DOMAIN\n");
                    v = valStack.pop() / v;
                }
                valStack.push(v);
            } else if (c == '+' || c == '-' || c == '*' || c == '/') {
                opStack.push(c);
            } else return Double.NaN;
        }
        return valStack.pop();
    }
}