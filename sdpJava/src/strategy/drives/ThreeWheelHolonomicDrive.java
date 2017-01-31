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
   * @param force The direction that the robot should move at the current timeslice to reached the desired direction
   * @param rotation The rotation required to achieve the goal?
   * @param factor scaling factor for todo:idk what!
   */
  public void move(RobotPort port, DirectedPoint location, VectorGeometry force, double rotation,
      double factor) {
    assert (port instanceof ThreeWheelHolonomicRobotPort);

    // dir is the output of the designed direction we intent to go to
    VectorGeometry dir = new VectorGeometry();
    double angle = force.angle();
    force.copyInto(dir).coordinateRotation(force.angle() - location.direction);
    factor = Math.min(1, factor);

    //double lim = this.MAX_MOTION - Math.abs(rotation* this.MAX_ROTATION *factor);

    double backmotor = 0;
    if (rotation > 0) {
      // rotate back motor to the right
      backmotor -= rotation;
      if (backmotor < -100) {
        backmotor = -100;
      }
    } else {
      backmotor += rotation;
      if (backmotor > 100) {
        backmotor = 100;
      }
    }
    double actual_x = location.x;
    double actual_y = location.y;

    double goal_x = dir.x;
    double goal_y = dir.y;

    double diff_x = goal_x - actual_x;
    double diff_y = goal_y - actual_y;

    double frontLeft = MAX_ROTATION + diff_x;
    double frontRight = MAX_ROTATION + diff_x;

    frontLeft -= diff_y;
    frontRight += diff_y;

    //double back = -dir.x;
    //double frontRight = -dir.y;
    //double right = dir.x;
    //double normalizer = Math.max(Math.max(Math.abs(back), Math.abs(right)), Math.max(Math.abs(frontLeft), Math.abs(frontRight)));

    //normalizer = lim / normalizer * factor;
    //frontLeft = frontLeft * normalizer + rotation * this.MAX_ROTATION;
    //frontRight  = frontRight*normalizer + rotation * this.MAX_ROTATION;
    //back = back * normalizer + rotation * this.MAX_ROTATION;
    // right motor is no longer user (as we have only 3) thus all logic has to be either replaced
    // or modified, but I just don't get it yet
    //right = right * normalizer + rotation * this.MAX_ROTATION;

    // Instructs the robot to to the desired location with that amount of "power"
    ((ThreeWheelHolonomicRobotPort) port).threeWheelHolonomicMotion(0, 0, backmotor);

  }
}
