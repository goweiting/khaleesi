package strategy.drives;

import PolarCoordNavigation.Coordinates.CartesianCoordinate;
import PolarCoordNavigation.Coordinates.PolarCoordinate;
import PolarCoordNavigation.PolarNavigator;
import com.sun.xml.internal.bind.v2.schemagen.xmlschema.Import;
import communication.SDPPort;
import communication.ports.interfaces.FourWheelHolonomicRobotPort;
import communication.ports.interfaces.RobotPort;
import communication.ports.interfaces.SpammingKickRobotPort;
import communication.ports.interfaces.AngryBirdPort;
import strategy.Strategy;
import strategy.points.ImportantPoints;
import vision.Robot;
import vision.RobotType;
import vision.colorAnalysis.SDPColor;
import vision.constants.Constants;
import vision.gui.SDPConsole;

/**
 * Created by levi on 04/02/17.
 */
public class HorizVertSimpleDrive implements DriveInterface {

    private static final boolean DEBUG_MODE = true;
    PolarNavigator polarNavigator = new PolarNavigator();
    // Rado Kirilchev: We'd like to be able to set different targets for the robot, not just the opposing goal.
    CartesianCoordinate curTarget = null;
    private RobotPort commandPort;
    private boolean firstFlap = true;

    public String robotInPath(PolarCoordinate[] robots, double ourRadius,
                              double ourAngle, double targetRadius, double targetAngle) {

        // check if robot is in the destination path of diag4 and where
        String whereIsTheRobot = "Nowhere";
        final double angleThreshold = Math.PI / 40;
        for (PolarCoordinate robot : robots) {
            double targetAngleDiff = Math.abs(robot.getAngle() - targetAngle);
            double robotsAngleDiff = Math.abs(robot.getAngle() - ourAngle);
            if (targetAngleDiff <= angleThreshold && robotsAngleDiff <= angleThreshold) {
                if (robot.getRadius() < ourRadius && robot.getRadius() >= targetRadius) {
                    whereIsTheRobot = "In front";
                } else if (robot.getRadius() < targetRadius) {
                    whereIsTheRobot = "Behind";
                }
            } else if (robot.getAngle() < Math.max(ourAngle, targetAngle) && robot.getAngle() > Math.min(ourAngle, targetAngle)) {
                if (robot.getRadius() >= targetRadius && robot.getRadius() < ourRadius) {
                    whereIsTheRobot = "In front";
                }
            }
        }
        return whereIsTheRobot;
    }

