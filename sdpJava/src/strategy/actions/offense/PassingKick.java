package strategy.actions.offense;

import strategy.Strategy;
import strategy.actions.ActionBase;
import strategy.points.basicPoints.BallPoint;
import strategy.robots.Khaleesi;

/**
 * Created by Rado Kirilchev on 21/02/2017.
 */
// Pass ball to friend. FIXME: ACTUALLY MAKE IT DO THINGS.
public class PassingKick extends ActionBase {
    public PassingKick() {
        this.rawDescription = "Passing Kick";
    }

    @Override
    public void update() {
        Khaleesi us = (Khaleesi) Strategy.currentRobotBase;

        us.MOTION_CONTROLLER.setActive(true);
        us.MOTION_CONTROLLER.setHeading(new BallPoint());
        us.MOTION_CONTROLLER.setDestination(new BallPoint());
        // Enable dribbler
        us.DRIBBLER_CONTROLLER.setActive(true);
        // Check distance to ball, try kicking if we're near it
        // (I have no idea if this code is actually correct)
        if (Strategy.curVisionRobot.location.distance(Strategy.world.getBall().location) < 10) {
            us.KICKER_CONTROLLER.setActive(true);
        }
    }


    @Override
    public void onEnd() {
        // Disable kicker and dribbler.
        ((Khaleesi)Strategy.currentRobotBase).KICKER_CONTROLLER.setActive(false);
        ((Khaleesi)Strategy.currentRobotBase).DRIBBLER_CONTROLLER.setActive(false);
    }
}
