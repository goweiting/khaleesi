package communication.ports.interfaces;

/** Created by levif */
public interface SpinnerKickRobotPort {
  /**
   * @param spin amount/direction to spin
   * @param engage whether the spinner is down on the ball "engaged" or up "disengaged"
   */
  public void spinnerKick(int spin, int engage);
}
