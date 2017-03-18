package PolarCoordNavigation.Coordinates;

/** Created by levif on 09/03/17. */
public class PolarCoordinate extends Vector2 {

  //----constructs------

  public PolarCoordinate() {
    super();
  }

  public PolarCoordinate(float radius, float angle) {
    super(radius, angle);
  }

  public static PolarCoordinate CartesianToPolar(
      CartesianCoordinate position, CartesianCoordinate origin) {
    float diffX = origin.getX() - position.getX();
    float diffY = origin.getY() - position.getY();
    float radius = (float) Math.sqrt(diffX * diffX + diffY * diffY);
    float angle = (float) ((Math.PI * 2.5 + Math.atan2(diffY, diffX)) % (Math.PI * 2));

    return new PolarCoordinate(radius, angle);
  }

  //-----get/sets-----
  public float getRadius() {
    return super.getV1();
  }

  public void setRadius(float v) {
    this.setV1(v);
  }

  public float getAngle() {
    return super.getV2();
  }

  //-----static methods------

  public void setAngle(float v) {
    this.setV2(v);
  }
}
