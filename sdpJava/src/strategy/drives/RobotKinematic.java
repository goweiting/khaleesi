package strategy.drives;

import vision.tools.DirectedPoint;
import vision.tools.VectorGeometry;

/**
 * Created by goweiting on 31/01/17.
 * A class to encapsulates the mathematical calculation for the speed of the wheels
 */
public class RobotKinematic {

  private final double pi = Math.PI; // shorthand

  // The kicker of the robot is pointing towards the positive y-axis of the field
  // A point P is chosen wrt to the centre of the robot
  private final double frontRight = pi / 6;
  private final double frontLeft = 5 * pi / 6;
  private final double backWheel = 9 * pi / 6;

//  private DenseMatrix64F m = new DenseMatrix64F(3, 3, true,
//      Math.cos(frontLeft + pi / 2), Math.cos(frontRight + pi / 2), Math.cos(backWheel + pi / 2),
//      Math.sin(frontLeft + pi / 2), Math.sin(frontRight + pi / 2), Math.sin(backWheel + pi / 2),
//      1, 0, 1
//  );

  public RobotKinematic() {
  }

  /**
   * Apply the inverse kinematic model to derieved the speed required to drive the robot
   * from current location to the desired vector v = (force, rotation).T
   *
   * @param location the current location of the robot
   * @param force the desired location in (x,y)
   * @param rotation difference between the the heading of the robot and the desired location
   */
  public double[] inverseKinematic(DirectedPoint location, VectorGeometry force,
      double rotation) {

    // The desired vector to return (the speed of each wheels)
    double new_x = force.getX() - location.getX();
    double new_y = force.getY() - location.getY();
    double theta = rotation - location.getDirection();

    double[] wheelSpeeds = new double[3];
    wheelSpeeds[0] = 0.5 * new_x + (-1 * (Math.sqrt(3) / 2) * new_y) + (-1 * theta);
    wheelSpeeds[1] = 0.5 * new_x + (Math.sqrt(3) / 2) * new_y + (-1 * theta);
    wheelSpeeds[2] = -1 * new_x + 0 + (-1 * theta);

    return wheelSpeeds;
  }

  private static int[] checkValue(int[] wheelSpeeds) {
    int maxV = getMaxValue(wheelSpeeds);
    for (int i : wheelSpeeds) {
      wheelSpeeds[i] = wheelSpeeds[i] / maxV * 100;
    }
    return wheelSpeeds;
  }

  private static int getMaxValue(int[] array) {
    int maxValue = array[0];
    for (int i = 1; i < array.length; i++) {
      if (array[i] > maxValue) {
        maxValue = array[i];
      }
    }
    return maxValue;
  }

}

