/**
 * 5/10/20
 * Jarek,Jack, Alana
 * CS 351
 **/
package auction.agent;

public class Agent {

    private final String id;
    private final String agentName;
    private final int initialAccountBalance;
    private final int initialAvailableFunds;

    /**
     * constructor
     */
    public Agent(String id, String agentName, int accountBalance,
                 int availableFunds) {
        this.id = id;
        this.agentName = agentName;
        this.initialAccountBalance = accountBalance;
        this.initialAvailableFunds = availableFunds;
    }

    /**
     * gets ID
     */
    public String getId() {
        return id;
    }

    /**
     * getter
     *
     * @return the name of the agent
     */
    public String getAgentName() {
        return agentName;
    }

    /**
     * gets starting balance and returns it
     */
    public int getInitialAccountBalance() {
        return initialAccountBalance;
    }

    /**
     * gets starting funds and returns it
     */
    public int getInitialAvailableFunds() {
        return initialAvailableFunds;
    }

    /**
     * makes a bid on the given item
     * @param item given item
     * @return true if bid was made
     */
//    public boolean makeBid(Item item) {
//        boolean madeBid = false;
//        int currentBid = item.getBidAmount();
//        int availableFunds =  bankAccount.getAvailableFunds();
//        if (availableFunds <= currentBid || !canBid) {
//            return false;
//        }
//        int bidAmount = currentBid + 10;
//        if (bidAmount > availableFunds) {
//            return false;
//        }
//        Bid bid = new Bid(item.getHouseID(), item.getItemId(), agents.get(id), item, bidAmount);
//        bids.put(bid.getBidId(), bid);
//        auctionHouses.get(item.getHouseID()).bidhandler(bid);
//        madeBid = true;

//        return madeBid;
//    }

//    /**
//     * handles the bid and gives output on what happened
//     * @param bidId the bids ID
//     * @param status the status of the bid
//     * @param item item we are bidding on
//     */
//    public synchronized void handleBidResponse(String bidId, BidStatus status, Item item) {
//
//        bids.get(bidId).setStatus(status);
//
//        if (status == BidStatus.REJECTED) {
//            makeBid(item);
//        } else if (status == BidStatus.WON) {
//            if (bankAccount.payAuctionHouse(bids.get(bidId).getBidAmount(),
//                    auctionHouseAccounts.get(bids.get(bidId).getAuctionHouseId()))) {
//                bids.get(bidId).getItem().setPaid(true);
//                purchasedItems.add(item);
//            } else {
//                bids.get(bidId).setStatus(BidStatus.INSUFFICIENT_FUNDS);
//            }
//        }
//    }

}

// ***********************************************************************************
//    private HashMap<String, Bid> bids = new HashMap<String, Bid>();
//    private HashMap<String, House> auctionHouses = new HashMap<String, House>();
//    private HashMap<String, AuctionHouseAccount> auctionHouseAccounts =
//            new HashMap<String, AuctionHouseAccount>();
//    private HashMap<String, Agent> agents = new HashMap<String, Agent>();
//    private boolean canBid;
//    private ArrayList<Item> purchasedItems = new ArrayList<>();
//    //Items available from all auction houses
//    private ArrayList<Item> allItems = new ArrayList<>();
//    //Items agent can afford
//    private ArrayList<Item> biddableItems = new ArrayList<>();
//
/**
 * finds all items that the agent can bid on
 */
//  public synchronized void populateAgentItems() {
//        if (!auctionHouses.isEmpty()) {
//            for (Map.Entry<String, House> entry : auctionHouses.entrySet()) {
//                for (Item item : entry.getValue().getAuctionList()) {
//                    if (!allItems.contains(item)) {
//                        allItems.add(item);
//                    }
//                }
//            }
//        }
//
//        int availFunds = bankAccount.getAvailableFunds();
//
//        for (Item item : allItems) {
//
//            if (item.getBidAmount() <= availFunds && !biddableItems.contains(item)) {
//                biddableItems.add(item);
//            }
//        }
//   }

//    /**
//     * select a valid item to bid on
//     * @return the item to bid on
//     */
//    public synchronized Item selectBidItem() {
//        Random rng = new Random();
//
//        // we should disallow bidding for that agent
//        while (biddableItems.isEmpty()) {
//            bankAccount.addFunds(100);
//            populateAgentItems();
//        }
//
//
//        int itemNum = rng.nextInt(biddableItems.size());
//        String itemId = biddableItems.get(itemNum).getItemId();
//
//        return biddableItems.get(itemNum);
//    }
