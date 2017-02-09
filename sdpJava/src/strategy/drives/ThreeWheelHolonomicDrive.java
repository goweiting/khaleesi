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

  public int MAX_ROTATION = 50;
  public int MAX_MOTION = 100;
  public double[][] FORCE_DECOUPLING = new double[][]{
      {-1, Math.sqrt(3), 1},
      {-1, -Math.sqrt(3), 1},
      {2, 0, 1}
  };

  /**
   * Moving the three wheel holonomic robot to a desired location.
   *
   * @param port interface to command the robot
   * @param location Current location of the robot - the origin (x,y) = (0,0) of the field is the
   * centre of the entire field
   * @param force The direction that the robot should move at the current timeslice to reached the
   * desired direction
   * @param rotation The angle between the robot's heading and the desired point
   * @param factor scaling factor for todo:idk what!
   */
  public void move(
      RobotPort port, DirectedPoint location, VectorGeometry force, double rotation,
      double factor) {
    assert (port instanceof ThreeWheelHolonomicRobotPort);

    rotation /= Math.PI;
    VectorGeometry dir = new VectorGeometry();
    force.copyInto(dir).coordinateRotation(force.angle() - location.direction);
    factor = Math.min(1, factor); // this is basically the P_controller bit

//        double lim = this.MAX_MOTION - Math.abs(rotation * this.MAX_ROTATION * factor);

    double frontRight = FORCE_DECOUPLING[0][0] * dir.x + FORCE_DECOUPLING[0][1] * dir.y;
    double frontLeft = FORCE_DECOUPLING[1][0] * dir.x + FORCE_DECOUPLING[1][1] * dir.y;
    double backWheel = FORCE_DECOUPLING[2][0] * dir.x + FORCE_DECOUPLING[2][1] * dir.y;

    // find the largest speed required and normalise each of the wheel's speed:
    double normalizer = Math.max(Math.abs(frontRight),
        Math.max(Math.abs(frontLeft), Math.abs(backWheel)));

    // refine the priorities on each action
    this.calibrate(rotation);

    frontRight = (frontRight / normalizer * this.MAX_MOTION + rotation * this.MAX_ROTATION);
    frontLeft = (frontLeft / normalizer * this.MAX_MOTION + rotation * this.MAX_ROTATION);
    backWheel = (backWheel / normalizer * this.MAX_MOTION + rotation * this.MAX_ROTATION);

    normalizer = Math.max(Math.abs(frontRight), Math.max(Math.abs(frontLeft), Math.abs(backWheel)));

    // SIMPLE NORMALISER FOR SCALING THE SPEED; USAGE OF FACTOR TO SEE WHAT HAPPENS
    frontRight = (frontRight / normalizer) * (100 * factor);
    frontLeft = (frontLeft / normalizer) * (100 * factor);
    backWheel = (backWheel / normalizer) * (100 * factor);

    // DEBUG
    SDPConsole.writeln("FL: " + frontLeft + " FR: " + frontRight + "Back: " + backWheel);

    // Instructs the robot to to the desired location with that amount of "speed"
    ((ThreeWheelHolonomicRobotPort) port)
        .threeWheelHolonomicMotion(frontLeft, frontRight, backWheel);
  }

  /**
   * Sets the MAX_MOTION and MAX_ROTATION based on the amount of rotation required towards the
   * goal. Ideally, this reduces the number of circles the robot goes before it reaches the goal
   *
   * @param rotation the rotation required for the robot to face its desired heading in Radians
   */
  private void calibrate(double rotation) {
    // Implementing a Linear Model:
    // if is PI, go up to 80 => gradient = 80/pi
    this.MAX_ROTATION = (int) Math.abs(80 * rotation / Math.PI);

    // A logarithmic model:
    // this.MAX_ROTATION = (int) Math.abs(Math.log10(rotation) * 20);

    this.MAX_MOTION = 100 - this.MAX_ROTATION;
  }
}
