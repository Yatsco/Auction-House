/**
 * 5/10/20
 * Jarek,Jack, Alana
 * CS 351
 **/
package auction.bank;

public class HouseAccount {
    private final String id;
    private int accountBalance;

    /**
     * constructor
     */
    public HouseAccount(String id) {
        this.id = id;
        this.accountBalance = 0;
    }

    /**
     * adds funds to the house
     *
     * @param amount to add
     */
    public void addFunds(int amount) {
        // amount can be negative
        accountBalance += amount;
    }

    /**
     * add funds to the houses account
     *
     * @param bidAmount the funds to add
     */
    void payAccount(int bidAmount) {
        accountBalance += bidAmount;
    }


    public String getId() {
        return id;
    }


    public int getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(int accountBalance) {
        this.accountBalance = accountBalance;
    }
}
