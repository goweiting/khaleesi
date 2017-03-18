package vision;

import vision.tools.DirectedPoint;

/** Created by Simon Rovder */
public final class Robot {
  public DirectedPoint location;
  public DirectedPoint velocity;
  public RobotType type;
  public RobotAlias alias;
  private boolean hasBall;

  public Robot() {}

  @Override
  public Robot clone() {
    Robot r = new Robot();
    r.location = this.location.clone();
    r.velocity = this.velocity.clone();
    r.type = this.type;
    r.hasBall = this.hasBall;
    return r;
  }

  public boolean getHasBall() {
    return this.hasBall;
  }

  public void setHasBall(boolean hasBall) {
    this.hasBall = hasBall;
  }
}
