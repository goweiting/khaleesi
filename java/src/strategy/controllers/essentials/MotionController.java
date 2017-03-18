package strategy.controllers.essentials;

import strategy.Strategy;
import strategy.controllers.ControllerBase;
import strategy.robots.RobotBase;
import vision.Robot;
import vision.RobotType;

/** Created by Simon Rovder */
public class MotionController extends ControllerBase {

  public MotionMode mode;

  public MotionController(RobotBase robot) {
    super(robot);
  }

  public void setMode(MotionMode mode) {
    this.mode = mode;
  }

  public void perform() {
    if (this.mode == MotionMode.OFF) return;

    Robot us = Strategy.world.getRobot(RobotType.FRIEND_2);
    if (us == null) return;

    this.robot.drive.move(this.robot.port);
  }

  public enum MotionMode {
    ON,
    OFF
  }
}
