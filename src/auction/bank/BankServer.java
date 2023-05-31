/**
 * 5/10/20
 * Jarek,Jack, Alana
 * CS 351
 **/

package auction.bank;

import auction.Db;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class BankServer {

    public static void main(String[] args) throws IOException {
        int portNumber = Integer.parseInt(args[0]);
        ServerSocket serverSocket = new ServerSocket(portNumber);
        Bank bank = new Bank();

        Db.out("Listening on: " + portNumber);
        while (true) {
            Socket clientSocket = serverSocket.accept();

//            Socket client = serverSocket.accept();
//            String hostName = client.getInetAddress().getHostName();

            BankProtocol protocol = new BankProtocol(clientSocket, bank);
            Thread t = new Thread(protocol);
            t.start();
        }
    }
}
