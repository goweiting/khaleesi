package strategy.actions.other;

import strategy.Strategy;
import strategy.actions.ActionBase;
import strategy.points.basicPoints.AnnoyingDefendPoint;
import strategy.points.basicPoints.DangerousPoint;
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
        us.MOTION_CONTROLLER.setActive(true);
        us.MOTION_CONTROLLER.setHeading(new DangerousPoint());
        // Intercept opponent instead of trying to block the goal.
        //us.MOTION_CONTROLLER.setDestination(new MidDangerPoint(us.robotType));
        us.MOTION_CONTROLLER.setDestination(new AnnoyingDefendPoint());
        us.MOTION_CONTROLLER.setTolerance(-1);
    }

    @Override
    public void update() {
    }
}
