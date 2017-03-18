package communication.ports.robotPorts;

import communication.ports.interfaces.FourWheelHolonomicRobotPort;
import communication.ports.interfaces.RobotPort;
import communication.ports.interfaces.SpinnerKickRobotPort;
import strategy.Strategy;
import vision.Robot;
import vision.RobotType;

/** Created by levif */
public class Diag4RobotPort extends RobotPort
    implements SpinnerKickRobotPort, FourWheelHolonomicRobotPort {

  public Diag4RobotPort() {
    super("diag4");
  }

  @Override
  public void fourWheelHolonomicMotion(double front, double back, double left, double right) {
    this.sdpPort.commandSender("r", (int) front, (int) back, (int) left, (int) right);
  }

  @Override
  public void spinnerKick(int spin, int engaged) {
    //NOTE:
    // engaged == 1, move the spinner DOWN to touch the ball
    // engaged == 0, keep the spinner as it is (whether it is DOWN or UP)
    // engaged == -1, move the spinner UP to prepare to collect the ball again

    // spin == 1, run the spinner at full force (for now)
    // spin == 0, don't change the spinner's state
    // spin == -1, turn off the spinner
    //this.sdpPort.commandSender("spinkick", spin, engaged);
  }

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
