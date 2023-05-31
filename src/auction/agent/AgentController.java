/**
 * 5/10/20
 * Jarek,Jack, Alana
 * CS 351
 * Controls the agent GUI
 **/

package auction.agent;

import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import auction.Db;
import auction.Message;
import auction.house.Bid;
import auction.house.Bid.BidStatus;
import auction.house.BidTimer;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import static auction.Message.Type.*;

public class AgentController implements Closeable {

    @FXML
    private final int BANK_PORT = 8000;
    @FXML
    private final int HOUSE_PORT = 8001;
    @FXML
    private final HashMap<String, BidStatus> BID_STATUSES = new HashMap<>();
    @FXML
    private TextField bankHost;
    @FXML
    private Label bankConnectStatus;
    @FXML
    private TextField housesList;
    @FXML
    private TextField houseId;
    @FXML
    private TextField houseHost;
    @FXML
    private Label houseConnectStatus;
    @FXML
    private TextField houseFunds;
    @FXML
    private TextField itemsList;
    @FXML
    private TextField itemId;
    @FXML
    private TextField itemName;
    @FXML
    private TextField itemPrice;
    @FXML
    private TextField bidAmount;
    @FXML
    private TextField bidReply;
    @FXML
    private Socket bankSocket;
    @FXML
    private PrintWriter bankOut;
    @FXML
    private BufferedReader bankIn;
    @FXML
    private TextField agentId;
    @FXML
    private TextField agentName;
    @FXML
    private TextField agentAccountBal;
    @FXML
    private TextField agentAvailFunds;
    private Socket houseSocket;
    private PrintWriter houseOut;
    private BufferedReader houseIn;
    @FXML
    private TextField minBid;
    @FXML
    private TextField highBidAgent;
    @FXML
    private TextField wonItemId;
    @FXML
    private TextField wonItemAmount;
    private int bidCheck;
    private ArrayList<String> closedHouses = new ArrayList<>();

    /**
     * resets all values to default
     */
    private void resetFields() {
        houseFunds.clear();
        itemsList.clear();
        itemName.clear();
        itemPrice.clear();
        minBid.clear();
        highBidAgent.clear();
        bidAmount.clear();
        bidReply.clear();
    }

    @FXML
    /**
     * sets our values to given starting values
     */
    public void populateInitial() throws IOException {
        Agent agent = AgentServer.getAgent();

        agentId.setText(agent.getId());
        agentName.setText(agent.getAgentName());
        queryAgentAccountBalance(null);

    }

    /**
     * Handles what happens when viewing an item
     * and sets all text fields according
     */
    public synchronized void queryItemInfo(ActionEvent actionEvent)
            throws IOException {
        if (houseSocket != null && houseOut != null && houseIn != null
        && (closedHouses.isEmpty() || !closedHouses.contains(houseId.getText()))) {
            String id = itemId.getText();
            Message message = new Message(QUERY_ITEM, id);
            Db.out("getItemInfo: " + message);
            houseOut.println(message);

            String houseLine = null;
            try {
                houseLine = houseIn.readLine();
            } catch (IOException e) {
                return;
            }

            Message houseMsg = Message.fromString(houseLine);
                String itemNameData = houseMsg.getData1();
                String itemBidData = "";

                if (itemNameData.equals("none")) {
                    itemName.setText("No such item exists");
                    itemPrice.clear();
                    bidAmount.clear();
                    minBid.clear();
                    highBidAgent.clear();
                    bidReply.clear();

                } else {
                    itemBidData = houseMsg.getData2();
                    String itemBidStatusId = houseMsg.getData3();
                    String itemMinBid = houseMsg.getData4();

                    if (actionEvent != null) {
                        itemName.setText(itemNameData);
                        itemPrice.setText(itemBidData);
                        bidAmount.setText(itemMinBid);
                        minBid.setText(itemMinBid);
                        highBidAgent.setText(itemBidStatusId);
                        bidReply.clear();
                    }
                }

                if (actionEvent == null) {
                    bidCheck = Integer.parseInt(itemBidData);
                }
            }
                updateBidStatuses();

    }

