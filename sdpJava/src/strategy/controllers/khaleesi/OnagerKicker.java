package strategy.controllers.khaleesi;

import communication.ports.interfaces.KickerEquippedRobotPort;
import strategy.Strategy;
import strategy.controllers.ControllerBase;
import strategy.robots.RobotBase;
import vision.Robot;

/** Created by Rado Kirilchev on 31/01/17. */
// It now also works as a grabber, and stays up by default, so modifications have been made.
public class OnagerKicker extends ControllerBase {
  // Implementing the Onager™ Reinforced Propelling System

  //===== Power settings.
  // NOTE TO SELF: POSITIVE POWER IS UP, NEGATIVE IS DOWN, at least it should be
  private static final boolean POSITIVE_POWER_IS_UP = true;

  // Maximum descending power. More => faster "grabbing".
  private static final int MAX_KICKER_DESCEND_POWER = 100;
  // Power to use to keep the kicker down - less than the descending one
  private static final int KICKER_HOLD_POWER = 100;
  // Maximum ascending power. More => more powerful kick, but we can't avoid slamming the kicker in the robot
  private static final int MAX_KICKER_ASCEND_POWER = 100;

  //===== Operation settings.
  // Grab duration: How long to keep the ball grabbed for (in msec).
  // Negative values will return the kicker to manual mode.
  // (Note that receiving a 'shoot()' call will instantly release the ball even on auto mode)
  private static final int KICKER_DEFAULT_HOLD_DURATION = 2500;
  // However, holding the ball forever will probably be against the rules.
  // Therefore, we need a fail-safe.
  private static final int KICKER_MANUAL_MAX_HOLD_DURATION =
      2500; //10000; // hack to prevent long stalling
  // Assumed duration of the movements. Please adjust as necessary - these will depend on the power.
  private static final int KICKER_ASCEND_DURATION = 750;
  private static final int KICKER_DESCEND_DURATION = 750;
  // Whether to execute a single kick and stop, or try kicking multiple times.
  private boolean autoShutdownAfterKick = true; // Just once by default

  // Internal vars
  private boolean shutDownAfterKick = true;
  private boolean shootOrderReceived = false;
  private int kickerHoldDuration = KICKER_DEFAULT_HOLD_DURATION;
  private KickerStatus kickerStatus = KickerStatus.OFF;
  // We need some way to track the time, in order to change states properly
  private long nextStateChangeTime = 0;

  private int lastKickerAction = 0;

  public OnagerKicker(RobotBase robot) {
    super(robot);
    this.kickerStatus = KickerStatus.OFF;
  }

  // Ideally, we SHOULDN'T attempt to kick whilst already kicking, and OFF is the only state where kicking should be OK
  public boolean isKickInProgress() {
    return kickerStatus != KickerStatus.OFF;
  }

  public boolean isInManualMode() {
    return kickerHoldDuration < 0;
  }

  @Override
  public void setActive(boolean active) {
    // We need to allow the current action to complete itself. Killing the motors
    // whilst still mid-kick would probably be a terrible idea.
    // (I.e. we need to release the ball if we're holding it, no matter what)
    // Ergo, we must delay the deactivation process until the kick has completed.

    if (active) {
      super.setActive(true);
      return;
    }

    // If cancelling, we need to think a little.
    // If in manual mode, always obey cancellation orders.
    if (isInManualMode()) {
      if (isKickInProgress()) shoot();
      else super.setActive(false);
    } else {
      if (isKickInProgress()) shutDownAfterKick = true;
      else super.setActive(false);
    }
  }

  // (Why are we using doubles?)
  // By default, we assume that positive power is upwards, and negative is downwards.
  private void doAction(int kickerPower) {
    // Clamp power to max (and min) values
    kickerPower = (kickerPower > MAX_KICKER_ASCEND_POWER) ? MAX_KICKER_ASCEND_POWER : kickerPower;
    kickerPower =
        (kickerPower < -MAX_KICKER_DESCEND_POWER) ? -MAX_KICKER_DESCEND_POWER : kickerPower;

    // Make sure our signs are correct. FLIP THE FLAG IF YOU NEED TO, DON'T TOUCH THIS CODE!
    if (!POSITIVE_POWER_IS_UP) kickerPower *= -1;

    // Send command
    ((KickerEquippedRobotPort) this.robot.port).updateKicker(kickerPower);
  }

