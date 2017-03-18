package strategy.drives;

import PolarCoordNavigation.Coordinates.CartesianCoordinate;
import communication.ports.interfaces.RobotPort;

/** Created by Simon Rovder */
public interface DriveInterface {

    /**
     * SDP2017NOTE If you wnt your robot to be able to move using out automatic NavigationInterface
     * System, you need to implement this method for it. We have a 4 wheel holonomic one already
     * implemented, you can copy the pattern from there.
     *
     * <p>This method will be called automatically by the system. Just implement it, instantiate it
     * and place the instance into your robot's 'drive' variable.
     *
     * @param port The sdpPort of the robot that is to be moved.
     */
    void move(RobotPort port);

    // Rado Kirilchev: And this
    void setCurrentLookTarget(CartesianCoordinate target);
}
