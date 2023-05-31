/**
 * 5/10/20
 * Jarek,Jack, Alana
 * CS 351
 **/
package auction.agent;

import auction.Db;
import auction.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class AgentProtocol implements Runnable {
    private final Socket clientSocket;
    private final PrintWriter out;
    private final BufferedReader in;

    /**
     * constructor
     */
    public AgentProtocol(Socket clientSocket, Agent agent) throws IOException {
        this.clientSocket = clientSocket;
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(
                clientSocket.getInputStream()));
    }

    @Override
    /**
     * runs the agent protocol
     */
    public void run() {
        while (true) {
            try {
                String msgLine = in.readLine();
                Message message = Message.fromString(msgLine);
                Db.out("AgentProtocol::run: " + message);
                Message outMessage = null;
                switch (message.getType()) {
                    case REQUEST_ID:
                        outMessage = new Message(Message.Type.RETURN);
                        break;
                }
                if (outMessage != null) {
                    out.println(outMessage.toString());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
