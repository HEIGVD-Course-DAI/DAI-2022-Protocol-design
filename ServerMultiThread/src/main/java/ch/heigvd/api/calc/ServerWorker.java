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
    private final char OPEN_PARENTHESIS = '(',
            CLOSE_PARENTHESIS = ')',
            MULTIPLY = '*',
            DIVIDE = '/',
            ADD = '+',
            SUBSTRACT = '-';

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

                        try {
                            if(syntaxIsCorrect(msg)){
                                try{
                                    Double result = processOperation(msg);
                                    out.write("RESULT " + result);
                                }catch(RuntimeException e){
                                    out.write("ERROR DEFINITION_DOMAIN " + e.getMessage());
                                }
                            }
                        } catch (RuntimeException re) {
                            try {
                                out.write("ERROR SYNTAX " + re.getMessage());;
                            }catch (IOException ioe) {
                                ioe.printStackTrace();
                            }
                        }

                    out.flush();
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    boolean syntaxIsCorrect(String msg) throws RuntimeException{
        int     nbOpenParenthesis = 0,
                nbClosedParenthesis = 0,
                nbOperands = 0,
                nbOperator =0;

        StringBuilder stringNumber = new StringBuilder();
        for(int i = 0; i < msg.length(); ++i){
            char c = msg.charAt(i);
            if(c == ' ' || c =='\n'){
                continue;
            }
            if(!Character.isDigit(c)){
                //NotANumber
                if(!stringNumber.toString().equals("")){
                    nbOperands++;
                    stringNumber = new StringBuilder();
                }

                if(c == MULTIPLY || c == DIVIDE || c == ADD || c == SUBSTRACT){
                    nbOperator++;
                }else if(c == OPEN_PARENTHESIS){
                    nbOpenParenthesis++;
                } else if (c == CLOSE_PARENTHESIS) {
                    nbClosedParenthesis++;
                }else{
                    throw new RuntimeException("Invalid character");
                }

            }else{
                stringNumber.append(c);
            }
        }
        if(nbClosedParenthesis != nbOpenParenthesis){
            throw new RuntimeException("Parenthesis mismatch");
        }
        if(nbOperator == 0 || nbOperands == 0 || nbOperator != (nbOperands - 1)){
            throw new RuntimeException("Operand or operator missing");
        }
        return true;
    }
    private Double processOperation(String msg)throws RuntimeException {
        Stack<Double> valStack = new Stack<>();
        Stack<Character> opStack = new Stack<>();
        StringBuilder number = new StringBuilder();

        for (int i = 0; i < msg.length(); ++i) {

            char c = msg.charAt(i);
            if (c == ' ' || c == OPEN_PARENTHESIS) {
                continue;
            }
            if (Character.isDigit(c)) {
                // Concat all next digital char to compose a number
                number.append(c);
            }
            else{
                // Push the number in the value stack
                if(!number.toString().equals("")){
                    valStack.push(Double.parseDouble(number.toString()));
                    number = new StringBuilder();
                }

                if (c == CLOSE_PARENTHESIS) {
                    double v = valStack.pop();
                    char op = opStack.pop();
                    if (op == ADD) {
                        v = valStack.pop() + v;
                    } else if (op == SUBSTRACT) {
                        v = valStack.pop() - v;
                    } else if (op == MULTIPLY) {
                        v = valStack.pop() * v;
                    } else if (op == DIVIDE) {
                        if(v == 0)
                            throw new RuntimeException("Divide by zero\n");
                        v = valStack.pop() / v;
                    }
                    valStack.push(v);
                } else if (c == ADD || c == SUBSTRACT || c == MULTIPLY || c == DIVIDE) {
                    opStack.push(c);
                }
            }
        }
        return valStack.pop();
    }
}