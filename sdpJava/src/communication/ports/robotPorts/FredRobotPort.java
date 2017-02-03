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

    // Retain state for dribbling and kicking (allow single-var updates)
    private double curDribblerPower = 0;
    private double curKickerPower = 0;

    public FredRobotPort() {
        super("pang");
        curDribblerPower = 0;
        curKickerPower = 0;
    }

    /**
     * We need to be clear as to how the wheels are spinning due to the awkward and
     * counter-intuitive ways the motors are being placed in our robot
     * To be exact:
     * @param frontLeft is going clockwise
     * @param frontRight is going counter clockwise
     * @param back is going clockwise
     *
     */
    @Override
    public void threeWheelHolonomicMotion(double frontRight, double frontLeft, double back) {
        this.sdpPort.commandSender("r", (int) frontRight, (int) frontLeft, (int) back);
    }

    // command to spin dribbler and kicker
    @Override
    public void updateDribbler(double dribblerPower) {
        curDribblerPower = dribblerPower;
        this.sdpPort.commandSender("dk", (int) dribblerPower, (int) curKickerPower);
    }

    @Override
    public void updateKicker(double kickerPower) {
        curKickerPower = kickerPower;
        this.sdpPort.commandSender("dk", (int) curDribblerPower, (int) kickerPower);
    }

    @Override
    public void dribblerKicker(double dribbler, double kicker) {
        curDribblerPower = dribbler;
        curKickerPower = kicker;
        this.sdpPort.commandSender("dk", (int) dribbler, (int) kicker);
    }

    //   will keep it for reference, there is some extra login in arduino code for this
    @Override
    public void propeller(int spin) {
        this.sdpPort.commandSender("kick", spin);
    }


}
