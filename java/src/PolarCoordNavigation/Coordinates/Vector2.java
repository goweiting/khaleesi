package PolarCoordNavigation.Coordinates;

/** Created by levif on 09/03/17. */
public class Vector2 {

  protected float v1;
  protected float v2;

  //----constructs------

  public Vector2() {
    this.v1 = 0;
    this.v2 = 0;
  }

  public Vector2(float v1, float v2) {
    this.v1 = v1;
    this.v2 = v2;
  }

  public static Vector2 Subtract(Vector2 pc1, Vector2 pc2) {
    //two classes must be subtracted that are the same
    if (pc1.getClass() != pc2.getClass()) return null;

    return new Vector2(pc1.getV1() - pc2.getV1(), pc1.getV2() - pc2.getV2());
  }

  //-----get/sets-----
  protected float getV1() {
    return this.v1;
  }

  protected void setV1(float v) {
    this.v1 = v;
  }

  protected float getV2() {
    return this.v2;
  }

  protected void setV2(float v) {
    this.v2 = v;
  }

  //-----static methods------

  @Override
  public String toString() {
    return "(" + this.v1 + ", " + this.v2 + ")";
  }
}