    /**
     * updates the status of the bid
     *
     * @throws IOException
     */
    public synchronized void updateBidStatuses() throws IOException {
        if (!BID_STATUSES.isEmpty()) {
            for (Map.Entry<String, BidStatus> entry : BID_STATUSES.entrySet()) {
                if (entry.getValue() == BidStatus.ACCEPTED) {
                    String bidId = entry.getKey();
                    String houseId = "";
                    String itemId = "";
                    int index = bidId.indexOf('i');

                    if (index != -1) {
                        houseId = bidId.substring(0, index);
                        itemId = bidId.substring(index);
                    } else {
                        break;
                    }

                    Socket checkHouseSocket = null;
                    PrintWriter checkHouseOut = null;
                    BufferedReader checkHouseIn;
                    String houseHost = queryHouseHost(houseId);
                    int housePort = 8001;

                    try {
                        checkHouseSocket = new Socket(houseHost,
                          housePort);
                        checkHouseOut = new PrintWriter(
                                checkHouseSocket.getOutputStream(), true);
                        checkHouseIn = new BufferedReader(new InputStreamReader(
                                checkHouseSocket.getInputStream()));
                        Message message = new Message(QUERY_ITEM, itemId);
                        checkHouseOut.println(message);
                        String houseResponse = checkHouseIn.readLine();

                        Message houseMsg = Message.fromString(houseResponse);
                        String itemBidStatusId = houseMsg.getData3();

                        if ((!BID_STATUSES.isEmpty()
                                && BID_STATUSES.get(bidId) != null) &&
                                BID_STATUSES.get(bidId) == BidStatus.ACCEPTED &&
                                !itemBidStatusId
                                        .equalsIgnoreCase(agentId.getText())) {
                            BID_STATUSES.put(bidId, BidStatus.OUTBID);

                            queryAgentAccountBalance(null);
                        }

                    } catch (IOException e) {
                        try {
                            assert checkHouseSocket != null;
                            checkHouseSocket.close();
                            closedHouses.add(houseId);
                            System.out.println(houseId + " closed");
                        } catch (NullPointerException nullPtr) {

                        }
                    }
                }
            }
        }
    }

    /**
     * Handles what the house items values should be set to and displays them
     */
    public void queryHouseItemsList(ActionEvent actionEvent)
            throws IOException {
        if (houseSocket != null && houseOut != null && houseIn != null) {
            Message message = new Message(QUERY_ITEMS);
            Db.out("getHouseItemsList: " + message);
            houseOut.println(message);
            String itemsLine = houseIn.readLine();
            Message itemsMsg = Message.fromString(itemsLine);
            itemsList.setText(itemsMsg.getData1());
        }
    }

    /**
     * sets the list of houses to what they should be and displays them
     */
    public void queryHousesList(ActionEvent actionEvent) throws IOException {
        if (bankSocket != null && bankOut != null && bankIn != null) {
            Message message = new Message(QUERY_HOUSES);
            String messageStr = message.toString();
            bankOut.println(messageStr);
            String housesLine = bankIn.readLine();
            Message housesMsg = Message.fromString(housesLine);
            housesList.setText(housesMsg.getData1());
        }
    }

    /**
     * sets the house host to whats given and displays it
     *
     * @throws IOException
     */
    public void queryHouseHost(ActionEvent actionEvent) throws IOException {
        houseConnectStatus.setVisible(false);
        if (bankSocket != null && bankOut != null && bankIn != null) {
            String id = houseId.getText();
            Message message = new Message(QUERY_HOST, id);
            String messageStr = message.toString();
            bankOut.println(messageStr);
            String hostLine = bankIn.readLine();
            Message hostMessage = Message.fromString(hostLine);
            houseHost.setText(hostMessage.getData1());
        }
    }

    /**
     * sets the house host to whats given and displays it
     *
     * @throws IOException
     */
    public String queryHouseHost(String houseID) throws IOException {
        if (bankSocket != null && bankOut != null && bankIn != null) {
            Message message = new Message(QUERY_HOST, houseID);
            String messageStr = message.toString();
            bankOut.println(messageStr);
            String hostLine = bankIn.readLine();
            Message hostMessage = Message.fromString(hostLine);
            return hostMessage.getData1();
        }
        return null;
    }

    /**
     * connects to the house and displays green if successful red if not
     *
     * @param actionEvent button
     */
    public void handleHouseConnect(ActionEvent actionEvent) {
        try {
            if (!houseHost.getText().equals("null")) {
                houseSocket = new Socket(houseHost.getText(), HOUSE_PORT);
                houseOut = new PrintWriter(houseSocket.getOutputStream(),
                        true);
                houseIn = new BufferedReader(
                        new InputStreamReader(houseSocket.getInputStream()));
                houseConnectStatus.setText("Connection successful!");
                houseConnectStatus.setStyle("-fx-text-fill: green;");
                houseConnectStatus.setVisible(true);
                resetFields();
            }
        } catch (IOException e) {
            houseConnectStatus.setText("Connection to server failed!");
            houseConnectStatus.setStyle("-fx-text-fill: red;");
            houseConnectStatus.setVisible(true);
        }
    }

