package PolarCoordNavigation;

import PolarCoordNavigation.Coordinates.CartesianCoordinate;
import PolarCoordNavigation.Coordinates.PolarCoordinate;

/**
 * Created by levif on 09/03/17.
 *
 * <p>This class represents the state of some polar coordinate world and an object within it
 */
public class PolarNavigationState {

  private CartesianCoordinate origin;
  private PolarCoordinate object;

  public PolarNavigationState() {
    this.origin = new CartesianCoordinate();
    this.object = new PolarCoordinate();
  }

  public void UpdateState(CartesianCoordinate origin, PolarCoordinate object) {
    if (origin != null) {
      this.origin = origin;
    }

    if (object != null) {
      this.object = object;
    }
  }

  public PolarCoordinate getObject() {
    return this.object;
  }

  public CartesianCoordinate getOrigin() {
    return this.origin;
  }

  @Override
  public String toString() {
    return this.object.toString();
  }
}
