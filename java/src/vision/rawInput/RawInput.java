package vision.rawInput;

import vision.RobotType;
import vision.constants.Constants;
import vision.distortion.Distortion;
import vision.settings.SettingsManager;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.LinkedList;

import static strategy.Strategy.world;

/**
 * Created by Simon Rovder
 */
public class RawInput extends JPanel {

    public static final RawInput rawInputMultiplexer = new RawInput();
    public static boolean IS_IN_ROOM_1 = true;
    private static boolean debugMode = true; // GWT: turns the debug mode on and or off
    private static int CURRENT_REGION = -1;
    public BufferedImage lastImage;
    private ArrayList<PanelLocation> panelLocations = new ArrayList<>();
    private JTabbedPane tabbedPane;
    private AbstractRawInput[] rawInputs = {LiveCameraInput.liveCameraInput, StaticImage.staticImage};
    private LinkedList<RawInputListener> imageListeners;

    private RawInput() {
        super();
        this.setLayout(new BorderLayout(0, 0));

        this.tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        this.imageListeners = new LinkedList<RawInputListener>();

        panelLocations.add(0, new PanelLocation(100, 70));
        panelLocations.add(1, new PanelLocation(100, -70));
        panelLocations.add(2, new PanelLocation(0, 0));
        panelLocations.add(3, new PanelLocation(-100, 70));
        panelLocations.add(4, new PanelLocation(-100, -70));


        this.add(this.tabbedPane);

        for (AbstractRawInput rawInput : this.rawInputs) {
            rawInput.setInputListener(this);
            this.tabbedPane.addTab(rawInput.getTabName(), null, rawInput, null);
        }
    }

    public static int currentRegion() {
        return CURRENT_REGION;
    }

    public static void addRawInputListener(RawInputListener ril) {
        RawInput.rawInputMultiplexer.imageListeners.add(ril);
    }

    public void nextFrame(BufferedImage image, long time) {
        this.lastImage = image;
        try {
            CURRENT_REGION = closestPlateRegion(world.getRobot(RobotType.FRIEND_2).location.getX(),
                    world.getRobot(RobotType.FRIEND_2).location.getY());
            String path = Constants.settingsFilePath(CURRENT_REGION);

            int ballRegion = closestPlateRegion(world.getBall().location.getX(),
                    world.getBall().location.getY());
            String ballPath = Constants.settingsFilePath(ballRegion);

            if (debugMode) {
                //                System.out.println("            ROBOT PATH :" + path);
                //                System.out.println("BALL PATH :" + ballPath);
                System.out.println("ROBOT: " + Integer.toString(CURRENT_REGION) + " BALL: " + Integer.toString(ballRegion));
            }

            // Actively reload the settings for this current frame!
            SettingsManager.reloadSettings(path, ballPath, CURRENT_REGION, ballRegion);
            //      SettingsManager.loadBallSettings(ballPath);

        } catch (Exception x) {
            System.out.println(x.getMessage());
        }

        for (RawInputListener ril : this.imageListeners) {
            ril.nextFrame(image, time);
        }
    }

    public double distanceFrom(double r1, double r2, double theta1, double theta2) {
        return Math.sqrt(r1 * r1 + r2 * r2 - 2 * r1 * r2 * Math.cos(theta2 - theta1));
    }

    //CALCULATE POLAR DISTANCE

    public int closestPlateRegion(double x, double y) {

        double r1 = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
        double theta1 = Math.atan2(y, x);
        double min = 10000000;
        int min_index = -5;
        for (PanelLocation panel : panelLocations) {
            double dist = distanceFrom(r1, panel.getRadius(), theta1, panel.getTheta());
            if (dist < min) {
                min = dist;
                min_index = panelLocations.indexOf(panel);
            }
        }
        if (min_index >= 0 && min < 10000000) {
            if (Distortion.ROTATE_PITCH) {
                switch (min_index) {
                    case 0:
                        return 4;
                    case 1:
                        return 3;
                    case 2:
                        return 2;
                    case 3:
                        return 1;
                    case 4:
                        return 0;
                    default:
                        return min_index;
                }
            } else {
                return min_index;
            }
        }
        return -1;
    }

    //CALCULATE CLOSEST PLATE CALIBRATION

    public void stopAllInputs() {
        for (RawInputInterface input : this.rawInputs) {
            input.stop();
        }
    }

    public void setVideoChannel(int port) {
        ((LiveCameraInput) (this.rawInputs[0])).setVideoChannel(port);
    }

    public void streamVideo() {
        this.rawInputs[0].start();
    }

    private class PanelLocation {

        private double r1;
        private double theta;

        public PanelLocation(double x, double y) {
            double r1 = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
            double theta1 = Math.atan2(y, x);

            this.r1 = r1;
            this.theta = theta1;

        }

        public double getRadius() {
            return r1;
        }

        public double getTheta() {
            return theta;
        }

    }
}