    /**
     * connects to the bank and displays green if successful red if not
     *
     * @param actionEvent button
     */
    public void handleBankConnect(ActionEvent actionEvent) {
        try {
            bankSocket = new Socket(bankHost.getText(), BANK_PORT);
            bankOut = new PrintWriter(bankSocket.getOutputStream(),
                    true);
            bankIn = new BufferedReader(
                    new InputStreamReader(bankSocket.getInputStream()));
            bankConnectStatus.setText("Connection successful!");
            bankConnectStatus.setStyle("-fx-text-fill: green;");
        } catch (IOException e) {
            bankConnectStatus.setText("Connection to server failed!");
            bankConnectStatus.setStyle("-fx-text-fill: red;");
        }
    }

    /**
     * gets the agents balance and displays it
     *
     * @param actionEvent
     * @throws IOException
     */
    public synchronized void queryAgentAccountBalance(ActionEvent actionEvent)
            throws IOException {
        if (bankSocket != null && bankOut != null && bankIn != null) {
            String id = agentId.getText();
            Message message = new Message(QUERY_AGENT_ACCOUNT_BALANCE, id);
            Db.out("queryAgentAccountBalance: " + message);
            bankOut.println(message);
            String bankLine = bankIn.readLine();
            Message bankMessage = Message.fromString(bankLine);
            agentAccountBal.setText(bankMessage.getData1());
            agentAvailFunds.setText(bankMessage.getData2());
        }
    }

    /**
     * gets the house's balance and displays it
     *
     * @param actionEvent
     * @throws IOException
     */
    public synchronized void queryHouseAccountBalance(ActionEvent actionEvent)
            throws IOException {
        if (bankSocket != null && bankOut != null && bankIn != null) {
            String id = houseId.getText();
            Message message = new Message(QUERY_HOUSE_ACCOUNT_BALANCE, id);
            Db.out("queryHouseAccountBalance: " + message);
            bankOut.println(message);
            String bankLine = bankIn.readLine();
            Message bankMessage = Message.fromString(bankLine);
            houseFunds.setText(bankMessage.getData1());
        }
    }

    /**
     * transfers funds from the agent to the house
     *
     * @param agentId given agent
     * @param houseId given house
     * @param amount  given amount
     * @throws IOException
     */
    public synchronized boolean transferFunds(String agentId, String houseId,
                                              int amount)
            throws IOException {
        if (bankSocket != null && bankOut != null && bankIn != null) {
            Message message = new Message(TRANSFER_FUNDS, agentId, houseId,
                    String.valueOf(amount));
            Db.out("transferFunds: " + message);
            bankOut.println(message);
            String bankLine = bankIn.readLine();
            Message bankMessage = Message.fromString(bankLine);
            Db.out("transferFunds2: " + message);
            // TODO update fields automatically
            queryAgentAccountBalance(null);
            queryHouseAccountBalance(null);
            return Boolean.parseBoolean(bankMessage.getData3());
        }
        return false;
    }

    /**
     * makes a bid on an item when button is pressed
     *
     * @throws IOException
     */
    public synchronized void makeBid(ActionEvent actionEvent)
            throws IOException {

        if (houseSocket != null && houseOut != null && houseIn != null) {
            String houseIdStr = houseId.getText();
            String agentIdStr = agentId.getText();
            String itemIdStr = itemId.getText();
            String bidStr = bidAmount.getText();
            int bidAmount = Integer.parseInt(bidStr);
            String bidId = houseIdStr + itemIdStr;

            if ((!BID_STATUSES.isEmpty() && BID_STATUSES.get(bidId) != null)) {
                updateBidStatuses();
                if (BID_STATUSES.get(bidId) == BidStatus.ACCEPTED &&
                        bidCheck == bidAmount) {
                    bidReply.setText(
                            "Your bid of " + bidStr + " is the current high bid");
                    return;
                }
            }

            Message message = new Message(MAKE_BID, houseIdStr, itemIdStr,
                    agentIdStr, bidStr);
            Db.out("makeBid: " + message);
            houseOut.println(message);
            String bidLine = houseIn.readLine();
            Message bidMsg = Message.fromString(bidLine);
            String bidStatus = bidMsg.getData1();
            Bid.BidStatus status = Bid.getBidStatus(bidStatus);

            switch (status) {

                case ACCEPTED:
                    queryAgentAccountBalance(null);
                    BidTimer winTimer = new BidTimer(30, itemIdStr,
                            houseIdStr,
                            bidAmount, this);
                    BID_STATUSES.put(bidId, status);

                    if (BID_STATUSES.size() == 1) {
                        UpdateBidsTimer checkBids =
                                new UpdateBidsTimer(1, this);
                    }
                    break;

                case REJECTED:
                case OUTBID:
                    BID_STATUSES.put(bidId, status);
                    //TODO add a way to notify gui of outbid status

                default:
                    //DO NOTHING
            }

            bidReply.setText(bidStatus);
        }
    }

