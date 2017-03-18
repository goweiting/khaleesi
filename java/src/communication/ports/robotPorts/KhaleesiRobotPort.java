package communication.ports.robotPorts;

import communication.ports.interfaces.FourWheelHolonomicRobotPort;
import communication.ports.interfaces.RobotPort;

/** Created by Rado Kirilchev */
public class KhaleesiRobotPort extends RobotPort implements FourWheelHolonomicRobotPort {

    public KhaleesiRobotPort() {
        super("pang");
    }

    @Override
    public void fourWheelHolonomicMotion(double front, double back, double left, double right) {
        this.sdpPort.commandSender("r", (int)front, (int)back, (int)left, (int)right);
    }

    public void spamKick() {
        this.sdpPort.commandSender("k");
    }

}
