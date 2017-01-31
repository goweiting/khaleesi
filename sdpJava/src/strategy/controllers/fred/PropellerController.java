package strategy.controllers.fred;

import communication.ports.interfaces.PropellerEquipedRobotPort;
import strategy.Strategy;
import strategy.controllers.ControllerBase;
import strategy.robots.RobotBase;
import vision.Robot;

/**
 * Created by Simon Rovder
 */
// Adapting for ad-hoc use with the "kick" command. Don't use this!
public class PropellerController extends ControllerBase {
    private int kickerStatus;

    public PropellerController(RobotBase robot) {
        super(robot);
        this.kickerStatus = 0;
    }

    @Override
    public void setActive(boolean active) {
        super.setActive(active);
        this.kickerStatus = 0;
    }

    private void propell(int newStatus){
        PropellerEquipedRobotPort port = (PropellerEquipedRobotPort) this.robot.port;
        port.propeller(newStatus);
    }

    @Override
    public void perform(){
        assert (this.robot.port instanceof PropellerEquipedRobotPort);

        Robot us = Strategy.world.getRobot(this.robot.robotType);
        if(us != null){
            if(this.isActive()){
                // The current Arduino file knows the following when it comes to status:
                // Works only on one of the motors, too
                // 0 = full stop (kickers)
                // -1 = negative 100 => do the kick
                // 1 = positive 100 => retract (not ideal, should use less force for that)
                propell(0); // Do nothing. Do not use this command. (FIXME?)
            }
        }
    }

}
