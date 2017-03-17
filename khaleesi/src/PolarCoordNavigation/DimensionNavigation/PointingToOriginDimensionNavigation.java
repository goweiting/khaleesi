package PolarCoordNavigation.DimensionNavigation;

/** Created by levif on 09/03/17. */
public class PointingToOriginDimensionNavigation extends OneDimensionNavigation {

  private static final double POWER_CONST_TO_ORIGIN = 20.0;

  @Override
  protected double DirectionalisePower(double power, double polarity) {
    return polarity < 0 ? -power : power;
  }

  @Override
  protected double DistToTarget(double current, double target) {
    return target - current;
  }

  @Override
  protected double PowerProcessing(double dist) {
    if (Math.abs(dist) < Math.PI / 10.0) return 0;

    double k = POWER_CONST_TO_ORIGIN / Math.PI;
    return Math.max(POWER_CONST_TO_ORIGIN, Math.abs(dist) * k);
  }

  @Override
  public double[] Drive4WheelToTarget(double current, double target, double... extraConsts) {
    double powerDirection = DirectionalPowerToTarget(current, target);

    double[] powerVec = {powerDirection, powerDirection, powerDirection, powerDirection};

    return powerVec;
  }
}
