package communication.ports.robotPorts;

import communication.ports.interfaces.FourWheelHolonomicRobotPort;
import communication.ports.interfaces.KickerEquippedRobotPort;
import communication.ports.interfaces.RobotPort;
import communication.ports.interfaces.ThreeWheelHolonomicRobotPort;

/** Created by Simon Rovder */
public class KhaleesiRobotPort extends RobotPort
        implements FourWheelHolonomicRobotPort, ThreeWheelHolonomicRobotPort, KickerEquippedRobotPort {

    // Retain state for dribbling and kicking (allow single-var updates)
    private double curDribblerPower = 0;
    private double curKickerPower = 0;

    public KhaleesiRobotPort() {
        super("pang");
        curDribblerPower = 0;
        curKickerPower = 0;
    }

    /**
     * We need to be clear as to how the wheels are spinning due to the awkward and counter-intuitive
     * ways the motors are being placed in our robot To be exact:
     *
     * @param frontLeft is going clockwise
     * @param frontRight is going counter clockwise
     * @param back is going clockwise
     */
    @Override
    public void threeWheelHolonomicMotion(double frontLeft, double frontRight, double back) {
        this.sdpPort.commandSender("md", (int)frontLeft, (int)frontRight, (int)back);
    }

    // Support four-wheel motion too
    @Override
    public void fourWheelHolonomicMotion(double front, double back, double left, double right) {
        this.sdpPort.commandSender("r", (int)front, (int)back, (int)left, (int)right);
    }

    // command to operate kicker
    @Override
    public void updateKicker(double kickerPower) {
        curKickerPower = kickerPower;
        //this.sdpPort.commandSender("dk", (int) curDribblerPower, (int) kickerPower);
        this.sdpPort.commandSender("k", (int) kickerPower);
    }
}
