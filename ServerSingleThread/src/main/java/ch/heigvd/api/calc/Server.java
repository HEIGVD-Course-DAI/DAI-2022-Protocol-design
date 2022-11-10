package ch.heigvd.api.calc;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.IOException;

import javax.script.*;

import static java.lang.Integer.parseInt;


/**
 * Calculator server implementation - single threaded
 */
public class Server {

    private final static Logger LOG = Logger.getLogger(Server.class.getName());

    /**
     * Main function to start the server
     */
    public static void main(String[] args) throws Exception {
        // Log output on a single line
        System.setProperty("java.util.logging.SimpleFormatter.format", "%4$s: %5$s%6$s%n");

        (new Server()).start();
    }




    /**
     * Start the server on a listening socket.
     */
    private void start() throws Exception {
        /* TODO: implement the receptionist server here.
         *  The receptionist just creates a server socket and accepts new client connections.
         *  For a new client connection, the actual work is done by the handleClient method below.
         */
        ServerSocket serverSocket = new ServerSocket(7777);
        Socket clientSocket = null;
        while(true){
            System.out.println("Waiting client...");
            clientSocket = serverSocket.accept();
            System.out.println("client trouvé...");
            handleClient(clientSocket);
            clientSocket.close();
        }
    }

    /**
     * Handle a single client connection: receive commands and send back the result.
     *
     * @param clientSocket with the connection with the individual client.
     */
    private void handleClient(Socket clientSocket)throws Exception {

        /* TODO: implement the handling of a client connection according to the specification.
         *   The server has to do the following:
         *   - initialize the dialog according to the specification (for example send the list
         *     of possible commands)
         *   - In a loop:
         *     - Read a message from the input stream (using BufferedReader.readLine)
         *     - Handle the message
         *     - Send to result to the client
         */
        BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), "UTF-8"));
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream(), "UTF-8"));

        String line = reader.readLine();
        while(true) {
            if (Objects.equals(line, "close")) {
                break;
            }else if (Objects.equals(line, "open")) {
                writer.write(("connection ok\n"));
            }else if(Objects.equals(line, "help")){
                writer.write("Bonjour, entrez votre calcul, les opérations supporée sont + - * / ex. |3 * 4 + 5|, " +
                        "mon développeur étant paresseux les paranthèse ne sont pas traitée ni la priorité des opérations\n");
            }else{
                line = line.replaceAll("\\s+", "");
                String[] val =line.split("[*/+-]");
                List<Character> op = new ArrayList<>();
                boolean formatOk = true;
                for (int i = 0; i < line.length(); ++i){
                    char c = line.charAt(i);
                    if(c == '+' || c == '-' ||c == '*' ||c == '/'){
                        op.add(c);
                        continue;
                    }
                    if(c >= '0' && c <= '9')continue;
                    formatOk = false;
                }
                if(val.length != op.size() + 1)formatOk = false;

                System.out.println(Arrays.toString(val));
                System.out.println(op);

                if(formatOk){
                    double res = parseInt(val[0]);
                    for(int i = 0;i < op.size();++i){
                        double newVal = parseInt(val[i+1]);
                        switch (op.get(i)){
                            case '*':
                                res *= newVal;
                                break;
                            case '/':
                                res /= newVal;
                                break;
                            case '+':
                                res += newVal;
                                break;
                            case '-':
                                res -= newVal;
                                break;
                        }
                        System.out.println(res);
                    }
                    writer.write("resultat : " + res + "\n");
                }else{
                    writer.write("format incorrect\n");
                }

            }
            writer.flush();
            line = reader.readLine();
        }

        writer.close();
        reader.close();
    }
}

