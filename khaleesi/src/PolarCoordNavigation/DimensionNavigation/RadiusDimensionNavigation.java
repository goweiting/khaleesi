package PolarCoordNavigation.DimensionNavigation;

/** Created by levif on 09/03/17. */
public class RadiusDimensionNavigation extends OneDimensionNavigation {

  private static final double POWER_CONST_RADIUS = 60.0;

  @Override
  protected double PowerProcessing(double dist) {
    return Math.max(
        POWER_CONST_RADIUS, POWER_CONST_RADIUS * (Math.abs(dist) / (POWER_CONST_RADIUS * 2)));
  }

  @Override
  public double[] Drive4WheelToTarget(double current, double target, double... extraConsts) {
    double powerDirection = this.DirectionalPowerToTarget(current, target);

    double[] powerVec = {0, 0, powerDirection, -powerDirection};

    return powerVec;
  }
}
