package vision.colorAnalysis;

import java.awt.*;
import java.util.HashMap;

/** Created by Simon Rovder, modified by Rado Kirilchev */
public class SDPColors {

  public static final SDPColors sdpColors = new SDPColors();

  public static HashMap<SDPColor, SDPColorInstance> colors;

  private SDPColors() {
    colors = new HashMap<SDPColor, SDPColorInstance>();
    for (SDPColor c : SDPColor.values()) {
      colors.put(c, new SDPColorInstance(c.toString(), -1, new Color(255, 0, 0), c));
    }
  }
}
