package communication.ports.interfaces;

/**
 * Created by s1452923 on 31/01/17.
 */
public interface DribblerKickerEquippedRobotPort {

    public void updateDribbler(double dribblerPower);

    public void updateKicker(double kickerPower);

    public void dribblerKicker(double dribbler, double kicker);
}
