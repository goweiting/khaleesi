package communication.ports.interfaces;

/**
 * Created by Simon Rovder
 */
public interface FourWheelHolonomicRobotPort {
    void fourWheelHolonomicMotion(double frontLeft, double frontRight, double back);
}
