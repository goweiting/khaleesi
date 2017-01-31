package strategy.drives;

import communication.ports.interfaces.RobotPort;
import communication.ports.interfaces.ThreeWheelHolonomicRobotPort;
import org.ejml.data.DenseMatrix64F;
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
      RobotPort port, DirectedPoint location, VectorGeometry force, double rotation, double
      factor) {
    assert (port instanceof ThreeWheelHolonomicRobotPort);

    RobotKinematic kinematics = new RobotKinematic();
    DenseMatrix64F v = kinematics.inverseKinematic(location, force, rotation);

    // Instructs the robot to to the desired location with that amount of "speed"
    ((ThreeWheelHolonomicRobotPort) port).threeWheelHolonomicMotion(v.get(0), v.get(1), v.get(2));
  }

}
