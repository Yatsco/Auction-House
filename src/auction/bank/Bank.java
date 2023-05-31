/**
 * 5/10/20
 * Jarek,Jack, Alana
 * CS 351
 **/

package auction.bank;


import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Bank {
    private final static int INITIAL_BALANCE = 0;
    private static int currentAgentIdNum = 1;
    private static int currentHouseIdNum = 1;
    private final String id;
    private final Map<String, String> houseHosts = new HashMap<>();
    private final Map<String, String> agentHosts = new HashMap<>();
    //-----------------------------------------------------------------
    private final HashMap<String, AgentAccount> agentAccounts = new HashMap<String, AgentAccount>();
    private final HashMap<String, HouseAccount> houseAccounts = new HashMap<String, HouseAccount>();

    public Bank() {
        id = "bank";
    }

    /**
     * getters
     */
    public String getNextHouseId() {
        return String.format("h%02d",
                currentHouseIdNum++);
    }

    public String getNextAgentId() {
        return String.format("a%02d",
                currentAgentIdNum++);
    }

    public Set<String> getAgentIds() {
        return agentHosts.keySet();
    }

    public Set<String> getHouseIds() {
        return houseHosts.keySet();
    }

    public String getAgentHost(String agentId) {
        return agentHosts.get(agentId);
    }

    public String getHouseHost(String houseId) {
        return houseHosts.get(houseId);
    }

    public String getHost(String id) {
        String host = houseHosts.get(id);
        if (host != null) {
            return host;
        } else {
            host = agentHosts.get(id);
            return host;
        }
    }

    /**
     * registers a house to a host
     *
     * @param id
     * @param host
     */
    public void registerHouse(String id, String host) {

        houseHosts.put(id, host);
        HouseAccount account = new HouseAccount(id);
        houseAccounts.put(id, account);
    }

    /**
     * registers an agent to a host
     *
     * @param id
     * @param host
     * @param agentName
     * @param initialBalance
     */
    public void registerAgent(String id, String host, String agentName,
                              int initialBalance) {
        agentHosts.put(id, host);
        AgentAccount account = new AgentAccount(id, initialBalance, agentName);
        agentAccounts.put(id, account);
    }

    /**
     * gets agents balance
     */
    public int getAgentAccountBalance(String id) {
        AgentAccount account = agentAccounts.get(id);
        return account.getAccountBalance();
    }

    /**
     * gets agents available balance
     *
     * @param id
     * @return
     */
    public int getAgentAvailableBalance(String id) {
        return agentAccounts.get(id).getAvailableFunds();
    }

    /**
     * gets house's balance
     *
     * @param id of house
     * @return balance
     */
    public int getHouseAccountBalance(String id) {
        HouseAccount account = houseAccounts.get(id);
        return account.getAccountBalance();
    }

    /**
     * holds the agents funds
     */
    public boolean holdAgentFunds(String id, int holdAmount) {
        return agentAccounts.get(id).holdFunds(holdAmount);
    }

    /**
     * Getters
     */
//    public HashMap getAgentAccounts() {
//        return agentAccounts;
//    }
//
//    public HashMap getHouseAccounts() {
//        return houseAccounts;
//    }

    /**
     * transfers the funds of an agent to the house
     *
     * @param agentId id of the agent
     * @param houseId of the house
     * @param amount  to transfer
     */
    public synchronized boolean transferAgentFundsToHouse(String agentId,
                                                          String houseId,
                                                          int amount) {
        AgentAccount agentAccount = agentAccounts.get(agentId);
        boolean fundsAvail = agentAccount.payAuctionHouse(amount);

        if (fundsAvail) {
            HouseAccount houseAccount = houseAccounts.get(houseId);
            houseAccount.addFunds(amount);
        }

        return fundsAvail;
    }

    ///////////////////////////////////////////////////////////////////////////

    /**
     * getter
     */
    public String getId() {
        return id;
    }

    /**
     * holds the funds of the bid
     *
     * @param agentId   the agent to hold
     * @param bidAmount the funds to hold
     */
    public boolean holdBidFunds(String agentId, int bidAmount) {
        AgentAccount agentAccount = null;
        try {
            agentAccount = agentAccounts.get(agentId);
        } catch (Exception e) {
            System.out.println("no such agent");
            return false;
        }
        return agentAccount.holdFunds(bidAmount);
    }

    /**
     * Releases the funds of the bid
     *
     * @param agentId   the agent to release
     * @param bidAmount the funds to release
     * @return
     */
    public boolean releaseBidFunds(String agentId, int bidAmount) {
        AgentAccount agentAccount = null;
        try {
            agentAccount = agentAccounts.get(agentId);
        } catch (Exception e) {
            System.out.println("no such agent");
            return false;
        }
        return agentAccount.releaseFunds(bidAmount);
    }

    public void initBank() {
    }
}
