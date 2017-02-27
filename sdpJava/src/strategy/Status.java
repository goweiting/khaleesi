package strategy;

import vision.Ball;
import vision.DynamicWorld;
import vision.RobotType;

/**
 * Created by Simon Rovder
 */
// RK: I might've found a use for this class...
public class Status {
    public final BallState ballState;

    public Status(DynamicWorld world) {
        Ball ball = world.getBall();
        RobotType prob = world.getProbableBallHolder();

        if (prob != null) {
            switch (prob) {
                case FRIEND_1:
                    this.ballState = BallState.FRIEND;
                    break;
                case FRIEND_2:
                    this.ballState = BallState.ME;
                    break;
                case FOE_1:
                    this.ballState = BallState.THEM;
                    break;
                case FOE_2:
                    this.ballState = BallState.THEM;
                    break;
                default:
                    this.ballState = BallState.LOST;
                    break;
            }
        } else {
            if (ball != null) {
                this.ballState = BallState.FREE;
            } else {
                this.ballState = BallState.LOST;
            }
        }
    }

    public enum BallState {
        ME, FRIEND, THEM, FREE, LOST
    }
}
