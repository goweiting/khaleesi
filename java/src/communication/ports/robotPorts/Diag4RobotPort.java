package communication.ports.robotPorts;

import communication.ports.interfaces.FourWheelHolonomicRobotPort;
import communication.ports.interfaces.RobotPort;
import communication.ports.interfaces.SpammingKickRobotPort;
import strategy.Strategy;
import vision.Robot;
import vision.RobotType;

/** Created by levif */
public class Diag4RobotPort extends RobotPort
    implements SpammingKickRobotPort, FourWheelHolonomicRobotPort {

  public Diag4RobotPort() {
    super("diag4");
  }

  @Override
  public void fourWheelHolonomicMotion(double front, double back, double left, double right) {
    this.sdpPort.commandSender("r", (int) front, (int) back, (int) left, (int) right);
  }

  @Override
  public void spamKick() {
    this.sdpPort.commandSender("k");
  }

  @Override
  public void receivedStringHandler(String portMessage) {
    if (portMessage != null) {
      RobotType OUR_ROBOT = RobotType.FRIEND_2;
      Robot r = Strategy.world.getRobot(OUR_ROBOT);

      if (portMessage.equals("B1")) {
        r.setHasBall(true);
        System.out.println("!!!!!!!-----DIAG4 HAS BALL");
      } else if (portMessage.equals("B0")) {
        r.setHasBall(false);
      }
    }
  }
}
