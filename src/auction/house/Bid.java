/**
 * 5/10/20
 * Jarek,Jack, Alana
 * CS 351
 **/
package auction.house;

//import agent.Agent;

public class Bid {
    private final static int BID_DURATION = 3000;
    private final String id;
    private final String houseId;
    private final String itemId;
    private final String agentId;
    private final int bidAmount;
    private BidStatus bidStatus;
    private int submitTime;
    private int endTime;
    public Bid(String id, String houseId, String itemId, String agentId,
               int bidAmount) {

        this.id = id;
        this.houseId = houseId;
        this.itemId = itemId;
        this.agentId = itemId;
        this.bidAmount = bidAmount;
        bidStatus = BidStatus.AWAITING_RESPONSE;
    }

    public static BidStatus getBidStatus(String statusStr) {
        BidStatus status;
        if (statusStr.equalsIgnoreCase(BidStatus.ACCEPTED.name())) {
            status = BidStatus.ACCEPTED;
        } else if (statusStr.equalsIgnoreCase(BidStatus.REJECTED.name())) {
            status = BidStatus.REJECTED;
        } else if (statusStr.equalsIgnoreCase(BidStatus.OUTBID.name())) {
            status = BidStatus.OUTBID;
        } else if (statusStr.equalsIgnoreCase(BidStatus.WON.name())) {
            status = BidStatus.WON;
        } else if (statusStr.equalsIgnoreCase(BidStatus.LOST.name())) {
            status = BidStatus.LOST;
        } else if (statusStr.equalsIgnoreCase(
                BidStatus.AWAITING_RESPONSE.name())) {
            status = BidStatus.AWAITING_RESPONSE;
        } else {
            status = BidStatus.UNKNOWN;
        }
        return status;
    }

    /**
     * Getters and Setters
     */
    public String getId() {
        return id;
    }

    public String getHouseId() {
        return houseId;
    }

    public String getAgentId() {
        return agentId;
    }

    public String getItemId() {
        return itemId;
    }

    public int getBidAmount() {
        return bidAmount;
    }

    public BidStatus getStatus() {
        return bidStatus;
    }

    public void setStatus(BidStatus bidStatus) {
        this.bidStatus = bidStatus;
    }

    public int getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(int submitTime) {
        this.submitTime = submitTime;
        this.endTime = submitTime + BID_DURATION;
    }

    public enum BidStatus {
        ACCEPTED, // your bid is current highest bid
        REJECTED, // bid is not high enough or insufficient funds
        OUTBID, // outbid
        WON, // Bid is won. Pay the AuctionHouse.
        LOST, // Bid is lost and closed. Update bid status and take no further action
        AWAITING_RESPONSE,
        UNKNOWN
    }


}

