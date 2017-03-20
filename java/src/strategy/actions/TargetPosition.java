package strategy.actions;

import PolarCoordNavigation.Coordinates.CartesianCoordinate;
import strategy.Strategy;

public class TargetPosition extends ActionBase {
    private CartesianCoordinate targetPosition;

    public TargetPosition(CartesianCoordinate target) {
        targetPosition = target;
        this.rawDescription =
                "Targeting position [X " + targetPosition.getX() + ";" + "Y " + targetPosition.getY() + "]";
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
