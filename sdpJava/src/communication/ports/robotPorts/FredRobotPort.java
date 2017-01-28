package communication.ports.robotPorts;

import communication.ports.interfaces.FourWheelHolonomicRobotPort;
import communication.ports.interfaces.PropellerEquipedRobotPort;
import communication.ports.interfaces.RobotPort;

/**
 * Created by Simon Rovder
 */
public class FredRobotPort extends RobotPort implements PropellerEquipedRobotPort, FourWheelHolonomicRobotPort {

    public FredRobotPort() {
        super("pang");
    }

    @Override
    public void fourWheelHolonomicMotion(double frontLeft, double frontRight, double back) {
        this.sdpPort.commandSender("r", (int) frontLeft, (int) frontRight, (int) back);
    }


    public void dribblerKicker(double dribbler, double kicker) {
        this.sdpPort.commandSender("dk", (int) dribbler, (int) kicker);
    }

    @Override
    public void propeller(int spin) {
        this.sdpPort.commandSender("kick", spin);
    }


}
