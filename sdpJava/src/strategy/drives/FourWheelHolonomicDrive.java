package strategy.drives;

import communication.ports.interfaces.FourWheelHolonomicRobotPort;
import communication.ports.interfaces.RobotPort;
import vision.tools.DirectedPoint;
import vision.tools.VectorGeometry;

/**
 * Created by Simon Rovder
 */

public class FourWheelHolonomicDrive implements DriveInterface{

    public int MAX_ROTATION = 30;
    public int MAX_MOTION = 200;

    public void move(RobotPort port, DirectedPoint location, VectorGeometry force, double rotation, double factor){
        assert(port instanceof FourWheelHolonomicRobotPort);

        VectorGeometry dir = new VectorGeometry();
        force.copyInto(dir).coordinateRotation(force.angle() - location.direction);
        factor = Math.min(1, factor);

        double lim = this.MAX_MOTION - Math.abs(rotation* this.MAX_ROTATION *factor);

        double frontLeft = dir.y;
        double back = -dir.x;
        double frontRight = -dir.y;
        double right = dir.x;
        double normalizer = Math.max(Math.max(Math.abs(back), Math.abs(right)), Math.max(Math.abs(frontLeft), Math.abs(frontRight)));

        normalizer = lim/normalizer*factor;
        frontLeft = frontLeft*normalizer + rotation * this.MAX_ROTATION;
        frontRight  = frontRight*normalizer + rotation * this.MAX_ROTATION;
        back  = back*normalizer + rotation * this.MAX_ROTATION;
        right = right*normalizer + rotation * this.MAX_ROTATION;

        ((FourWheelHolonomicRobotPort) port).fourWheelHolonomicMotion(frontLeft, frontRight, back);

    }
}
