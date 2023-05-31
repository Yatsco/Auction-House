/**
 * 5/10/20
 * Jarek,Jack, Alana
 * CS 351
 **/

package auction.bank;

import auction.Db;
import auction.Message;
import auction.Message.Type;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class BankProtocol implements Runnable {
    private final Socket clientSocket;
    private final PrintWriter out;
    private final BufferedReader in;
    private final Bank bank;

    public BankProtocol(Socket clientSocket, Bank bank) throws IOException {
        this.clientSocket = clientSocket;
        this.bank = bank;
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(
                clientSocket.getInputStream()));
    }

    @Override
    public void run() {
        while (true) {
            try {
                String agentId, agentHost;
                String msgLine = in.readLine();
                if (msgLine != null) {
                    Message message = Message.fromString(msgLine);
                    Db.out("BankProtocol::run: " + message);
                    Message outMessage = null;
                    switch (message.getType()) {

                        case REGISTER_HOUSE:
                            String houseId = bank.getNextHouseId();
                            String houseHost = clientSocket.getInetAddress()
                                    .getHostName();
                            bank.registerHouse(houseId, houseHost);
                            outMessage = new Message(Message.Type.RETURN,
                                    houseId);
                            break;

                        case REGISTER_AGENT:
                            agentId = bank.getNextAgentId();
                            agentHost = clientSocket.getInetAddress()
                                    .getHostName();
                            bank.registerAgent(agentId, agentHost,
                                    message.getData1(),
                                    Integer.parseInt(message.getData2()));
                            outMessage = new Message(Message.Type.RETURN,
                                    agentId,
                                    String.valueOf(
                                            bank.getAgentAccountBalance(agentId)),
                                    String.valueOf(
                                            bank.getAgentAvailableBalance(agentId)));
                            break;

                        case QUERY_AGENTS:
                            String agentIds = bank.getAgentIds().toString();
                            outMessage = new Message(Message.Type.RETURN,
                                    agentIds);
                            break;

                        case QUERY_HOUSES:
                            String houseIds = bank.getHouseIds().toString();
                            outMessage = new Message(Message.Type.RETURN,
                                    houseIds);
                            break;

                        case QUERY_HOST:
                            String id = message.getData1();
                            String host = bank.getHost(id);
                            Db.out("Query host: " + host);
                            outMessage = new Message(Message.Type.RETURN, host);
                            break;

                        case QUERY_AGENT_ACCOUNT_BALANCE:
                            agentId = message.getData1();
                            int balance = bank.getAgentAccountBalance(agentId);
                            int available = bank
                                    .getAgentAvailableBalance(agentId);
                            Db.out("Query Account Balance: " + agentId + " $"
                                    + balance);
                            outMessage = new Message(Message.Type.RETURN,
                                    Integer.toString(balance),
                                    Integer.toString(available));
                            break;

                        case QUERY_HOUSE_ACCOUNT_BALANCE:
                            houseId = message.getData1();
                            int houseBalance = bank
                                    .getHouseAccountBalance(houseId);
                            Db.out("Query Account Balance: " + houseId + " $"
                                    + houseBalance);
                            outMessage = new Message(Message.Type.RETURN,
                                    Integer.toString(houseBalance));
                            break;

                        case HOLD_FUNDS:
                            boolean goodHold = bank
                                    .holdBidFunds(message.getData1(),
                                            Integer.parseInt(
                                                    message.getData2()));
                            String response = String.valueOf(goodHold);
                            outMessage = new Message(Type.RETURN, response);
                            break;

                        case RELEASE_FUNDS:
                            String released = String
                                    .valueOf(bank.releaseBidFunds(
                                            message.getData1(), Integer.parseInt(
                                                    message.getData2())));
                            outMessage = new Message(Type.RETURN, released);
                            break;

                        case TRANSFER_FUNDS:
                            Db.out("BP TRANSFER_FUNDS:" + message);
                            agentId = message.getData1();
                            houseId = message.getData2();
                            String md3 = message.getData3();
                            int amount = Integer.parseInt(md3);
                            boolean accountPaid =
                                    bank.transferAgentFundsToHouse(agentId,
                                            houseId, amount);

                            int agentBalance = bank
                                    .getAgentAccountBalance(agentId);
                            int houseBalance2 = bank
                                    .getHouseAccountBalance(houseId);
                            Db.out("Bank TRANSFER_FUNDS: $" + amount +
                                    " agentBalance: $" + agentBalance + " $"
                                    + houseBalance2);
                            outMessage = new Message(Message.Type.RETURN,
                                    Integer.toString(agentBalance),
                                    Integer.toString(houseBalance2),
                                    String.valueOf(accountPaid));
                            break;

                        case REQUEST_ID:
                            outMessage = new Message(Message.Type.RETURN,
                                    bank.getId());
                            break;

                        default:
                            Db.out("BankProtocol message not recognized: "
                                    + message);
                    }
                    if (outMessage != null) {
                        out.println(outMessage.toString());
                    }
                }
            } catch (IOException e) {
//                e.printStackTrace();
                try {
                    clientSocket.close();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }

        }
    }
}
