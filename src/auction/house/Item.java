/**
 * 5/10/20
 * Jarek,Jack, Alana
 * CS 351
 **/
package auction.house;

import java.util.ArrayList;
import java.util.Collections;

public class Item {
    private final String id;
    private final String houseID;
    private final String name;


    private final ArrayList<String> nounList = new ArrayList<>();
    private final ArrayList<String> adjectiveList = new ArrayList<>();
    private int bidAmount;
    private int minimumBid;
    private boolean firstBid;
    private boolean sold;

    private boolean won;
    private String currentAgentHighBidId;

    /**
     * @param bidAmount starting bid value
     * @param id        the Item ID
     * @param houseID   The House ID its in
     */
    public Item(int bidAmount, String id, String houseID) {
        this.bidAmount = bidAmount;
        minimumBid = bidAmount;
        this.id = id;
        this.houseID = houseID;
        sold = false;
        firstBid = true;
        currentAgentHighBidId = "none";


        fillAdjectiveList("Adventurous");
        fillAdjectiveList("Bad");
        fillAdjectiveList("Crazy");
        fillAdjectiveList("Dull");
        fillAdjectiveList("Evil");
        fillAdjectiveList("Itchy");
        fillAdjectiveList("Bad");
        fillAdjectiveList("Light");
        fillAdjectiveList("Smoggy");
        fillAdjectiveList("Zany");

        fillNounList("Part");
        fillNounList("Thing");
        fillNounList("Home");
        fillNounList("Office");
        fillNounList("Air");
        fillNounList("Education");
        fillNounList("Car");
        fillNounList("Slide");
        fillNounList("Phone");
        fillNounList("Auction House");
        Collections.shuffle(nounList);
        Collections.shuffle(adjectiveList);

        name = adjectiveList.get(0) + " " + nounList.get(0);

    }

    /**
     * Getters
     */
    public String getId() {
        return id;
    }

    public int getBidAmount() {
        return bidAmount;
    }

    public void setBidAmount(int bidAmount) {
        this.bidAmount = bidAmount;
        minimumBid = bidAmount + 1;
    }

    /**
     * adds a word to the adj list
     *
     * @param word the word
     */
    public void fillAdjectiveList(String word) {
        adjectiveList.add(word);
    }

    /**
     * adds a word to the noun list
     *
     * @param word the word
     */
    public void fillNounList(String word) {
        nounList.add(word);
    }

    /**
     * ToString
     */
    @Override
    public String toString() {
        return name + " " + "is currently at $" +
                bidAmount + " from " + houseID;
    }

    /**
     * if the item is sold
     *
     * @return true if sold
     */
    public boolean isSold() {
        return sold;
    }

    /**
     * sets if the item is sold
     */
    public void setSold(boolean sold) {
        this.sold = sold;
    }

    public boolean isFirstBid() {
        return firstBid;
    }

    public void setFirstBid(boolean firstBid) {
        this.firstBid = firstBid;
    }

    public String getCurrentAgentHighBidId() {
        return currentAgentHighBidId;
    }

    public void setCurrentAgentHighBidId(String currentAgentHighBidId) {
        this.currentAgentHighBidId = currentAgentHighBidId;
    }

    public boolean isWon() {
        return won;
    }

    public void setWon(boolean won) {
        this.won = won;
    }

    public int getMinimumBid() {
        return minimumBid;
    }

    public String makeDescription() {
        String connector = "";
        if (sold || won) {
            connector = " is NOT available.";
        } else {
            connector = " is AVAILABLE.";
        }
        return name + connector;
    }
}

