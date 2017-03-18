package PolarCoordNavigation.DimensionNavigation;

/** Created by levif on 09/03/17. */
public class AngleDimensionNavigation extends OneDimensionNavigation {

  private static final double POWER_CONST_ANGLE = 60.0;
  private static final double SIZE_OF_ROBOT_IN_PIXELS = 20.0;

  @Override
  protected double PowerProcessing(double dist) {
    return Math.max(POWER_CONST_ANGLE, POWER_CONST_ANGLE * (dist / Math.PI));
  }

  @Override
  public double[] Drive4WheelToTarget(double current, double target, double... extraConsts) {
    double powerDirection = DirectionalPowerToTarget(current, target);

    //front wheel runs slower relative to the radius
    double wheelPowerFront =
        (extraConsts[0] - SIZE_OF_ROBOT_IN_PIXELS / 2)
            / (extraConsts[0] + SIZE_OF_ROBOT_IN_PIXELS / 2)
            * powerDirection;
    //back wheel runs faster relative to the radius
    double wheelPowerBack =
        (extraConsts[0] + SIZE_OF_ROBOT_IN_PIXELS / 2)
            / (extraConsts[0] - SIZE_OF_ROBOT_IN_PIXELS / 2)
            * powerDirection;
    double[] powerVec = {-wheelPowerFront, wheelPowerBack, 0.0, 0.0};

    return powerVec;
  }
}
