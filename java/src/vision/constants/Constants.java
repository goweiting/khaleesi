package vision.constants;

import vision.rawInput.RawInput;

/** Created by Simon Rovder SDP2017NOTE Edit these if the camera or pitch change. */
public class Constants {
  public static final int INPUT_WIDTH = 640; // In pixels
  public static final int INPUT_HEIGHT = 480; // In pixels
  public static final int PITCH_WIDTH = 300; // In centimetres
  public static final int PITCH_HEIGHT = 220; // In centimetres
  // I don't remember what the following two do...
  public static boolean GUI = true;
  public static boolean TIMER = false;
  //	public static final int PREVIEW_FACTOR = 2;

  public static String settingsFilePath(int region) {
    if (region < 0 || region > 14) return "";
    String roomID = (RawInput.IS_IN_ROOM_1) ? "room1" : "room2";
    return System.getProperty("user.dir") + "/../vision_settings/" + roomID + "/" + Integer.toString(region);
  }
}
