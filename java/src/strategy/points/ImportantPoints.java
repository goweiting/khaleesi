package strategy.points;

import PolarCoordNavigation.Coordinates.CartesianCoordinate;
import PolarCoordNavigation.Coordinates.PolarCoordinate;
import strategy.Strategy;
import vision.Ball;
import vision.Robot;
import vision.RobotType;
import vision.constants.Constants;

/** Created by levif on 14/03/17. */
public class ImportantPoints {

  /**
   * TODO: important note --- one possibility would be to have a global variable called origin in
   * which ALL points returned correspond to this origin. Kind of like that idea so I am going to do
   * it.
   */
  private static CartesianCoordinate origin = getEnemyGoalCartesian();

  public static CartesianCoordinate getOrigin() {
    return ImportantPoints.origin;
  }

  public static void setOrigin(CartesianCoordinate origin) {
    ImportantPoints.origin = origin;
  }

  /**
   * Ball positions
   *
   * @return
   */
  public static CartesianCoordinate getBallCartesian() {
    Ball ball = Strategy.world.getBall();
    if (ball != null) {
      return new CartesianCoordinate((float) ball.location.x, (float) ball.location.y);
    } else {
      RobotType probableHolder = Strategy.world.getProbableBallHolder();
      if (probableHolder != null) {
        Robot p = Strategy.world.getRobot(probableHolder);
        if (p != null) {
          return new CartesianCoordinate((float) p.location.x, (float) p.location.y);
        }
      }
    }

    //WTF is this shit we need to not return NULL and return a probable
    // position. It's only here because last codebase was ass
    //TODO: we should return the last known position
    return new CartesianCoordinate(-1, -1);
  }

  public static PolarCoordinate getBallPolar() {
    return PolarCoordinate.CartesianToPolar(getBallCartesian(), ImportantPoints.origin);
  }

  /**
   * Enemy Goal Positions
   *
   * @return
   */
  public static CartesianCoordinate getEnemyGoalCartesian() {
    return new CartesianCoordinate(Constants.PITCH_WIDTH / 2, 0);
  }

  public static PolarCoordinate getEnemyGoalPolar() {
    return PolarCoordinate.CartesianToPolar(getEnemyGoalCartesian(), ImportantPoints.origin);
  }

  public static CartesianCoordinate getRobotCartesian(RobotType type) {
    Robot friend = Strategy.world.getRobot(type);

    //TODO: we should return the last known position
    if (friend == null) return new CartesianCoordinate(-1, -1);

    return new CartesianCoordinate((float) friend.location.x, (float) friend.location.y);
  }

  public static PolarCoordinate getRobotPolar(RobotType type) {
    return PolarCoordinate.CartesianToPolar(getRobotCartesian(type), origin);
  }
}
