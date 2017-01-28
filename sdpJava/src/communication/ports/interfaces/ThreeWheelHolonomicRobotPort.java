package communication.ports.interfaces;

/**
 * Created by Simon Rovder
 * Edited by Wildfire
 */
public interface ThreeWheelHolonomicRobotPort {
    void threeWheelHolonomicMotion(double frontLeft, double frontRight, double back);
}
