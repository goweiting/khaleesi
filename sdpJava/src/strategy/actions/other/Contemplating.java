package strategy.actions.other;

import strategy.Strategy;
import strategy.actions.ActionBase;

/**
 * Created by Simon Rovder
 * <p>
 * When the robot fails, it shall Contemplate...
 */
public class Contemplating extends ActionBase {
    public Contemplating() {
        this.rawDescription = "Contemplating...";
    }

    @Override
    public void onStart() {
        // Literally stop doing anything. Just stop. STAAAHP.
        Strategy.currentRobotBase.setControllersActive(false);
        Strategy.currentRobotBase.port.stop();
    }

    @Override
    public void update() { }
}
