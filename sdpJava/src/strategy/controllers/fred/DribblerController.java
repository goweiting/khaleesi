package strategy.controllers.fred;

import communication.ports.interfaces.DribblerKickerEquippedRobotPort;
import strategy.Strategy;
import strategy.controllers.ControllerBase;
import strategy.robots.RobotBase;
import vision.Robot;

/**
 * Created by s1452923 on 31/01/17.
 */
public class DribblerController extends ControllerBase {

    private boolean dribblerWorking = true;
    private boolean defaultDribblerDirectionNegative = true;

    public DribblerController(RobotBase robot) {
        super(robot);
        this.dribblerWorking = false;
        this.defaultDribblerDirectionNegative = false;
    }

    @Override
    public void setActive(boolean active) {
        super.setActive(active);
    }

    // (Why are we using doubles?)
    // Positive dribbler power means we're "sucking" the ball in;
    // Positive kicker power means we're actually kicking, i.e. propelling the ball outwards.
    private void doAction(double dribblerPower) {
        // Update status before doing anything.
        dribblerWorking = (dribblerPower != 0);

        // Always force positive dribbler power to move to default direction
        if (defaultDribblerDirectionNegative) dribblerPower *= -1;

        // Send command
        ((DribblerKickerEquippedRobotPort)this.robot.port).updateDribbler(dribblerPower);
    }

    @Override
    // This (allegedly) gets called every update cycle. Track state changes in here?
    public void perform() {
        assert (this.robot.port instanceof DribblerKickerEquippedRobotPort);
        Robot us = Strategy.world.getRobot(this.robot.robotType);
        // Abort if we don't exist, or...
        if (us == null) return;
        // ... if we're turned off, IFF we don't have a kick to complete, or...
        if (!this.isActive()) return;

        // For now, just spin if active
        doAction(100);
    }
}
