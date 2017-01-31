package strategy.robots;

import strategy.controllers.fred.DribblerKickerController;
import strategy.controllers.fred.PropellerController;
import strategy.drives.ThreeWheelHolonomicDrive;
import communication.ports.robotPorts.FredRobotPort;
import vision.RobotType;

/**
 * Created by Simon Rovder
 */
public class Fred extends RobotBase {

    public final PropellerController PROPELLER_CONTROLLER = new PropellerController(this);

    public final DribblerKickerController DK_CONTROLLER = new DribblerKickerController(this);

    public Fred(RobotType robotType){
        super(robotType, new FredRobotPort(), new ThreeWheelHolonomicDrive());
        this.controllers.add(this.PROPELLER_CONTROLLER); // TODO: Decide on this line. Remove it?
        this.controllers.add(this.DK_CONTROLLER);
    }


    @Override
    public void performManual() {

    }
}
