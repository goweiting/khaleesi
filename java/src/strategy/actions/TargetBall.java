package strategy.actions;

import strategy.Strategy;
import strategy.points.ImportantPoints;

/** Created by Simon Rovder */
public class TargetBall extends ActionBase {

    public TargetBall() {
        this.rawDescription = "Looking At Ball";
    }

    @Override
    public void onStart() {
        Strategy.currentRobotBase.MOTION_CONTROLLER.setActive(true);
        Strategy.currentRobotBase.drive.setCurrentLookTarget(ImportantPoints.getBallCartesian());
    }

    @Override
    public void update() {}
}
