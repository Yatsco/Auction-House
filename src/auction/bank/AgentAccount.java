/**
 * 5/10/20
 * Jarek,Jack, Alana
 * CS 351
 **/

package auction.bank;

public class AgentAccount {
    private final String id;
    private final String name;
    private int accountBalance;
    private int availableFunds;

    //TODO: disallow neg account balance at open

    /**
     * constructor for an Agent Account
     *
     * @param id             Id of agent
     * @param initialBalance starting funds
     * @param name           the name of the agent
     */
    public AgentAccount(String id, int initialBalance, String name) {
        this.id = id;
        this.name = name;
        this.accountBalance = initialBalance;
        this.availableFunds = initialBalance;
    }

    /**
     * adds funds to an agent
     *
     * @param amount funds to change price
     * @return the updated funds
     */
    public void addFunds(int amount) {
        accountBalance += amount;
        availableFunds += amount;
    }


    /**
     * Holds the funds of an agent
     *
     * @param bidAmount the amount to hold
     */
    public boolean holdFunds(int bidAmount) {
        if (bidAmount <= availableFunds && bidAmount > 0) {
            availableFunds -= bidAmount;
            return true;
        } else if (bidAmount < 0) {
            //TODO add a way to pass a string that says to pass a positive num
            return false;
        } else {
            //TODO add a way to pass a string notifying that available funds are
            // too low
            return false;
        }
    }

    /**
     * releases funds of an agent
     *
     * @param bidAmount the amount to release
     */
    public boolean releaseFunds(int bidAmount) {
        if (bidAmount > 0) {
            availableFunds += bidAmount;
            System.out.println("released $" + bidAmount + id);
            return true;
        } else {
            //TODO add way to pass a string indicating to pass + bidamount
            System.out.println("not released $" + bidAmount + id);
            return false;
        }
    }

    /**
     * Pays the house
     *
     * @param bidAmount the amount to pay
     */
    public boolean payAuctionHouse(int bidAmount) {
        if (bidAmount > 0 && (accountBalance - bidAmount >= 0)) {
            accountBalance -= bidAmount;
            availableFunds -= bidAmount;
            return true;
        } else {
            return false;
        }
    }

    /**
     * getters
     */
    public int getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(int accountBalance) {
        this.accountBalance = accountBalance;
    }

    public int getAvailableFunds() {
        return availableFunds;
    }
}

