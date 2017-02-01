package strategy.actions.other;

import strategy.Strategy;
import strategy.actions.ActionException;
import strategy.actions.ActionBase;
import strategy.actions.offense.OffensiveKick;
import strategy.points.basicPoints.BallPoint;
import strategy.navigation.Obstacle;
import strategy.points.basicPoints.ConstantPoint;
import communication.ports.robotPorts.FredRobotPort;
import strategy.robots.Fred;
import strategy.robots.RobotBase;
import vision.Ball;
import vision.Robot;
import vision.RobotType;
import vision.constants.Constants;
import vision.tools.VectorGeometry;

/**
 * Created by Simon Rovder
 */
public class GoToBall extends ActionBase {
    Robot us = Strategy.world.getRobot(RobotType.FRIEND_2);
    public GoToBall(RobotBase robot) {
        super(robot);
        this.rawDescription = " Go To Ball";
    }

    @Override
    public void enterState(int newState) {
        if (newState == 0) {
            if (this.robot instanceof Fred) {
                //   ((Fred) this.robot).MOTION_CONTROLLER.


                //Robot us = Strategy.world.getRobot(RobotType.FRIEND_2);
                Ball ball = Strategy.world.getBall();
                if (us == null || ball == null) return;

                //this.robot.MOTION_CONTROLLER.addObstacle(new Obstacle((int) ball.location.x, (int) ball.location.y, 30));
                this.robot.MOTION_CONTROLLER.setDestination(new ConstantPoint(new BallPoint().getX(), new BallPoint().getY()));
                this.robot.MOTION_CONTROLLER.setHeading(new BallPoint());
                this.robot.MOTION_CONTROLLER.setTolerance(-1);
                ((Fred) this.robot).DRIBBLER_CONTROLLER.setActive(true);

            }
        }
    }

        @Override
        public void tok () throws ActionException {
            if (haveBall()) {
                this.robot.ACTION_CONTROLLER.setAction(new OffensiveKick(this.robot));
                throw new ActionException(true, false);
            } else {
                ((Fred)this.robot).KICKER_CONTROLLER.setActive(false);
            }

        }

    public static boolean haveBall() {
        Robot us = Strategy.world.getRobot(RobotType.FRIEND_2);
        if (us.location.distance(new BallPoint().getX(), new BallPoint().getY()) < 20) {
            return true;
        }
        return false;
    }

}
