/**
 * 5/10/20
 * Jarek,Jack, Alana
 * CS 351
 **/

package auction.agent;

import javafx.application.Application;
import auction.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class AgentServer {
    private static String bankHost;
    private static final int bankPort = 8000;
    private static String agentName;
    private static String initialBalance;
    private static Agent agent;

    /**
     * sets up the agent to the bank server
     *
     * @return
     */
    public static String registerWithBank() {
        try {
            Socket socket = new Socket(bankHost, bankPort);
            PrintWriter out = new PrintWriter(socket.getOutputStream(),
                    true);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            Message message = new Message(Message.Type.REGISTER_AGENT,
                    agentName, initialBalance);
            out.println(message.toString());
            String reply = in.readLine();
            return reply;
        } catch (IOException e) {
            e.printStackTrace();
            return "registerWithBank failed.";
        }
    }

    public static void main(String[] args) throws Exception {
        int portNumber = Integer.parseInt(args[0]);
        ServerSocket serverSocket = new ServerSocket(portNumber);
        if (args.length == 4) {
            bankHost = args[1];
            agentName = args[2];
            initialBalance = args[3];
        } else {
            bankHost = "127.0.0.1";
            agentName = args[1];
            initialBalance = args[2];
        }


        String reply = registerWithBank();
        Message message = Message.fromString(reply);
        String agentId = message.getData1();


        agent = new Agent(agentId, agentName, Integer.parseInt(
                message.getData2()),
                Integer.parseInt(message.getData3()));


        System.out.println("Listening on: " + portNumber);


        Application.launch(AgentMain.class, args);

        while (true) {
            Socket clientSocket = serverSocket.accept();

//            Socket client = serverSocket.accept();
//            String hostName = client.getInetAddress().getHostName();

            AgentProtocol protocol = new AgentProtocol(clientSocket, agent);
            Thread t = new Thread(protocol);
            t.start();

        }

    }

    /**
     * getter
     *
     * @return agent
     */
    public static Agent getAgent() {
        return agent;
    }
}
