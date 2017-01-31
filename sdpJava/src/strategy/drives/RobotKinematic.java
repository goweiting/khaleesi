package strategy.drives;

import java.util.Vector;
import org.ejml.data.FixedMatrix3x3_64F;
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
  private final double frontRight = pi / 4; // these are the angles of the wheels wrt to point P
  private final double frontLeft = 5 * pi / 8;
  private final double backWheel = -1 * pi / 2;
  private final double distL = 1; // assume distance between wheels and P is 1
  private final double radius = 1; // assume that the radius of the wheels is r
  private final double gamma = 0; // the roller are 90-degrees to the wheel
  private final double beta = 0; // because the wheels are tangent to the robot's circular body

  public FixedMatrix3x3_64F epsilon_I = new FixedMatrix3x3_64F();
  public FixedMatrix3x3_64F J_2 = new FixedMatrix3x3_64F(
      radius, 0, 0,
      0, radius, 0,
      0, 0, radius
  ); // this is basically a diagonal with r
  public FixedMatrix3x3_64F J_1f = new FixedMatrix3x3_64F(
      Math.sin(frontLeft), -1 * Math.cos(frontLeft), -1 * distL,
      0, -1 * Math.cos(frontRight), -1 * distL,
      Math.sin(backWheel), -1 * Math.cos(backWheel), -1 * distL
  );

  /**
   * Using the forward kinematic model, computes the speed for each wheels;
   *
   * @param location Current location of the robot (x,y,theta)
   * @param force The desired location
   * @param rotation The desired angle
   * Returns the speeds for each of the motors
   */
  public double[] forwardKinematic(
      DirectedPoint location, VectorGeometry force, double rotation) {
    double theta = location.getDirection();// current location
//    FixedMatrix3x3_64F orthogonalRotation = new FixedMatrix3x3_64F(
//        Math.cos(theta), Math.sin(theta), 0,
//        -1 * Math.sin(theta), Math.cos(theta), 0,
//        0, 0, 1
//    );



  }

}
