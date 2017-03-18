package strategy.actions;

import strategy.Strategy;
import strategy.points.ImportantPoints;

/** Created by Simon Rovder */
public class OffensiveAction extends ActionBase {

    public OffensiveAction() {
        this.rawDescription = "Offensive";
    }

    @Override
    public void onStart() {
        Strategy.currentRobotBase.MOTION_CONTROLLER.setActive(true);
        Strategy.currentRobotBase.drive.setCurrentLookTarget(ImportantPoints.getEnemyGoalCartesian());

        super.onStart();
    }

    @Override
    public void update() {}
}
