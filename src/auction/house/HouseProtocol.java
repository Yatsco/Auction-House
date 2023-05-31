/**
 * 5/10/20
 * Jarek,Jack, Alana
 * CS 351
 **/

package auction.house;

import auction.Db;
import auction.Message;
import auction.Message.Type;
import auction.house.Bid.BidStatus;

import java.io.*;
import java.net.Socket;
import java.util.Map;

public class HouseProtocol implements Runnable, Closeable {
    private final Socket clientSocket;
    private final House house;
    private final PrintWriter out;
    private final BufferedReader in;

    /**
     * Constructor
     * @param clientSocket connecting socket
     * @param house House with access to items list and house functions
     * @throws IOException
     */
    public HouseProtocol(Socket clientSocket, House house) throws IOException {
        this.clientSocket = clientSocket;
        this.house = house;
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(
                clientSocket.getInputStream()));
    }

    /**
     * Handles incoming messages
     */
    @Override
    public void run() {
        while (true) {
            try {
                String msgLine = in.readLine();
                if (msgLine != null) {
                    Message message = Message.fromString(msgLine);

                    Db.out("HouseProtocol::run: " + message);
                    Message outMessage = null;
                    switch (message.getType()) {
                        case REQUEST_ID:
                            outMessage = new Message(Message.Type.RETURN);
                            break;

                        case QUERY_ITEMS:
                            String itemsIds = house.getItemIds().toString();
                            outMessage = new Message(Message.Type.RETURN,
                                    itemsIds);
                            break;

                        case QUERY_ITEM:
                            String itemId = message.getData1();
                            if (house.getItemIds().contains(itemId)) {
                                Item item = house.getItem(itemId);
                                String nameAndDescript = item.makeDescription();
                                int bidAmount = item.getBidAmount();
                                String currentAgentId = item
                                        .getCurrentAgentHighBidId();
                                int minBid = item.getMinimumBid();
                                outMessage = new Message(Message.Type.RETURN,
                                        nameAndDescript,
                                        Integer.toString(bidAmount), currentAgentId,
                                        String.valueOf(minBid));
                            } else {
                                outMessage = new Message(Type.RETURN,
                                        "none");
                            }

                            break;

                        case MAKE_BID:

                            int agentBid = Integer.parseInt(message.getData4());
                            String bidItemId = message.getData2();
                            String bidAgentId = message.getData3();

                            Message holdFundsMessage = null;
                            Socket bankSocket = new Socket(
                                    HouseServer.getBankHost(),
                                    HouseServer.getBankPort());
                            PrintWriter bankOut = new PrintWriter(
                                    bankSocket.getOutputStream(), true);
                            BufferedReader bankIn = new BufferedReader(
                                    new InputStreamReader(
                                            bankSocket.getInputStream()));
                            String bankResponse;
                            Message releaseFundsMessage = null;

                            boolean isFirstBid = house.getItem(bidItemId)
                                    .isFirstBid();
                            int currentBid = house.getItem(bidItemId)
                                    .getBidAmount();
                            boolean isSold = house.getItem(bidItemId).isSold();
                            boolean isWon = house.getItem(bidItemId).isWon();
                            if (isSold || isWon) {
                                outMessage = new Message(Message.Type.RETURN,
                                        BidStatus.LOST.name());
                            } else if ((isFirstBid && currentBid <= agentBid) ||
                                    (!isFirstBid && agentBid > currentBid)) {
                                holdFundsMessage = new Message(
                                        Message.Type.HOLD_FUNDS, bidAgentId,
                                        message.getData4());
                                bankOut.println(holdFundsMessage);
                                bankResponse = bankIn.readLine();
                                Message bankMessage = Message
                                        .fromString(bankResponse);
                                if (Boolean.valueOf(bankMessage.getData1())
                                        && isFirstBid) {
                                    System.out
                                            .println("FIRST BID ON: " + bidItemId);
                                    outMessage = new Message(
                                            Message.Type.RETURN,
                                            BidStatus.ACCEPTED.name());
                                    house.getItem(bidItemId).setFirstBid(false);
                                    house.getItem(bidItemId)
                                            .setBidAmount(agentBid);
                                    house.getItem(bidItemId)
                                            .setCurrentAgentHighBidId(bidAgentId);
                                } else if (
                                        Boolean.valueOf(bankMessage.getData1())
                                                && !isFirstBid) {
                                    outMessage = new Message(
                                            Message.Type.RETURN,
                                            BidStatus.ACCEPTED.name());
                                    System.out.println(
                                            "RELEASE FUNDS FOR: " + house
                                                    .getItem(bidItemId)
                                                    .getCurrentAgentHighBidId());
                                    releaseFundsMessage = new
                                            Message(Message.Type.RELEASE_FUNDS,
                                            house.getItem(bidItemId)
                                                    .getCurrentAgentHighBidId(),
                                            String.valueOf(currentBid));
                                    bankOut.println(releaseFundsMessage);

                                    house.getItem(bidItemId)
                                            .setBidAmount(agentBid);
                                    house.getItem(bidItemId)
                                            .setCurrentAgentHighBidId(bidAgentId);
                                } else {
                                    outMessage = new Message(
                                            Message.Type.RETURN,
                                            BidStatus.REJECTED.name());
                                }
                            } else {
                                outMessage = new Message(Message.Type.RETURN,
                                        BidStatus.REJECTED.name());
                            }

                            break;

                        case CHECK_WIN:
                            String wonItemID = message.getData1();
                            String wonAgentId = message.getData2();
                            int wonBidAmount = Integer
                                    .parseInt(message.getData3());
                            String itemsAgent = house.getItem(wonItemID)
                                    .getCurrentAgentHighBidId();
                            int itemsBid = house.getItem(wonItemID)
                                    .getBidAmount();

                            boolean correctAgent = itemsAgent
                                    .equals(wonAgentId);
                            boolean correctBid = itemsBid == wonBidAmount;

                            if (correctAgent && correctBid) {
                                house.getItem(wonItemID).setWon(true);
                                Socket wonBankSocket = new Socket(
                                        HouseServer.getBankHost(),
                                        HouseServer.getBankPort());
                                PrintWriter wonBankOut = new PrintWriter(
                                        wonBankSocket.getOutputStream(), true);
                                BufferedReader wonBankIn = new BufferedReader(
                                        new InputStreamReader(
                                                wonBankSocket.getInputStream()));
                                Message winReleaseFundsMessage = null;

                                outMessage = new Message(Message.Type.RETURN,
                                        BidStatus.WON.name());
                                releaseFundsMessage = new
                                        Message(Message.Type.RELEASE_FUNDS,
                                        wonAgentId, String.valueOf(wonBidAmount));
                                wonBankOut.println(releaseFundsMessage);
                            } else if (correctAgent && !correctBid) {
                                outMessage = new Message(Message.Type.RETURN,
                                        BidStatus.ACCEPTED
                                                .name());
                            } else {
                                outMessage = new Message(Message.Type.RETURN,
                                        BidStatus.OUTBID.name());
                            }

                            break;
                        case ITEM_PAID:
                            if (Boolean.valueOf(message.getData1())) {
                                house.getItem(message.getData2()).setSold(true);
                                house.fillItemList(1);
                                outMessage = new Message(Message.Type.RETURN,
                                        BidStatus.WON.name());
                            } else {
                                house.getItem(message.getData2()).setWon(false);
                                //Reject for insufficient funds
                                outMessage = new Message(Message.Type.RETURN,
                                        BidStatus.REJECTED.name());
                            }
                            break;

                        case CLOSE:
                            house.setUpdateList(false);
                            for (Map.Entry<String, Item> entry :
                                    house.getItems().entrySet()) {
                                entry.getValue().setWon(true);
                            }
                            break;

                        default:
                            Db.out("HouseProtocol message not recognized: "
                                    + message);

                    }
                    if (outMessage != null) {
                        Db.out("HouseProtocol::run: " + outMessage);
                        out.println(outMessage);
                    }
                }
            } catch (IOException e) {
                try {
                    clientSocket.close();
                } catch (IOException socketEx) {
                    socketEx.printStackTrace();
                }
            }
        }
    }

    /**
     * Closes the socket
     * @throws IOException
     */
    @Override
    public void close() throws IOException {
        clientSocket.close();
    }
}
