package communication.ports.robotPorts;

import communication.ports.interfaces.AngryBirdPort;
import communication.ports.interfaces.FourWheelHolonomicRobotPort;
import communication.ports.interfaces.RobotPort;
import communication.ports.interfaces.SpammingKickRobotPort;

/**
 * Created by Rado Kirilchev and GWT and Justin R
 */
public class KhaleesiRobotPort extends RobotPort implements
        FourWheelHolonomicRobotPort, SpammingKickRobotPort, AngryBirdPort {

    // IMPORTANT: THIS MUST BE IN SYNC WITH THE FIRMWARE!
    public boolean wingState;

    public KhaleesiRobotPort() {
        super("pang");
        wingState = false; // at the very start, the wings are not enabled.
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
    public void toggle(boolean status) {
        if (wingState != status) return; // prevent spamming of toggle command
        wingState = status; // change the state
        if (wingState){
            this.sdpPort.commandSender("toggle", 1); // triple triggered
            this.sdpPort.commandSender("toggle", 1); // triple triggered
            this.sdpPort.commandSender("toggle", 1); // triple triggered
        } else {
            this.sdpPort.commandSender("toggle", 0); // triple triggered
            this.sdpPort.commandSender("toggle", 0); // triple triggered
            this.sdpPort.commandSender("toggle", 0); // triple triggered
        }
    }

    @Override
    public void flap() {
        this.sdpPort.commandSender("flap");
    }
}
