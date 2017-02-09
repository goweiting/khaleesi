package strategy.controllers.essentials;

import strategy.GUI;
import strategy.Strategy;
import strategy.controllers.ControllerBase;
import strategy.navigation.NavigationInterface;
import strategy.navigation.Obstacle;
import strategy.navigation.aStarNavigation.AStarNavigation;
import strategy.navigation.potentialFieldNavigation.PotentialFieldNavigation;
import strategy.points.DynamicPoint;
import strategy.robots.RobotBase;
import vision.Robot;
import vision.RobotType;
import vision.gui.SDPConsole;
import vision.tools.VectorGeometry;

import java.util.LinkedList;

/**
 * Created by Simon Rovder
 */
public class MotionController extends ControllerBase {

    public MotionMode mode;
    private DynamicPoint heading = null;
    private DynamicPoint destination = null;

    private int tolerance;

    private LinkedList<Obstacle> obstacles = new LinkedList<Obstacle>();

    public MotionController(RobotBase robot) {
        super(robot);
    }

    public void setMode(MotionMode mode) {
        this.mode = mode;
    }

    public void setTolerance(int tolerance) {
        this.tolerance = tolerance;
    }

    public void setDestination(DynamicPoint destination) {
        this.destination = destination;
    }

    public void setHeading(DynamicPoint dir) {
        this.heading = dir;
    }

    public void addObstacle(Obstacle obstacle) {
        this.obstacles.add(obstacle);
    }

    public void clearObstacles() {
        this.obstacles.clear();
    }

    public void perform() {
        if (this.mode == MotionMode.OFF) {
            return;
        }

        Robot us = Strategy.world.getRobot(RobotType.FRIEND_2);
        if (us == null) {
            // todo: For probabilistic inference add something here wrt to the last known location?
            return;
        }

        NavigationInterface navigation;

        // always starts with a blankstate:
        VectorGeometry heading = null;
        VectorGeometry destination = null;

        if (this.destination != null) {
            this.destination.recalculate();

            destination = new VectorGeometry(this.destination.getX(), this.destination.getY());

            // find if any of the obstacles OR robots intersects the planned path:
            boolean intersects = false;
            for (Obstacle o : this.obstacles) {
                intersects = intersects || o.intersects(us.location, destination);
            }

            for (Robot r : Strategy.world.getRobots()) {
                if (r != null && r.type != RobotType.FRIEND_2) {
                    intersects = intersects ||
                            VectorGeometry.vectorToClosestPointOnFiniteLine(us.location, destination, r.location)
                                    .minus(r.location).length() < 30;
                }
            }

            // Determines which navigation system to use:
            // use AStarNavigation if obstacle(s) imminent or destination is too far away;
            // otherwise potential field navigation is good enough.
            // todo: might want to just use any one of it first?
            if (intersects || us.location.distance(destination) > 30) {
                navigation = new AStarNavigation();
                GUI.gui.searchType.setText("A*");
            } else {
                navigation = new PotentialFieldNavigation();
                GUI.gui.searchType.setText("Potential Fields");
            }

            navigation.setDestination(new VectorGeometry(destination.x, destination.y));

        } else {
            // no destination set for robot
            return;
        }

        if (this.obstacles != null) {
            navigation.setObstacles(this.obstacles);
        }

        if (this.heading != null) {
            //SDPConsole.write("recalculating heading (" + this.heading.toString() + ")..."); // DEBUG
            this.heading.recalculate();
            heading = new VectorGeometry(this.heading.getX(), this.heading.getY());
            //SDPConsole.write("   -> " + heading.toString() + " ");
        } else {
            heading = VectorGeometry.fromAngular(us.location.direction, 10, null);
            //SDPConsole.write("finding heading (" + heading.toString() + ")..."); // DEBUG
        }

        // find the force required to get to the goal (this implementation is navigation algorithm
        // dependent!)
        VectorGeometry force = navigation.getForce();
        if (force == null) {
            this.robot.port.stop(); // halt the robot if there is no need to move!
            return;
        }

        // working out the amount of rotation required to face the desired heading
        VectorGeometry robotHeading = VectorGeometry.fromAngular(us.location.direction, 10, null);
        VectorGeometry robotToPoint = VectorGeometry.fromTo(us.location, heading);
        double rotation = VectorGeometry.signedAngle(robotToPoint, robotHeading);

        // Can throw null without check because null check takes SourceGroup into consideration.
        // Reduce the scaling if the distance is very close to us:
        // The P controller bit is here:
        double factor = 1;
        if (destination.distance(us.location) < 30) {
            factor = 0.7;
        }

        // If the robot is well within the distance from the destination then just stop! The desirable
        // distance is dependent on the Motion that robot suppose to execute (e.g. kicking)
        if (this.destination != null &&
                us.location.distance(destination) < tolerance) {
            this.robot.port.stop();
            return;
        }

        // strategy.navigationInterface.draw();

        // MOVE THE ROBOT:
        this.robot.drive.move(this.robot.port, us.location, force, rotation, factor);
    }

    public enum MotionMode {
        ON, OFF
    }
}
