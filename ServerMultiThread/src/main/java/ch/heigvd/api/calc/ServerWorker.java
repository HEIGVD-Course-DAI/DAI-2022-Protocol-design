package ch.heigvd.api.calc;

import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Calculator worker implementation
 */
public class ServerWorker implements Runnable {

    private final static Logger LOG = Logger.getLogger(ServerWorker.class.getName());
    private BufferedReader in;
    private BufferedWriter out;
    private long time;
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

        // try to implement the heart beat ...
        time = System.currentTimeMillis();
        Timer timer = new Timer(10000, evt -> {
            if(System.currentTimeMillis() - time >= 30000){
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        timer.setRepeats(true);
        timer.setCoalesce(true);
        timer.start();

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

        boolean connectionRunning = false;

        try {
            while (true) {
                String s = in.readLine();
                System.out.println(s);
                if (!connectionRunning) {
                    if(!s.equals("💡")){
                        sendError("Bad connection message");
                    } else {
                        sendListOperation();
                        connectionRunning = true;
                    }
                    continue;
                }

                // How to implement
                if(s.equals("💓")){
                    time = System.currentTimeMillis();
                    continue;
                }

                if(s.startsWith("✖")){
                    final int numberOfParams = 2;
                    int[] values;
                    try {
                        values= getParameters(s, numberOfParams);
                    } catch (Exception e) {
                        sendError(e.getMessage());
                        continue;
                    }

                    out.write("🟰 " + (values[0] * values[1]) + "\n");
                    out.flush();
                    continue;
                }

                if(s.startsWith("➕")){
                    final int numberOfParams = 2;
                    int[] values;
                    try {
                        values= getParameters(s, numberOfParams);
                    } catch (Exception e) {
                        sendError(e.getMessage());
                        continue;
                    }

                    out.write("🟰 " + (values[0] + values[1]) + "\n");
                    out.flush();
                    continue;
                }
                if(s.equals("❌")){
                    break;
                }

                sendError("Not supported :" + s);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendError(String message) throws IOException {
        out.write("💥 " + message + "\n");
        out.flush();
    }

    private void sendListOperation() throws IOException {
        out.write("📃 ➕, ✖\n");
        out.flush();
    }

    private int[] getParameters(String in, int numberOfParams) {
        int[] res = new int[numberOfParams];
        String[] params = in.split(" ");
        if(params.length - 1 != numberOfParams) {
            throw new RuntimeException("Bad number of params");
        }

        for (int i = 0; i < numberOfParams; i++) {
            res[i] = Integer.parseInt(params[i + 1]);
        }

        return res;
    }


}