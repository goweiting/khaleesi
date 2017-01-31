package communication.ports.robotPorts;

import communication.ports.interfaces.DribblerKickerEquippedRobotPort;
import communication.ports.interfaces.PropellerEquipedRobotPort;
import communication.ports.interfaces.RobotPort;
import communication.ports.interfaces.ThreeWheelHolonomicRobotPort;

/**
 * Created by Simon Rovder
 */
public class FredRobotPort extends RobotPort implements
    PropellerEquipedRobotPort, ThreeWheelHolonomicRobotPort, DribblerKickerEquippedRobotPort {

  public FredRobotPort() {
    super("pang");
  }

  @Override
  /**
   * We need to be clear as to how the wheels are spinning due to the awkward and
   * counter-intuitive ways the motors are being placed in our robot
   * To be exact:
   * @param frontLeft is going clockwise
   * @param frontRight is going counter clockwise
   * @param back is going clockwise
   *
   */
  public void threeWheelHolonomicMotion(double frontRight, double frontLeft, double back) {
    // we have to change the sign of the frontRight:
    frontRight = -1 * frontRight;
    this.sdpPort.commandSender("r", (int) frontRight, (int) frontLeft, (int) back);
  }

  // command to spin dribbler and kicker
  public void dribblerKicker(double dribbler, double kickerL, double kickerR) {
    this.sdpPort.commandSender("dk", (int) dribbler, (int) kickerL, (int) kickerR);
  }

  // command to spin dribbler and kicker
  @Override
  public void dribblerKicker(double dribbler, double kicker) {
    this.sdpPort.commandSender("dk", (int) dribbler, (int) kicker);
  }

  public void kicker(double kickerL, double kickerR) {
    this.sdpPort.commandSender("kicker", (int) kickerL, (int) kickerR);
  }

//   will keep it for reference, there is some extra login in arduino code for this
    @Override
    public void propeller(int spin) {
      this.sdpPort.commandSender("kick", spin);
    }


}