    public BallTrackState getBallTrackState() {
        //convert the robots and ball position to polar coords
        PolarCoordinate diag4PolarCoords = ImportantPoints.getRobotPolar(RobotType.FRIEND_2);
        PolarCoordinate friendPolarCoords = ImportantPoints.getRobotPolar(RobotType.FRIEND_1);
        PolarCoordinate foe1PolarCoords = ImportantPoints.getRobotPolar(RobotType.FOE_1);
        PolarCoordinate foe2PolarCoords = ImportantPoints.getRobotPolar(RobotType.FOE_2);
        PolarCoordinate ballPolarCoords = ImportantPoints.getBallPolar();

        PolarCoordinate[] players = {friendPolarCoords, foe1PolarCoords, foe2PolarCoords};
        //TODO: instead of doing diff of angle and then diff of radius,
        //TODO:   treat these like cartesian points and update both in
        //TODO:   conjunction.

        //calculate difference between robot angle and ball angle
        double angleDiff = ballPolarCoords.getAngle() - diag4PolarCoords.getAngle();
        double angleDiffAbs = Math.abs(angleDiff);
        //calculate difference between robot radius and ball radius
        double radiusDiff = ballPolarCoords.getRadius() - diag4PolarCoords.getRadius();

//    System.out.println("DIFF ANGLE: " + angleDiff);
//    System.out.println("DIFF RADIUS: " + radiusDiff);

        //CONSTANTS
        // By default, if Khaleesi needs to attack, these parameters should allow it to go sufficiently close to
        // KICK the ball
        double radiusThreshold = 30.0;
        double radiusOffset = 0.0;
        float THE_ANGLE = ImportantPoints.getBallPolar().getAngle();
        float THE_RADIUS = ImportantPoints.getBallPolar().getRadius();
        if (THE_RADIUS < 120) {
            // If the ball is far away (i.e. near to the enemy goal, let JinShip do his thing.
            // We go really close to our own goal post.
            if (THE_ANGLE > 0.8 && THE_ANGLE < 2) {
                radiusThreshold = 100.0;
                radiusOffset = -20.0;
                ((AngryBirdPort) commandPort).toggle(false); // OFF THE IR Sensors!
            } else {
                // GO TO HOUSE
                SDPConsole.writeln(" BALL IS FAR AWAY. GOING BACK TO MY POST - DEFENDING!"); // DEBUG
                polarNavigator.SetTargetState(200, (float) 1.577);
                ((AngryBirdPort) commandPort).toggle(true); // TRIGGER the IR sensors
                return BallTrackState.GO_TO_HOUSE;
            }
        } else {
            SDPConsole.writeln("BALL NEAR OUR HOUSE DETECTED!");
            ((AngryBirdPort) commandPort).toggle(true); // TRIGGER the IR sensors
        }

//    final double angleThreshold = Math.PI / 10;
        final double angleThreshold2 = Math.PI / 40;
        //-----------------------------------

        //FOR TESTING IF THE ROBOT GOES TO THE BALL---
//	  PolarCoordinate ballTarget = ImportantPoints.getBallPolar();
//	  polarNavigator.SetTargetState(ballTarget.getRadius(), ballTarget.getAngle());
//	  if(ballTarget != null)
//	  	return BallTrackState.UNKNOWN;
        //------------

        //kicking
        if (angleDiffAbs <= angleThreshold2
                && diag4PolarCoords.getRadius() - ballPolarCoords.getRadius() < 25
                && diag4PolarCoords.getRadius() >= ballPolarCoords.getRadius()) {
            ((SpammingKickRobotPort) commandPort).spamKick();
        }

	/* case A: path on a straight line; check if there is
     a robot on it too and where */
        if (angleDiffAbs < angleThreshold2 && diag4PolarCoords.getRadius() > ballPolarCoords.getRadius()) {
            double targetRadius = ballPolarCoords.getRadius() - radiusOffset;
            double targetAngle = ballPolarCoords.getAngle();
            polarNavigator.SetTargetState((float) targetRadius, (float) targetAngle);
            // if robot in the middle of diag4's path move next to the ball
            if (robotInPath(players, diag4PolarCoords.getRadius(), diag4PolarCoords.getAngle(), targetRadius, targetAngle) == "In front") {
                System.out.println("GOING NEXT TO THE BALL");
                double actionTargetRadius = ballPolarCoords.getRadius();
                double a = 2 * Math.asin(radiusThreshold / (2.0 * ballPolarCoords.getRadius()));
                double actionTargetAngle = ballPolarCoords.getAngle() + (ballPolarCoords.getAngle() > Math.PI / 2.0 ? -a : a);
                polarNavigator.SetTargetState((float) actionTargetRadius, (float) actionTargetAngle);
                return BallTrackState.GO_NEXT_TO_BALL;
      /* if robot in the middle of ball and enemy goal, block any passes by staying behind
          the ball */
            } else if (robotInPath(players, diag4PolarCoords.getRadius(), diag4PolarCoords.getAngle(), targetRadius, targetAngle) == "Behind") {
                System.out.println("GOING BEHIND BALL");
                double actionTargetRadius = ballPolarCoords.getRadius() + radiusThreshold;
                double actionTargetAngle = ballPolarCoords.getAngle();
                polarNavigator.SetTargetState((float) actionTargetRadius, (float) actionTargetAngle);
                return BallTrackState.GO_BEHIND_BALL;
                // if we reach this stage path is clear
            } else {
                System.out.println("GOING TO THE BALL");
                double actionTargetRadius = targetRadius;
                double actionTargetAngle = targetAngle;
                polarNavigator.SetTargetState((float) actionTargetRadius, (float) actionTargetAngle);
                return BallTrackState.GO_TO_BALL;
            }
        } else if (diag4PolarCoords.getRadius() > ballPolarCoords.getRadius()) {
            double actionTargetRadius = ballPolarCoords.getRadius() + radiusThreshold;
            double actionTargetAngle = ballPolarCoords.getAngle();
            if (robotInPath(players, diag4PolarCoords.getRadius(),
                    diag4PolarCoords.getAngle(), actionTargetRadius, actionTargetAngle) == "In front") {
                System.out.println("Foe is in front");
                System.out.println("GOING NEXT TO THE BALL");
                actionTargetRadius = ballPolarCoords.getRadius();
                double a = 2 * Math.asin(radiusThreshold / (2.0 * ballPolarCoords.getRadius()));
                actionTargetAngle = ballPolarCoords.getAngle() + (ballPolarCoords.getAngle() > Math.PI / 2.0 ? -a : a);
            } else {
                System.out.println("Foe is nowhere");
                System.out.println("GOING BEHIND BALL");
            }
            polarNavigator.SetTargetState((float) actionTargetRadius, (float) actionTargetAngle);
            return BallTrackState.GO_BEHIND_BALL;
        } else if (diag4PolarCoords.getRadius() < ballPolarCoords.getRadius()) {
            System.out.println("GOING NEXT TO THE BALL");
            double actionTargetRadius = ballPolarCoords.getRadius();
            double a = 2 * Math.asin(radiusThreshold / (2.0 * ballPolarCoords.getRadius()));
            double actionTargetAngle = ballPolarCoords.getAngle() + (ballPolarCoords.getAngle() > Math.PI / 2.0 ? -a : a);
            polarNavigator.SetTargetState((float) actionTargetRadius, (float) actionTargetAngle);
            return BallTrackState.GO_NEXT_TO_BALL;
        } else {
            System.out.println("UNKNOWN STATE");
            double actionTargetRadius = diag4PolarCoords.getRadius();
            double actionTargetAngle = diag4PolarCoords.getAngle();
            polarNavigator.SetTargetState((float) actionTargetRadius, (float) actionTargetAngle);
            return BallTrackState.UNKNOWN;
        }
    }

