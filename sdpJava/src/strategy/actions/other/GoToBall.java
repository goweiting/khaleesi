package strategy.actions.other;

import strategy.Strategy;
import strategy.actions.ActionBase;
import strategy.points.basicPoints.BallPoint;
import strategy.robots.Khaleesi;
import strategy.robots.RobotBase;
import vision.Ball;
import vision.Robot;
import vision.RobotType;

/**
 * Created by Simon Rovder
 */
public class GoToBall extends ActionBase {
    Robot us = Strategy.world.getRobot(RobotType.FRIEND_2);

    public GoToBall(RobotBase robot) {
        this.rawDescription = "Go To Ball";
    }

    public static boolean haveBall() {
        Robot us = Strategy.world.getRobot(RobotType.FRIEND_2);
        if (us.location.distance(new BallPoint().getX(), new BallPoint().getY()) < 20) {
            return true;
        }
        return false;
    }

    @Override
    public void onStart() {
        Khaleesi us = (Khaleesi)Strategy.currentRobotBase;
        Ball ball = Strategy.world.getBall();
        if (ball == null) return;

        //this.robot.MOTION_CONTROLLER.addObstacle(new Obstacle((int) ball.location.x, (int) ball.location.y, 30));
        us.MOTION_CONTROLLER.setDestination(new BallPoint());
        us.MOTION_CONTROLLER.setHeading(new BallPoint());
        us.MOTION_CONTROLLER.setTolerance(-1);
        us.DRIBBLER_CONTROLLER.setActive(true);
    }

    @Override
    public void update() {

    }

}