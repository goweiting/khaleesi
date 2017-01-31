package communication.ports.robotPorts;

import communication.ports.interfaces.DribblerKickerEquippedRobotPort;
import communication.ports.interfaces.ThreeWheelHolonomicRobotPort;
import communication.ports.interfaces.PropellerEquipedRobotPort;
import communication.ports.interfaces.RobotPort;

/**
 * Created by Simon Rovder
 */
public class FredRobotPort extends RobotPort implements PropellerEquipedRobotPort,
        ThreeWheelHolonomicRobotPort, DribblerKickerEquippedRobotPort {

    public FredRobotPort() {
        super("pang");
    }

    @Override
    public void threeWheelHolonomicMotion(double frontLeft, double frontRight, double back) {
        this.sdpPort.commandSender("r", (int) frontLeft, (int) frontRight, (int) back);
    }


    // command to spin dribbler and kicker
    @Override
    public void dribblerKicker(double dribbler, double kicker) {
        this.sdpPort.commandSender("dk", (int) dribbler, (int) kicker);
    }

    // will keep it for reference, there is some extra login in arduino code for this
    @Override
    public void propeller(int spin) {
        this.sdpPort.commandSender("kick", spin);
    }


}
