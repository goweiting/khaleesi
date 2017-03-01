package strategy.actions.offense;

import strategy.Strategy;
import strategy.actions.ActionBase;
import strategy.points.basicPoints.BallPoint;
import strategy.points.basicPoints.RobotPoint;
import strategy.robots.Khaleesi;
import vision.RobotType;

/** Created by Rado Kirilchev on 21/02/2017. */
// Pass ball to friend.
public class PassingKick extends ActionBase {
  private boolean adjustingBeforeShot = false;
  private long nextStateChangeTime = -1;
  public PassingKick() {
    this.rawDescription = "Passing Kick";
  }

  @Override
  public void onStart() {
    Khaleesi us = (Khaleesi) Strategy.currentRobotBase;

    us.MOTION_CONTROLLER.setActive(true);
    us.MOTION_CONTROLLER.setHeading(new BallPoint());
    us.MOTION_CONTROLLER.setDestination(new BallPoint());

    us.KICKER_CONTROLLER.setKickerHoldDuration(2500);
    us.KICKER_CONTROLLER.setAutoShutdownAfterKick(true);

    super.onStart();
  }

  @Override
  public void update() {
    // Only update if necessary
    if (System.currentTimeMillis() < nextStateChangeTime) return;

    // Consider refactoring this, as it's currently a copy of Offensive Kick.
    // Replacing them with a generic Kick that takes a point should probably be good.

    Khaleesi us = (Khaleesi) Strategy.currentRobotBase;
    // Check if we've got the ball.
    boolean weGotBalls = Strategy.world.getProbableBallHolder() == RobotType.FRIEND_2;
    // If we do, try shooting at the opposing goal
    if (weGotBalls) {
      // We need to rotate towards the goal first. This takes some time.
      if (!adjustingBeforeShot) {
        nextStateChangeTime = System.currentTimeMillis() + 1250;
        us.MOTION_CONTROLLER.setHeading(new RobotPoint(RobotType.FRIEND_1));
        adjustingBeforeShot = true;
      }
      // When we're ready - and hopefully looking at our friend, kick.
      else {
        adjustingBeforeShot = false;
        us.KICKER_CONTROLLER.setActive(true);
      }
    }
    // If we don't have the ball, try grabbing it
    else {
      if (Strategy.curVisionRobot.location.distance(Strategy.world.getBall().location) < 10) {
        us.KICKER_CONTROLLER.setActive(true);
      }
    }
  }

  @Override
  public void onEnd() {
    // Disable kicker.
    ((Khaleesi) Strategy.currentRobotBase).KICKER_CONTROLLER.setActive(false);
  }
}
