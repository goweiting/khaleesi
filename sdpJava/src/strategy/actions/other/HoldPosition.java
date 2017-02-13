package strategy.actions.other;

import strategy.Strategy;
import strategy.actions.ActionBase;
import strategy.points.DynamicPoint;
import strategy.robots.Khaleesi;

/**
 * Created by Simon Rovder
 * <p>
 * When the robot fails, it shall Contemplate...
 */
public class HoldPosition extends ActionBase {
    private DynamicPoint targetPosition;

    public HoldPosition(DynamicPoint target) {
        targetPosition = target;
        this.rawDescription = "Holding position [X " + targetPosition.getX() + ";" +
                              "Y " + targetPosition.getY() + "]";
    }

    @Override
    public void onStart() {
        // Set target position as a destination
        Khaleesi us = (Khaleesi)Strategy.currentRobotBase;
        us.MOTION_CONTROLLER.setActive(true);
        us.MOTION_CONTROLLER.setDestination(targetPosition);
        us.MOTION_CONTROLLER.setHeading(targetPosition);
        us.MOTION_CONTROLLER.setTolerance(-1);
    }

    @Override
    public void update() { }
}
