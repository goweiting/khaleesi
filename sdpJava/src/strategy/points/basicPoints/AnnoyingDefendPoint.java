package strategy.points.basicPoints;

import strategy.Strategy;
import strategy.points.DynamicPointBase;
import vision.Ball;
import vision.Robot;
import vision.RobotType;
import vision.constants.Constants;
import vision.tools.VectorGeometry;

/**
 * Created by Simon Rovder
 */
public class AnnoyingDefendPoint extends DynamicPointBase {

    VectorGeometry lower = new VectorGeometry(-Constants.PITCH_WIDTH / 2 + 20, 20);
    VectorGeometry upper = new VectorGeometry(-Constants.PITCH_WIDTH / 2 + 20, -20);

    @Override
    public void recalculate() {
        Ball ball = Strategy.world.getBall();
        VectorGeometry closest = null;
        if (ball != null && ball.velocity.length() > 0.2) {
            closest = VectorGeometry.vectorToClosestPointOnFiniteLine(lower, upper, ball.location);
            this.x = (int) closest.x;
            this.y = (int) closest.y;
        } else {
            Robot foe1 = Strategy.world.getRobot(RobotType.FOE_1);
            if (foe1 != null) {
                closest = VectorGeometry.intersectionWithFiniteLine(foe1.location,
                        VectorGeometry.fromAngular(foe1.location.direction, 10, null), lower, upper);
            }
            Robot foe2 = Strategy.world.getRobot(RobotType.FOE_2);
            if (foe2 != null) {
                VectorGeometry foe2angular = VectorGeometry.fromAngular(foe2.location.direction, 10, null);
                VectorGeometry foe2diff = VectorGeometry.fromTo(foe2.location, new VectorGeometry(-Constants.PITCH_WIDTH, 0));
                VectorGeometry foe1angular = VectorGeometry.fromAngular(foe1.location.direction, 10, null);
                VectorGeometry foe1diff = VectorGeometry.fromTo(foe1.location, new VectorGeometry(-Constants.PITCH_WIDTH, 0));
                if (foe1 == null ||
                    VectorGeometry.angle(foe2angular, foe2diff) < VectorGeometry.angle(foe1angular, foe1diff)) {
                    closest = VectorGeometry.intersectionWithFiniteLine(foe2.location,
                            VectorGeometry.fromAngular(foe2.location.direction, 10, null), lower, upper);
                }
            }
            if (closest == null) {
                this.x = -Constants.PITCH_WIDTH / 2 + 20;
                this.y = 0;
            } else {
                this.x = (int) closest.x;
                this.y = (int) closest.y;
            }
        }

    }

    @Override
    public int getX() {
        return this.x;
    }

    @Override
    public int getY() {
        return this.y;
    }
}
