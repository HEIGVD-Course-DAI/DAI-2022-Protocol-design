package ch.heigvd.api.calc;

import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Calculator worker implementation
 */
public class ServerWorker implements Runnable {

    private final static Logger LOG = Logger.getLogger(ServerWorker.class.getName());

    private BufferedReader in;
    private BufferedWriter out;
    private String[] supportedOp = new String[] {"+","*","/"};



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
        try{
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(),"UTF-8"));
            out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        } catch (IOException e){
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
        boolean connected = false;
        try{
            for(;;){
                String msg = in.readLine().toUpperCase();
                System.out.println(msg);
                if(!connected){
                    if(!msg.equals("WELCOME RECEIVED")){
                        sendError(0, "Wrong connection message");
                    } else {
                        sendOperations();
                        connected = true;
                    }
                    continue;
                }

                // implementation
                if(msg.startsWith("CALCUL")){
                    String[] params = msg.split(" ");
                    if(params.length != 4){
                        //gérer erreur
                        sendError(2,"Syntax error, CALCUL leftValue operand rightValue is the format");
                        continue;
                    }
                    int l, r;
                    try{
                        l = Integer.parseInt(params[1]);
                        r = Integer.parseInt(params[3]);
                    } catch(Exception e){
                        // gerer erreur
                        sendError(2,"Syntax error, CALCUL leftValue operand rightValue is the format");
                        continue;
                    }

                    switch(params[2]){
                        case "+":
                            sendResult(l + r);
                            break;
                        case "/":
                            sendResult(l / r);
                            break;
                        case "*":
                            sendResult(l * r);
                            break;
                        default:
                            // si on arrive ici c'est que l'operation n'est pas géré
                            sendError(1,"Operation not suported");
                            continue;
                    }
                }

                if(msg.startsWith("CONNECTION END")){
                    sendEnd();
                    connected = false;
                }
            }
        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    private void sendResult(int result){
        try{
            out.write("RESULT " + result + "\n");
            out.flush();
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    private void sendError(int nb,String error){
        try{
            out.write("ERROR " + nb + " : " + error + "\n");
            out.flush();
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    private void sendEnd(){
        try{
            out.write("CONNECTION END OK\n");
            out.flush();
        } catch(Exception e){
            throw new RuntimeException(e);
        }
    }
    private void sendOperations(){
        try{
            out.write("LISTOPERATION [ ");
            for(int i = 0; i < supportedOp.length; ++i){
                if(i != 0){
                    out.write(" , ");
                }
                out.write(supportedOp[i]);
            }
            out.write(" ]\n");
        }
        catch(Exception e){
            throw new RuntimeException(e);
        }
    }
}