    /**
     * checks if the item bid on is won
     *
     * @param itemId    to check
     * @param houseId   the item is in
     * @param bidAmount of the given bid
     * @throws IOException
     */
    public synchronized void checkIfWon(String itemId, String houseId,
                                        int bidAmount) throws IOException {
        String bidId = houseId + itemId;
        updateBidStatuses();

        if ((!BID_STATUSES.isEmpty() && BID_STATUSES.get(bidId) != null)
                && BID_STATUSES.get(bidId) == BidStatus.ACCEPTED) {

            Socket winHouseSocket = null;
            PrintWriter winHouseOut;
            BufferedReader winHouseIn;

            String houseHost = queryHouseHost(houseId);
            int housePort = 8001;

            try  {
                winHouseSocket = new Socket(houseHost, housePort);
                winHouseOut = new PrintWriter(winHouseSocket.getOutputStream(),
                        true);
                winHouseIn = new BufferedReader(
                        new InputStreamReader(winHouseSocket.getInputStream()));
                Message message = new Message(QUERY_ITEM, itemId);
                winHouseOut.println(message);
                String houseResponse = winHouseIn.readLine();

                Message houseMsg = Message.fromString(houseResponse);
                String itemBidData = houseMsg.getData2();
                String itemBidStatusId = houseMsg.getData3();

                if (BID_STATUSES.get(bidId) == BidStatus.ACCEPTED &&
                        !itemBidStatusId.equalsIgnoreCase(agentId.getText())) {
                    BID_STATUSES.put(bidId, BidStatus.OUTBID);
                    return;
                }

                if (BID_STATUSES.get(bidId) == BidStatus.ACCEPTED) {
                    String agentIdStr = agentId.getText();
                    String bidStr = String.valueOf(bidAmount);

                    message = new Message(CHECK_WIN, itemId, agentIdStr,
                            bidStr);

                    winHouseOut.println(message);
                    houseResponse = winHouseIn.readLine();
                    houseMsg = Message.fromString(houseResponse);

                    String bidStatus = houseMsg.getData1();
                    Bid.BidStatus status = Bid.getBidStatus(bidStatus);

                    switch (status) {

                        case WON:
                            boolean paid =
                                    transferFunds(agentIdStr, houseId, bidAmount);
                            message = new Message(ITEM_PAID,
                                    String.valueOf(paid),
                                    itemId);

                            winHouseOut.println(message);
                            houseResponse = winHouseIn.readLine();
                            houseMsg = Message.fromString(houseResponse);

                            bidStatus = houseMsg.getData1();
                            status = Bid.getBidStatus(bidStatus);

                            BID_STATUSES.put(bidId, status);

                            if (status == BidStatus.WON) {
                                wonItemId.setText(bidId);
                                wonItemAmount.setText(bidStr);
                            }

                            break;

                        case ACCEPTED:
                            //DO NOTHING
                            break;

                        case OUTBID:
                            BID_STATUSES.put(bidId, status);
                            //TODO add a way to notify gui of outbid status

                        default:
                            //DO NOTHING
                    }

                }

            } catch (IOException e) {
                if (winHouseSocket != null) {
                    winHouseSocket.close();
                }
            }
        }
    }

    @FXML
    /**
     * disconnects given agent
     */
    private void disconnectAgent(ActionEvent actionEvent) throws IOException {
        if (!BID_STATUSES.isEmpty()) {
            try {
                Thread.sleep(35000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        updateBidStatuses();
        // Just in case this breaks in the future. For example, if bidding time
        // is extended to 45 seconds, the above sleep time would need to be
        // increased
        for (Map.Entry<String, BidStatus> entry : BID_STATUSES.entrySet()) {
            if (entry.getValue() == BidStatus.ACCEPTED) {
                System.out.println(entry.getKey());
                System.out.println("update disconnectAgent");
            }
        }

        System.exit(0);
    }

    @FXML
    /**
     * disconnects given house
     */
    private void disconnectHouse() throws IOException {
        if (houseSocket != null && houseOut != null && houseIn != null) {
            Message message = new Message(Message.Type.CLOSE);
            houseOut.println(message);
            itemsList.setText("House " + houseId.getText() + " closing."
                    + "Wait 35 secs for bidding to finish.");
        }
    }

    @Override
    /**
     * closes the sockets
     */
    public void close() throws IOException {
        bankSocket.close();
        houseSocket.close();
    }
}
