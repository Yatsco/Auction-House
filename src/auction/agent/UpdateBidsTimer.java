/**
 * 5/10/20
 * Jarek,Jack, Alana
 * CS 351
 **/

package auction.agent;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class UpdateBidsTimer {

    Timer timer;
    AgentController agentController;

    /**
     * constructor
     */
    public UpdateBidsTimer(int seconds, AgentController agentController) {
        timer = new Timer();
        this.agentController = agentController;
        timer.schedule(new UpdateBidsTimerTask(), 0, seconds * 1000);
    }

    public void cancelTimer() {
        timer.cancel();
    }

    /**
     * inner class that updates the status of the timer
     */
    class UpdateBidsTimerTask extends TimerTask {

        @Override
        public void run() {

            try {
                agentController.updateBidStatuses();
            } catch (IOException e) {
                timer.cancel();
            }
        }
    }

}
