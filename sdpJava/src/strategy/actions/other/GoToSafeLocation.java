package strategy.actions.other;

import strategy.Strategy;
import strategy.actions.ActionBase;
import strategy.navigation.Obstacle;
import strategy.points.basicPoints.BallPoint;
import strategy.points.basicPoints.ConstantPoint;
import strategy.robots.Khaleesi;
import vision.Ball;
import vision.Robot;
import vision.constants.Constants;
import vision.tools.VectorGeometry;

/**
 * Created by Simon Rovder
 */
public class GoToSafeLocation extends ActionBase {
    public GoToSafeLocation() {
        this.rawDescription = "Go to safe location";
    }

    public static boolean safe() {
        Robot us = Strategy.curVisionRobot;
        Ball ball = Strategy.world.getLastKnownBall();
        if (us == null || ball == null) return false;
        VectorGeometry ourGoal = new VectorGeometry(-Constants.PITCH_WIDTH / 2, 0);
        return us.location.distance(ourGoal) < ball.location.distance(ourGoal);
    }

    @Override
    public void onStart() {
        Khaleesi us = (Khaleesi)Strategy.currentRobotBase;
        Ball ball = Strategy.world.getBall();
        if (ball == null) return;
        // From what I'm seeing, we retreat to our goal whilst avoiding the ball
        us.MOTION_CONTROLLER.setActive(true);
        us.MOTION_CONTROLLER.addObstacle(new Obstacle((int)ball.location.x, (int)ball.location.y, 30));
        us.MOTION_CONTROLLER.setDestination(new ConstantPoint(-Constants.PITCH_WIDTH / 2, 0));
        us.MOTION_CONTROLLER.setHeading(new BallPoint());
        us.MOTION_CONTROLLER.setTolerance(-1);
    }

    @Override
    public void update() {
        Khaleesi us = (Khaleesi)Strategy.currentRobotBase;
        if (safe()) {
            us.MOTION_CONTROLLER.clearObstacles();
        }

    }
}
