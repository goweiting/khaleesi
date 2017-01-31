package strategy.actions;

import strategy.GUI;
import strategy.Strategy;
import strategy.WorldTools;
import strategy.actions.other.DefendGoal;
import strategy.actions.other.GoToBall;
import strategy.actions.other.GoToSafeLocation;
import strategy.actions.offense.OffensiveKick;
import strategy.actions.offense.ShuntKick;
import strategy.actions.other.Goto;
import strategy.points.basicPoints.BallPoint;
import strategy.points.basicPoints.ConstantPoint;
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
enum BehaviourEnum {
    DEFEND, SHUNT, KICK, SAFE, EMPTY, GO_TO_BALL
}

/**
 * The main Action class. It basically plays the game.
 */
public class Behave extends StatefulActionBase<BehaviourEnum> {


    public static boolean RESET = true;


    public Behave(RobotBase robot) {
        super(robot, null);
    }

    @Override
    public void enterState(int newState) {
        if (newState == 0) {
            this.robot.setControllersActive(true);
        }
        this.state = newState;
    }


    @Override
    public void tok() throws ActionException {

        this.robot.MOTION_CONTROLLER.clearObstacles();
        if (this.robot instanceof Fred) ((Fred) this.robot).PROPELLER_CONTROLLER.setActive(true);
        this.lastState = this.nextState;
        switch (this.nextState) {
            case DEFEND:
                this.enterAction(new DefendGoal(this.robot), 0, 0);
                break;
            case KICK:
                this.enterAction(new OffensiveKick(this.robot), 0, 0);
                break;
            case SHUNT:
                this.enterAction(new ShuntKick(this.robot), 0, 0);
                break;
            case SAFE:
                this.enterAction(new GoToSafeLocation(this.robot), 0, 0);
                break;
            case GO_TO_BALL:
                this.enterAction(new GoToBall(this.robot), 0, 0);
        }
    }

    @Override
    protected BehaviourEnum getState() {
        Ball ball = Strategy.world.getBall();
        if (ball == null) {
            this.nextState = BehaviourEnum.DEFEND;
        } else {
            Robot us = Strategy.world.getRobot(this.robot.robotType);
            if (us == null) {
                // TODO: Angry yelling
            } else {
                VectorGeometry ourGoal = new VectorGeometry(-Constants.PITCH_WIDTH / 2, 0);

                if (us.location.distance(ourGoal) > ball.location.distance(ourGoal)) {
                    this.nextState = BehaviourEnum.SAFE;
                } else {
                    if (us.location.distance(ball.location) > 10){
                        this.nextState = BehaviourEnum.GO_TO_BALL;
                    }
                }
            }
        }
        return this.nextState;
    }
}
