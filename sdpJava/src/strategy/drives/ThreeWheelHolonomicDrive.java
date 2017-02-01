package strategy.drives;

import communication.ports.interfaces.RobotPort;
import communication.ports.interfaces.ThreeWheelHolonomicRobotPort;
import vision.tools.DirectedPoint;
import vision.tools.VectorGeometry;

/**
 * Created by Simon Rovder
 * Edited by Wildfire
 */

public class ThreeWheelHolonomicDrive implements DriveInterface {

  public int MAX_ROTATION = 55;
  public int MAX_MOTION = 100;

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

    // Some debug print out:
    System.out.println("My current location" + location.toString());
    System.out.println("My desired location" + force.toString() + " at angle of :" + rotation);

    // Instructs the robot to to the desired location with that amount of "speed"
    ((ThreeWheelHolonomicRobotPort) port).threeWheelHolonomicMotion();
  }

  /**
   * Apply the inverse kinematic model to derieved the speed required to drive the robot
   * from current location to the desired vector v = (force, rotation).T
   *
   * @param location the current location of the robot
   * @param force the desired location in (x,y)
   * @param rotation difference between the the heading of the robot and the desired location
   */
  public double[] inverseKinematic(
      DirectedPoint location, VectorGeometry force, double rotation) {

    // The desired vector to return (the speed of each wheels)
    double new_x = force.getX() - location.getX();
    double new_y = force.getY() - location.getY();
    double theta = rotation - location.getDirection();

    // Check that theta does not exceed 180degrees (pi), because there's always a simpler way
    // to get there if that happens

    double[] wheelSpeeds = new double[3];
    // its frontLeft, frontRight then backWheel!!
    wheelSpeeds[0] = (-0.3333) * new_x + (-.5774) * new_y + .3333 * theta;
    wheelSpeeds[1] = (-0.3333) * new_x + (.5774) * new_y + .3333 * theta;
    wheelSpeeds[2] = (0.6667) * new_x + 0 + 0.3333 * theta;

    // Added Debug:
    System.out
        .printf("FL: " + wheelSpeeds[0] + " FR: " + wheelSpeeds[1] + "Back: " + wheelSpeeds[2]);
    return checkValue(wheelSpeeds);
  }

  private static double[] checkValue(double[] wheelSpeeds) {
    double maxV = getMaxValue(wheelSpeeds);
    if (maxV == 0) {
      return new double[]{0.0, 0.0, 0.0};
    } else {
      for (int i = 0; i < wheelSpeeds.length; i++) {
        double tmp = wheelSpeeds[i] / maxV * 100;
        if (tmp > 100) {
          wheelSpeeds[i] = 100;
        } else if (tmp < -100) {
          wheelSpeeds[i] = -100;
        } else {
          wheelSpeeds[i] = tmp;
        }
      }
    }
    return wheelSpeeds;
  }

  private static double getMaxValue(double[] array) {
    double maxValue = array[0];
    for (int i = 1; i < array.length; i++) {
      if (Math.abs(array[i]) > maxValue) {
        maxValue = array[i];
      }
    }
    return maxValue;
  }


}
