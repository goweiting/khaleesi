package strategy.actions.offense;

import strategy.Strategy;
import strategy.actions.ActionBase;
import strategy.points.basicPoints.BallPoint;
import strategy.points.basicPoints.ReverseBallDirection;
import strategy.robots.Khaleesi;
import vision.tools.VectorGeometry;

/**
 * Created by Simon Rovder
 */
public class ShuntKick extends ActionBase {

    private VectorGeometry destination;


    public ShuntKick() {
        this.rawDescription = "Shunt Kick";
    }

    @Override
    public void update() {
        Khaleesi us = (Khaleesi)Strategy.currentRobotBase;
        // I don't know what these are supposed to do. They were just here.
        us.MOTION_CONTROLLER.setDestination(new BallPoint());
        us.MOTION_CONTROLLER.setHeading(new ReverseBallDirection());
        us.MOTION_CONTROLLER.setTolerance(-1);

        // Perhaps some actual kicking logic should be added?...
    }
}
