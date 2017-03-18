package PolarCoordNavigation.Coordinates;

/** Created by levif on 09/03/17. */
public class CartesianCoordinate extends Vector2 {

  //----constructs------

  public CartesianCoordinate() {
    super();
  }

  public CartesianCoordinate(float x, float y) {
    super(x, y);
  }

  //-----get/sets-----
  public float getX() {
    return super.getV1();
  }

  public void setX(float v) {
    this.setV1(v);
  }

  public float getY() {
    return super.getV2();
  }

  public void setY(float v) {
    this.setV2(v);
  }

  //-----static methods------

  //TODO: polar to cartesian static method
}
