package strategy.actions.other;

import strategy.Strategy;
import strategy.actions.ActionBase;
import strategy.points.basicPoints.BallPoint;
import strategy.points.basicPoints.ConstantPoint;
import strategy.robots.Khaleesi;
import vision.Robot;
import vision.constants.Constants;
import vision.tools.VectorGeometry;

/**
 * Created by Rado Kirilchev
 * The idea here is that we patrol the general area of our goal.
 */
public class PatrolGoal extends ActionBase {

    public PatrolGoal() {
        this.rawDescription = "Patrol Goal";
    }

    private static final int ourGoalX = -Constants.PITCH_WIDTH / 2 + 20; // A little bit in front of the goal
    private static final ConstantPoint upperLimit = new ConstantPoint(ourGoalX, Constants.PITCH_HEIGHT / 2);
    private static final ConstantPoint lowerLimit = new ConstantPoint(ourGoalX, -Constants.PITCH_HEIGHT / 2);
    private ConstantPoint curDestination;

    @Override
    public void onStart() {
        Khaleesi us = (Khaleesi)Strategy.currentRobotBase;
        us.MOTION_CONTROLLER.setActive(true);
        // Keep looking towards the ball
        us.MOTION_CONTROLLER.setHeading(new BallPoint());
        // Move towards one of the points
        curDestination = upperLimit;
        us.MOTION_CONTROLLER.setDestination(curDestination);
        us.MOTION_CONTROLLER.setTolerance(-1);
    }

    @Override
    public void update() {
        // Check whether we're approaching our destination
        Robot us = Strategy.curVisionRobot;
        double dist = VectorGeometry.distance(us.location, new VectorGeometry(curDestination.getX(), curDestination.getY()));
        if (dist < 10) {
            // Change destination.
            curDestination = (curDestination == upperLimit) ? lowerLimit : upperLimit;
        }
    }
}