    public double[] getActionBallTrackedState(double usAngleDirection) {
        getBallTrackState();

        return this.polarNavigator.TransformDrive4Wheel(
                ImportantPoints.getRobotPolar(RobotType.FRIEND_2), usAngleDirection);
    }


    @Override
    //------------------------------------------------------------------------------
    //IMPORTANT NOTE: we don't give two shits about the parameters in this method. An important TODO is
    // to change this from the FRED codebase, etc.
    //------------------------------------------------------------------------------
    public void move(RobotPort port) {
        assert (port instanceof FourWheelHolonomicRobotPort);

        //try to get our robot from the world
        Robot us = Strategy.world.getRobot(RobotType.FRIEND_2);
        if (us == null) {
            return;
        }

        // First flap to reset the wings:
//        if (firstFlap){
//            ((AngryBirdPort) commandPort).flap();
//            System.out.println("FLIPPED ONCE TO RESET");
//            firstFlap = false; //
//        }

        //FIRST WE UPDATE OUR ORIGIN OF POLAR COORDS TO THE ENEMY GOAL
        // (just incase for some reason this has changed)
        if (curTarget == null) curTarget = ImportantPoints.getEnemyGoalCartesian();
        ImportantPoints.setOrigin(curTarget);

        commandPort = port;

        double[] totalPowerDrive = getActionBallTrackedState(us.location.direction);

        //send drive to wheels
        ((FourWheelHolonomicRobotPort) port).
                fourWheelHolonomicMotion(totalPowerDrive[0], totalPowerDrive[1], totalPowerDrive[2], totalPowerDrive[3]);
    }

    @Override
    public void setCurrentLookTarget(CartesianCoordinate target) {
        curTarget = target;
    }

    //TODO: this can just be represented by two booleans. For now we do this
    //TODO:   to make it clear what we are trying to do
    public enum BallTrackState {
        GO_BEHIND_BALL,
        GO_NEXT_TO_BALL,
        GO_TO_BALL,
        GO_TO_HOUSE,
        UNKNOWN
    }
}


