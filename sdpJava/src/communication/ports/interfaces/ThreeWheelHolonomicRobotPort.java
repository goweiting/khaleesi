package communication.ports.interfaces;

/**
 * Created by Simon Rovder
 * Edited by Wildfire
 */
public interface ThreeWheelHolonomicRobotPort {
    void threeWheelHolonomicMotion(double frontRight, double frontLeft, double backWheel);
}
