package strategy.drives;

import PolarCoordNavigation.Coordinates.CartesianCoordinate;
import PolarCoordNavigation.Coordinates.PolarCoordinate;
import PolarCoordNavigation.PolarNavigator;
import communication.ports.interfaces.FourWheelHolonomicRobotPort;
import communication.ports.interfaces.RobotPort;
import communication.ports.robotPorts.Diag4RobotPort;
import strategy.Strategy;
import strategy.points.ImportantPoints;
import vision.Robot;
import vision.RobotType;

/**
 * Created by levi on 04/02/17.
 */
public class HorizVertSimpleDrive implements DriveInterface {

  private static final boolean DEBUG_MODE = true;
  PolarNavigator polarNavigator = new PolarNavigator();
  private RobotPort commandPort;

//  public RobotInPathState robotInPath(PolarCoordinate[] robots, double ourRadius,
//      double ourAngle, double targetRadius, double targetAngle) {
//
//    // check if robot is in the destination path of diag4 and where
//    RobotInPathState state = new RobotInPathState(null, "Nowhere");
//    final double angleThreshold = Math.PI / 40;
//
//    for (PolarCoordinate robot : robots) {
//      if (robot == null) {
//        state = new RobotInPathState(null, "Nowhere");
//      } else {
//        double targetAngleDiff = Math.abs(robot.getAngle() - targetAngle);
//        double robotsAngleDiff = Math.abs(robot.getAngle() - ourAngle);
//        if (targetAngleDiff <= angleThreshold && robotsAngleDiff <= angleThreshold) {
//          if (robot.getRadius() < ourRadius && robot.getRadius() >= targetRadius) {
//            state = new RobotInPathState(robot, "In front");
//            return state;
//          } else if (robot.getRadius() < targetRadius) {
//            state = new RobotInPathState(robot, "Behind");
//            return state;
//          }
//        } else if (robot.getAngle() < Math.max(ourAngle, targetAngle) && robot.getAngle() > Math
//            .min(ourAngle, targetAngle)) {
//          if (robot.getRadius() >= targetRadius && robot.getRadius() < ourRadius) {
//            state = new RobotInPathState(robot, "In front");
//            return state;
//          }
//        }
//      }
//    }
//    return state;
//  }

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

    //CONSTANTS TODO---------------------
    double radiusThreshold = 30;
    double radiusOffset = Math.PI * Math.random() * 3; // stochastic behaviour
//    final double angleThreshold = Math.PI / 10;
//    if (ImportantPoints.getBallPolar().getRadius() < 120) {
//      radiusThreshold = 100.0;
//      radiusOffset = -100.0;
//    } else {
//      radiusThreshold = 30.0;
//      radiusOffset = 10.0;
//    }
    final double angleThreshold2 = Math.PI / 40;
    //-----------------------------------

    //FOR TESTING IF THE ROBOT GOES TO THE BALL---
//	  PolarCoordinate ballTarget = ImportantPoints.getBallPolar();
//	  polarNavigator.SetTargetState(ballTarget.getRadius(), ballTarget.getAngle());
//	  if(ballTarget != null)
//	    return BallTrackState.UNKNOWN;
    //------------

    //kicking
    if (angleDiffAbs <= angleThreshold2
        && diag4PolarCoords.getRadius() - ballPolarCoords.getRadius() < 25
        && diag4PolarCoords.getRadius() >= ballPolarCoords.getRadius()) {
      ((Diag4RobotPort) commandPort).spamKick();
    }

