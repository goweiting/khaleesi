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
  public void threeWheelHolonomicMotion(double frontLeft, double frontRight, double back) {
    // for debugging
    System.out.printf("frontleft : %d\nfrontRight : %d\nback: %d", frontLeft, frontRight, back);
    this.sdpPort.commandSender("r", (int) frontLeft, (int) frontRight, (int) back);
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
