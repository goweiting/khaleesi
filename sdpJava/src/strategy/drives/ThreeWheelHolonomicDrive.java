package strategy.drives;

import communication.ports.interfaces.ThreeWheelHolonomicRobotPort;
import communication.ports.interfaces.RobotPort;
import vision.tools.DirectedPoint;
import vision.tools.VectorGeometry;

/**
 * Created by Simon Rovder
 * Edited by Wildfire
 */

public class ThreeWheelHolonomicDrive implements DriveInterface{

    public int MAX_ROTATION = 30;
    public int MAX_MOTION = 200;

    public void move(RobotPort port, DirectedPoint location, VectorGeometry force, double rotation, double factor){
        assert(port instanceof ThreeWheelHolonomicRobotPort);


        /**
         *
         * I believe this is where we implement PID controller
         *
         */

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
        // right motor is no longer user (as we have only 3) thus all logic has to be either replaced
        // or modified, but I just don't get it yet
        right = right*normalizer + rotation * this.MAX_ROTATION;

        ((ThreeWheelHolonomicRobotPort) port).threeWheelHolonomicMotion(frontLeft, frontRight, back);

    }
}