        /* case A: path on a straight line; check if there is
           a robot on it too and where */
    if (angleDiffAbs < angleThreshold2 && diag4PolarCoords.getRadius() > ballPolarCoords
        .getRadius()) {
      double targetRadius = ballPolarCoords.getRadius() - radiusOffset;
      double targetAngle = ballPolarCoords.getAngle();
      polarNavigator.SetTargetState((float) targetRadius, (float) targetAngle);
//      RobotInPathState player = robotInPath(players, diag4PolarCoords.getRadius(),
//      diag4PolarCoords.getAngle(), targetRadius, targetAngle);
      // if robot in the middle of diag4's path move next to the ball
//      if (player.getState().equals("In front")) {
//        System.out.println("Foe is in front");
//        if (player.getPlayer().getRadius() > (ballPolarCoords.getRadius() + 40)) {
//          System.out.println("GOING NEXT TO THE BALL");
//          double actionTargetRadius = ballPolarCoords.getRadius();
//          double a = 2 * Math.asin(radiusThreshold / (2.0 * ballPolarCoords.getRadius()));
//          double actionTargetAngle =
//              ballPolarCoords.getAngle() + (ballPolarCoords.getAngle() > Math.PI / 2.0 ? -a : a);
//          polarNavigator.SetTargetState((float) actionTargetRadius, (float) actionTargetAngle);
//          return BallTrackState.GO_NEXT_TO_BALL;
//        } else {
//          System.out.println("GOING BEHIND BALL");
//          double actionTargetRadius = player.getPlayer().getRadius() + 40;
//          double actionTargetAngle = ballPolarCoords.getAngle();
//          polarNavigator.SetTargetState((float) actionTargetRadius, (float) actionTargetAngle);
//          return BallTrackState.GO_BEHIND_BALL;
//        }
//
//                        /* if robot in the middle of ball and enemy goal, block any passes by staying behind
//                              the ball */
//      } else if (player.getState().equals("Behind")) {
//        System.out.println("Foe is in behind");
//        if (player.getPlayer().getRadius() < (ballPolarCoords.getRadius() - 20)) {
//          System.out.println("GOING TO THE BALL");
//          double actionTargetRadius = targetRadius;
//          double actionTargetAngle = targetAngle;
//          polarNavigator.SetTargetState((float) actionTargetRadius, (float) actionTargetAngle);
//          return BallTrackState.GO_TO_BALL;
//
//        } else {
//          System.out.println("GOING BEHIND BALL");
//          double actionTargetRadius = ballPolarCoords.getRadius() + radiusThreshold;
//          double actionTargetAngle = ballPolarCoords.getAngle();
//          polarNavigator.SetTargetState((float) actionTargetRadius, (float) actionTargetAngle);
//          return BallTrackState.GO_BEHIND_BALL;
//        }
//
//        // if we reach this stage path is clear
//      } else {
      System.out.println("Foe is nowhere");
      System.out.println("GOING TO THE BALL");
      double actionTargetRadius = targetRadius;
      double actionTargetAngle = targetAngle;
      polarNavigator.SetTargetState((float) actionTargetRadius, (float) actionTargetAngle);
      return BallTrackState.GO_TO_BALL;
//      }
    } else if (diag4PolarCoords.getRadius() > ballPolarCoords.getRadius()) {
      double actionTargetRadius = ballPolarCoords.getRadius() + radiusThreshold;
      double actionTargetAngle = ballPolarCoords.getAngle();
//      if (robotInPath(players, diag4PolarCoords.getRadius(),
//          diag4PolarCoords.getAngle(), actionTargetRadius, actionTargetAngle).getState()
//          .equals("In front")) {
//        System.out.println("Foe is in front");
//        System.out.println("GOING NEXT TO THE BALL");
//        actionTargetRadius = ballPolarCoords.getRadius();
//        double a = 2 * Math.asin(radiusThreshold / (2.0 * ballPolarCoords.getRadius()));
//        actionTargetAngle =
//            ballPolarCoords.getAngle() + (ballPolarCoords.getAngle() > Math.PI / 2.0 ? -a : a);
//      } else {
//        System.out.println("Foe is nowhere");
//        System.out.println("GOING BEHIND BALL");
//      }
      polarNavigator.SetTargetState((float) actionTargetRadius, (float) actionTargetAngle);
      return BallTrackState.GO_BEHIND_BALL;
    } else if (diag4PolarCoords.getRadius() < ballPolarCoords.getRadius()) {
      System.out.println("GOING NEXT TO THE BALL");
      double actionTargetRadius = ballPolarCoords.getRadius();
      double a = 2 * Math.asin(radiusThreshold / (2.0 * ballPolarCoords.getRadius()));
      double actionTargetAngle =
          ballPolarCoords.getAngle() + (ballPolarCoords.getAngle() > Math.PI / 2.0 ? -a : a);
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

    //FIRST WE UPDATE OUR ORIGIN OF POLAR COORDS TO THE ENEMY GOAL
    // (just incase for some reason this has changed)
    ImportantPoints.setOrigin(ImportantPoints.getEnemyGoalCartesian());

    commandPort = port;

    double[] totalPowerDrive = getActionBallTrackedState(us.location.direction);

    //send drive to wheels
    ((FourWheelHolonomicRobotPort) port).
        fourWheelHolonomicMotion(totalPowerDrive[0], totalPowerDrive[1], totalPowerDrive[2],
            totalPowerDrive[3]);
  }

  @Override
  public void setCurrentLookTarget(CartesianCoordinate target) {
//    curTarget = target;
  }

  //TODO: this can just be represented by two booleans. For now we do this
//TODO:   to make it clear what we are trying to do
  public enum BallTrackState {
    GO_BEHIND_BALL,
    GO_NEXT_TO_BALL,
    GO_TO_BALL,
    UNKNOWN
  }

//  public class RobotInPathState {
//
//    private PolarCoordinate player;
//    private String whereIsTheRobot;
//
//    public RobotInPathState(PolarCoordinate player, String whereIsTheRobot) {
//      this.player = player;
//      this.whereIsTheRobot = whereIsTheRobot;
//    }
//
//    public PolarCoordinate getPlayer() {
//      return this.player;
//    }
//
//    public String getState() {
//      return this.whereIsTheRobot;
//    }
//  }
}


