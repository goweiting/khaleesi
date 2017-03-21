package communication.ports.robotPorts;

import communication.ports.interfaces.FourWheelHolonomicRobotPort;
import communication.ports.interfaces.RobotPort;
import communication.ports.interfaces.SpammingKickRobotPort;

/**
 * Created by Rado Kirilchev
 */
public class KhaleesiRobotPort extends RobotPort implements
    FourWheelHolonomicRobotPort, SpammingKickRobotPort {

  private static double[] lastSentSpeed = new double[5];

  public KhaleesiRobotPort() {
    super("pang");
  }

  public static double[] getLastSentSpeed() {
    return lastSentSpeed;
  }

  @Override
  public void fourWheelHolonomicMotion(double front, double back, double left, double right) {
    this.sdpPort.commandSender("r", (int) front, (int) back, (int) left, (int) right);
    lastSentSpeed = new double[]{front, back, left, right};
  }

  @Override
  public void spamKick() {
    this.sdpPort.commandSender("k");
  }

}
