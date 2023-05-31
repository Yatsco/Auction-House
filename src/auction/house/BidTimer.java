/**
 * 5/10/20
 * Jarek,Jack, Alana
 * CS 351
 **/
package auction.house;

import auction.agent.AgentController;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class BidTimer {
    Timer timer;
    String itemId;
    String houseId;
    int bidAmount;
    AgentController agentController;

    public BidTimer(int seconds, String itemId, String houseId, int bidAmount,
                    AgentController agentController) {
        timer = new Timer();
        this.itemId = itemId;
        this.houseId = houseId;
        this.bidAmount = bidAmount;
        this.agentController = agentController;
        timer.schedule(new BidTimerTask(), seconds * 1000);
    }

    public void cancelTimer() {
        timer.cancel();
    }

    /**
     * inner class that runs and stops the timer
     */
    class BidTimerTask extends TimerTask {

        @Override
        public void run() {
            try {
                agentController.checkIfWon(itemId, houseId, bidAmount);
            } catch (IOException e) {
                timer.cancel();
            }
        }
    }
}
