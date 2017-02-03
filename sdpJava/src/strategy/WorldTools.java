package strategy;

import vision.constants.Constants;
import vision.tools.VectorGeometry;

/**
 * Created by Simon Rovder
 */
public class WorldTools {
    private static int YFACTOR = 1;

    public static boolean isPointInFriendDefenceArea(VectorGeometry point) {
        double halfWidth = Constants.PITCH_WIDTH / 2;
        return (point.x < -halfWidth + 60) && point.y < 60 && point.y > -60;
    }

    public static boolean isPointInEnemyDefenceArea(VectorGeometry point) {
        double halfWidth = Constants.PITCH_WIDTH / 2;
        return (point.x > halfWidth - 60) && point.y < 60 && point.y > -60;
    }


}
