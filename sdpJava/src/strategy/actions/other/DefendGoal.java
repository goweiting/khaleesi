package strategy.actions.other;

import strategy.Strategy;
import strategy.actions.ActionBase;
import strategy.points.basicPoints.DangerousPoint;
import strategy.points.basicPoints.MidDangerPoint;
import strategy.robots.Khaleesi;

/**
 * Created by Simon Rovder
 */
public class DefendGoal extends ActionBase {

    public DefendGoal() {
        this.rawDescription = "Defend Goal";
    }

    @Override
    public void onStart() {
        Khaleesi us = (Khaleesi)Strategy.currentRobotBase;
        us.MOTION_CONTROLLER.setHeading(new DangerousPoint());
        us.MOTION_CONTROLLER.setDestination(new MidDangerPoint(us.robotType));
        us.MOTION_CONTROLLER.setTolerance(-1);
    }

    @Override
    public void update() {
    }
}
