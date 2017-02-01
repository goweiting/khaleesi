package strategy.drives;

import communication.ports.interfaces.RobotPort;
import communication.ports.interfaces.ThreeWheelHolonomicRobotPort;
import vision.gui.SDPConsole;
import vision.tools.DirectedPoint;
import vision.tools.VectorGeometry;

/**
 * Created by Simon Rovder
 * Edited by Wildfire
 */

public class ThreeWheelHolonomicDrive implements DriveInterface {

  public int MAX_ROTATION = 55; // todo: what is these for?
  public int MAX_MOTION = 100;
  public double[][] FORCE_DECOUPLING = new double[][]{
      {-.3333, -.5774, .3333},
      {-.3333, .5774, .3333},
      {.6667, 0, .3333}
  };

  /**
   * Moving the three wheel holonomic robot to a desired location.
   *
   * @param port interface to command the robot
   * @param location Current location of the robot - the origin (x,y) = (0,0) of the field is the
   * centre of the entire field
   * @param force The direction that the robot should move at the current timeslice to reached the
   * desired direction
   * @param rotation The rotation required to achieve the goal?
   * @param factor scaling factor for todo:idk what!
   */
  public void move(
      RobotPort port, DirectedPoint location, VectorGeometry force, double rotation,
      double factor) {
    assert (port instanceof ThreeWheelHolonomicRobotPort);

    VectorGeometry dir = new VectorGeometry();
    force.copyInto(dir).coordinateRotation(force.angle() - location.direction);
    factor = Math.min(1, factor); // this is basically the P_controller bit

//    double lim = this.MAX_MOTION - Math.abs(rotation * this.MAX_ROTATION * factor); // todo:
// not exactly sure what this does so just leaving it here for now

    double frontRight = FORCE_DECOUPLING[0][0] * dir.x + FORCE_DECOUPLING[0][1] * dir.y
        + FORCE_DECOUPLING[0][3] * force.angle();
    double frontLeft = FORCE_DECOUPLING[1][0] * dir.x + FORCE_DECOUPLING[1][1] * dir.y +
        FORCE_DECOUPLING[1][3] * force.angle();
    double backWheel = FORCE_DECOUPLING[2][0] * dir.x + FORCE_DECOUPLING[2][1] * dir.y +
        FORCE_DECOUPLING[2][3] * force.angle();

    // find the largest speed required and normalise each of the wheel's speed:
    double normalizer = Math.max(Math.abs(frontRight),
        Math.max(Math.abs(frontLeft), Math.abs(backWheel)));

//    normalizer = lim / normalizer * factor;
//    frontRight = frontRight * normalizer + rotation * this.MAX_ROTATION;
//    frontLeft = frontLeft * normalizer + rotation * this.MAX_ROTATION;
//    backWheel = backWheel * normalizer + rotation * this.MAX_ROTATION;

    // SIMPLE NORMALISER FOR SCALING THE SPEED; USAGE OF FACTOR TO SEE WHAT HAPPENS
    frontRight = frontRight / normalizer * 100 * factor;
    frontLeft = frontLeft / normalizer * 100 * factor;
    backWheel = backWheel / normalizer * 100 * factor;

    // DEBUG
    System.out
        .printf("FL: " + frontLeft + " FR: " + frontRight + "Back: " + backWheel);

    // Instructs the robot to to the desired location with that amount of "speed"
    ((ThreeWheelHolonomicRobotPort) port)
        .threeWheelHolonomicMotion(frontLeft, frontRight, backWheel);
  }
}
