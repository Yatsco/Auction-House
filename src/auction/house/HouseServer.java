/**
 * 5/10/20
 * Jarek,Jack, Alana
 * CS 351
 **/
package auction.house;

import auction.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class HouseServer {
    private static String bankHost;
    private static final int bankPort = 8000;

    public static String registerWithBank() {
        try {
            Socket socket = new Socket(bankHost, bankPort);
            PrintWriter out = new PrintWriter(socket.getOutputStream(),
                    true);
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    socket.getInputStream()));
            Message message = new Message(Message.Type.REGISTER_HOUSE);
            out.println(message.toString());
            String reply = in.readLine();
            return reply;
        } catch (IOException e) {
            e.printStackTrace();
            return "registerWithBank failed.";
        }
    }

    public static void main(String[] args) throws IOException {
        int portNumber = Integer.parseInt(args[0]);
        if (args.length == 2) {
            bankHost = args[1];
        } else {
            bankHost = "127.0.0.1";
        }
        ServerSocket serverSocket = new ServerSocket(portNumber);
        String reply = registerWithBank();
        Message message = Message.fromString(reply);
        String houseId = message.getData1();
        House house = new House(houseId);

        System.out.println("Listening on: " + portNumber);
        while (true) {
            Socket clientSocket = serverSocket.accept();
            HouseProtocol protocol = new HouseProtocol(clientSocket, house);
            Thread t = new Thread(protocol);
            t.start();
        }
    }

    public static String getBankHost() {
        return bankHost;
    }

    public static int getBankPort() {
        return bankPort;
    }
}
