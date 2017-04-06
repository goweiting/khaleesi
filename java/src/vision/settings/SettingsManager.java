package vision.settings;

import vision.colorAnalysis.SDPColor;
import vision.colorAnalysis.SDPColorInstance;
import vision.colorAnalysis.SDPColors;
import vision.distortion.Distortion;
import vision.gui.MiscellaneousSettings;
import vision.gui.SDPConsole;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;

import static vision.colorAnalysis.SDPColor._BALL;

/**
 * Created by Simon Rovder
 * <p>
 * <p>SDP2017NOTE This class takes care of storing and loading settings. If you add any new features
 * that need calibration or take a long time to set up, edit this class to also save those
 * settings.
 */
public class SettingsManager {

    public static void saveSettings() throws Exception {
        String fileName = SDPConsole.chooseFile("SAVE SETTINGS");
        saveSettingsToFile(fileName);
    }

    public static void saveSettingsToFile(String fileName) throws Exception {
        if (fileName != null) {
            PrintWriter writer = new PrintWriter(fileName, "UTF-8");
            writer.write("^COLORS\n");
            for (SDPColor key : SDPColor.values()) {
                writer.write(key.toString() + "\r\n");
                writer.write(SDPColors.colors.get(key).saveSettings() + "\r\n");
            }
            writer.write("^MISC\r\n");
            writer.write(MiscellaneousSettings.miscSettings.saveSettings() + "\r\n");

            writer.write("^DISTORTION\r\n");
            writer.write(Distortion.distortion.saveSettings() + "\r\n");
            writer.write("^END");
            writer.close();
        }
    }

    public static void saveRegionSettingsToFile(String fileName) throws Exception {
        if (fileName != null) {
            PrintWriter writer = new PrintWriter(fileName, "UTF-8");
            writer.write("^COLORS\n");
            for (SDPColor key : SDPColor.values()) {
                writer.write(key.toString() + "\r\n");
                writer.write(SDPColors.colors.get(key).saveSettings() + "\r\n");
            }
            writer.close();
        }
    }

    public static void reloadSettings(String fileName, String ballPath, int ourRegion, int ballRegion)
            throws Exception {
        if (fileName == null) {
            return;
        }

        BufferedReader r = new BufferedReader(new FileReader(new File(fileName)));
        String next = r.readLine();
        while (!next.equals("^COLORS")) {
            next = r.readLine();
        }
        next = r.readLine();
        while (!next.equals("^MISC")) {
            SDPColorInstance col = SDPColors.colors.get(SDPColor.valueOf(next));
            col.loadSettings(r.readLine());
            col.setRegionInTitle(ourRegion);
            // if r has next line
            next = r.readLine();
            if (next == null) {
                break;
            }
        }
//    next = r.readLine();
//    while (!next.equals("^DISTORTION")) {
//      // MiscellaneousSettings.miscSettings.loadSettings(next);
//      next = r.readLine();
//    }
//    next = r.readLine();
//    // Distortion.distortion.loadSettings(next);
//    while (!next.equals("^END")) {
//      next = r.readLine();
//    }
        SDPColors.colors.get(_BALL).setRegionInTitle(ourRegion);
        r.close();

        if (!(ballPath != null && !ballPath.equals(""))) {
            return;
        }

        BufferedReader r2 = new BufferedReader(new FileReader(new File(ballPath)));
        next = r2.readLine();
        while (!next.contains("_BALL")) {
            next = r2.readLine();
        }
        String ball = r2.readLine();
        SDPColors.colors.get(_BALL).loadSettings(ball);
        r2.close();
    }

    public static void loadSettings(String fileName, String ballPath) throws Exception {
        if (fileName == null) {
            return;
        }

        BufferedReader r = new BufferedReader(new FileReader(new File(fileName)));
        String next = r.readLine();
        while (!next.equals("^COLORS")) {
            next = r.readLine();
        }
        next = r.readLine();
        while (!next.equals("^MISC")) {
            SDPColorInstance col = SDPColors.colors.get(SDPColor.valueOf(next));
            col.loadSettings(r.readLine());
            col.setRegionInTitle(-1);
            next = r.readLine();
        }
        next = r.readLine();
        while (!next.equals("^DISTORTION")) {
            MiscellaneousSettings.miscSettings.loadSettings(next);
            next = r.readLine();
        }
        next = r.readLine();
        Distortion.distortion.loadSettings(next);
        while (!next.equals("^END")) {
            next = r.readLine();
        }
        r.close();

        if (!(ballPath != null && !ballPath.equals(""))) {
            return;
        }

        BufferedReader r2 = new BufferedReader(new FileReader(new File(ballPath)));
        next = r2.readLine();
        while (!next.contains("_BALL")) {
            next = r2.readLine();
        }
        String ball = r2.readLine();
        System.out.println(ball);
        SDPColors.colors.get(_BALL).loadSettings(ball);
        SDPColors.colors.get(_BALL).setRegionInTitle(-1);
        r2.close();

    }

    public static void loadSettings(String filename2) throws Exception {
        String fileName = SDPConsole.chooseFile("LOAD SETTINGS");
        if (fileName != null) {
            loadSettings(fileName, filename2);
        }
    }

//  public static void loadBallSettings(String fileName) throws Exception {
//    if (fileName != null) {
//
//    }

//    ^COLORS
//            _BALL
//    0.9371795;1.0371795;0.7996732;1.0;0.55;1.0
//    PINK
//    0.9274011;1.0274011;0.56780106;1.0;0.6990196;1.0
//    YELLOW
//    0.06517615;0.16517615;0.5559113;1.0;0.74607843;1.0
//    BLUE
//    0.5;0.75;0.41;1.0;0.0;1.0
//    GREEN
//    0.21715684;0.31715685;0.73160917;1.0;0.29117647;1.0
//            ^MISC
//    false;false;false;false;false;UNKNOWN;UNKNOWN;UNKNOWN;UNKNOWN
//            ^DISTORTION
//    0;0;0;0;0;0;0;10:10;630:470
//            ^END

}



