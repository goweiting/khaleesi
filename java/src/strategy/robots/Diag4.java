package strategy.robots;

import communication.ports.robotPorts.Diag4RobotPort;
import strategy.drives.HorizVertSimpleDrive;
import vision.RobotType;

/** Created by levif */
public class Diag4 extends RobotBase {

  public Diag4(RobotType robotType) {
    super(robotType, new Diag4RobotPort(), new HorizVertSimpleDrive());
  }

  @Override
  public void performManual() {
    /*nothing here for now*/
  }
}