  // If we're holding down (hopefully with a ball inside the grabber), kick it ASAP.
  public void shoot() {
    shootOrderReceived = true;
    nextStateChangeTime = System.currentTimeMillis() - 1; // Update ASAP
  }

  @Override
  // This (allegedly) gets called every update cycle. Track state changes in here?
  public void perform() {
    assert (this.robot.port instanceof KickerEquippedRobotPort);
    Robot us = Strategy.world.getRobot(this.robot.robotType);
    // Abort if we don't exist, or...
    if (us == null) return;
    // ... if we're turned off, IFF we don't have a kick to complete, or...
    if (isKickInProgress()) doAction(lastKickerAction);
    if (!this.isActive() && !isKickInProgress()) return;
    // ... if a change isn't due yet
    long currentTime = System.currentTimeMillis();
    if (currentTime < nextStateChangeTime) return;

    // If we've received an order to discharge the ball, do so.
    if (shootOrderReceived && isKickInProgress()) {
      // Instead of duplicating code, we just hook ourselves into the releasing subroutine.
      shootOrderReceived = false;
      kickerStatus = KickerStatus.HOLDING;
    }

    // Apparently, we're seeing issues where the kicker commands don't register.
    // I'm attributing this to UDP packet loss, which means we'll have to resend stuff.
    // Ergo, instead of calling 'doAction()' directly, set 'lastKickerAction'

    // This will continue working even if we're inactive, in case a kick is in progress.
    switch (kickerStatus) { // could've done it using methods inside an interface inside the enum... awwww yeaaaah
        // Nothing yet, we want to kick
      case OFF:
        kickerStatus = KickerStatus.DESCENDING;
        nextStateChangeTime = currentTime + KICKER_DESCEND_DURATION;
        lastKickerAction = -100;
        break;
        // Descending finished, now hold. We still need to apply power to resist the rubber band
      case DESCENDING:
        kickerStatus = KickerStatus.HOLDING;
        nextStateChangeTime =
            currentTime
                + ((isInManualMode()) ? KICKER_MANUAL_MAX_HOLD_DURATION : kickerHoldDuration);
        lastKickerAction = -KICKER_HOLD_POWER;
        break;
        // We're done with the holding, the ball needs to be released now.
      case HOLDING:
        kickerStatus = KickerStatus.ASCENDING;
        nextStateChangeTime = currentTime + KICKER_ASCEND_DURATION;
        lastKickerAction = 100;
        break;
        // And once we're done with moving upwards, turn motors off.
      case ASCENDING:
        kickerStatus = KickerStatus.OFF;
        nextStateChangeTime = currentTime + 5; // Just to be safe, I guess
        lastKickerAction = 0;
        // If we received a deactivation command whilst kicking, or we want to kick just once, shut down.
        if (shutDownAfterKick || autoShutdownAfterKick) {
          setActive(false);
          shutDownAfterKick = false;
        }
        break;
    }

    doAction(lastKickerAction);
  }

  // Toggle between infinite kicking and just one kick with greater ease
  public void setAutoShutdownAfterKick(boolean state) {
    autoShutdownAfterKick = state;
  }

  public boolean willAutoShutdownAfterKick() {
    return autoShutdownAfterKick;
  }

  // Allow toggling "manual mode" and run-time delay tweaking.
  public int getKickerHoldDuration() {
    return kickerHoldDuration;
  }

  // Call with a negative value to set "manual mode". Call with 0 to reset to default.
  public void setKickerHoldDuration(int kickerHoldDuration) {
    if (kickerHoldDuration == 0) kickerHoldDuration = KICKER_DEFAULT_HOLD_DURATION;
    this.kickerHoldDuration = kickerHoldDuration;
  }
  private enum KickerStatus {
    OFF, // Motors off, kicker in highest position.
    DESCENDING, // Motors on, kicker moving downwards.
    HOLDING, // Motors on limited power, kicker at lowest position.
    ASCENDING // Motors on, kicker moving upwards.

    // The lifetime of a kick:
    // Off -> Descending -> Holding -> Ascending -> Off
  }
}
