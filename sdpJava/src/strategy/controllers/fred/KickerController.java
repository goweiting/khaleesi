package strategy.controllers.fred;

import communication.ports.interfaces.DribblerKickerEquippedRobotPort;
import strategy.Strategy;
import strategy.controllers.ControllerBase;
import strategy.robots.RobotBase;
import vision.Robot;

/**
 * Created by s1452923 on 31/01/17.
 */
public class KickerController extends ControllerBase {
    // Prevent kicker from slamming back into the robot by clamping the power
    private static final int MAX_KICKER_RETRACT_POWER = 70;
    // A couple of other constants
    // How long to pause once having kicked the ball
    private static final int KICKER_PEAK_PAUSE_MSEC = 500;
    // How long do we expect the kicking OR retracting actions to take
    private static final int KICKER_MOVE_DURATION_MSEC = 1000;
    private boolean shutDownAfterKick = false;
    private KickerStatus kickerStatus = KickerStatus.OFF;
    // We need some way to track the time, in order to retract the kicker after
    private long nextStateChangeTime = 0;

    public KickerController(RobotBase robot) {
        super(robot);
        this.kickerStatus = KickerStatus.OFF;
    }

    // Ideally, we SHOULDN'T attempt to kick whilst already kicking, and OFF is the only state where kicking should be OK
    public boolean isKickInProgress() {
        return kickerStatus != KickerStatus.OFF;
    }

    @Override
    public void setActive(boolean active) {
        // We need to allow the current action to complete itself. Killing the motors
        // whilst still mid-kick would probably be a terrible idea.
        // Ergo, we need to delay the deactivation process until the kick has completed.
        if (isKickInProgress()) {
            shutDownAfterKick = !active;
        } else super.setActive(active);
    }

    // (Why are we using doubles?)
    // Positive dribbler power means we're "sucking" the ball in;
    // Positive kicker power means we're actually kicking, i.e. propelling the ball outwards.
    private void doAction(double kickerPower) {
        // Clamp kicker retracting force if necessary
        kickerPower = (kickerPower < -MAX_KICKER_RETRACT_POWER) ? -MAX_KICKER_RETRACT_POWER : kickerPower;

        // Send command
        ((DribblerKickerEquippedRobotPort) this.robot.port).updateKicker(kickerPower);
    }

    @Override
    // This (allegedly) gets called every update cycle. Track state changes in here?
    public void perform() {
        assert (this.robot.port instanceof DribblerKickerEquippedRobotPort);
        Robot us = Strategy.world.getRobot(this.robot.robotType);
        // Abort if we don't exist, or...
        if (us == null) return;
        // ... if we're turned off, IFF we don't have a kick to complete, or...
        if (!this.isActive() && !isKickInProgress()) return;
        // ... if a change isn't due yet
        long currentTime = System.currentTimeMillis();
        if (currentTime < nextStateChangeTime) return;

        //return; // FIXME: remove once complete

        System.out.println("DEBUG: KICKER PERFORMING; Status = " + kickerStatus.toString());

        // This will continue working even if we're inactive, in case a kick is in progress.
        switch (kickerStatus) { // could've done it using methods inside an interface inside the enum.. .awwww yeaaaah
            // Nothing yet, we want to kick
            case OFF:
                kickerStatus = KickerStatus.KICKING;
                nextStateChangeTime = currentTime + KICKER_MOVE_DURATION_MSEC;
                doAction(100); // Perform kick
                break;
            // Kicking up, and we've been pushing up for a while now. Time to hold for a bit.
            case KICKING:
                kickerStatus = KickerStatus.PEAK_PAUSE;
                nextStateChangeTime = currentTime + KICKER_PEAK_PAUSE_MSEC;
                doAction(0); // Stop motors
                break;
            // We've been paused at the top of the motion for a while now. We should start going down
            case PEAK_PAUSE:
                kickerStatus = KickerStatus.RETRACTING;
                nextStateChangeTime = currentTime + KICKER_MOVE_DURATION_MSEC;
                doAction(-100); // Pull kicker back in
                break;
            // Retracting has taken place now. Restart motors and be prepared to kick again.
            case RETRACTING:
                kickerStatus = KickerStatus.OFF;
                nextStateChangeTime = currentTime + 5; // Just to be safe, I guess
                doAction(0);
                // If we received a deactivation command whilst kicking, shut down.
                if (shutDownAfterKick) {
                    setActive(false);
                    shutDownAfterKick = false;
                }
                break;
        }
    }

    private enum KickerStatus {
        OFF,        // Motors off, kicker in lowest position.
        KICKING,    // Motors on, kicker moving upwards.
        PEAK_PAUSE, // Motors off, kicker at peak position.
        RETRACTING  // Motors on, kicker moving downwards.

        // The lifetime of a kick:
        // Off -> Kicking -> Peak_Pause -> Retracting -> Off
    }
}
