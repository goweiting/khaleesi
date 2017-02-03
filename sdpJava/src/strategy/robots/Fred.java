package strategy.robots;

import communication.ports.robotPorts.FredRobotPort;
import strategy.controllers.fred.DribblerController;
import strategy.controllers.fred.KickerController;
import strategy.controllers.fred.PropellerController;
import strategy.drives.ThreeWheelHolonomicDrive;
import vision.RobotType;

/**
 * Created by Simon Rovder
 */
public class Fred extends RobotBase {

    public final PropellerController PROPELLER_CONTROLLER = new PropellerController(this);

    public final DribblerController DRIBBLER_CONTROLLER = new DribblerController(this);
    public final KickerController KICKER_CONTROLLER = new KickerController(this);

    public Fred(RobotType robotType) {
        super(robotType, new FredRobotPort(), new ThreeWheelHolonomicDrive());
        //this.controllers.add(this.PROPELLER_CONTROLLER);
        this.controllers.add(this.DRIBBLER_CONTROLLER);
        this.controllers.add(this.KICKER_CONTROLLER);
    }


    @Override
    public void performManual() {

    }
}
