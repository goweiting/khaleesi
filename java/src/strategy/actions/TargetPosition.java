package strategy.actions;

import PolarCoordNavigation.Coordinates.CartesianCoordinate;
import strategy.Strategy;

public class HoldPosition extends ActionBase {
    private CartesianCoordinate targetPosition;

    public HoldPosition(CartesianCoordinate target) {
        targetPosition = target;
        this.rawDescription =
                "Holding position [X " + targetPosition.getX() + ";" + "Y " + targetPosition.getY() + "]";
    }

    @Override
    public void onStart() {
        // Set target position as the origin point of everything
        Strategy.currentRobotBase.MOTION_CONTROLLER.setActive(true);
        Strategy.currentRobotBase.drive.setCurrentLookTarget(targetPosition);
    }

    @Override
    public void update() {}
}
