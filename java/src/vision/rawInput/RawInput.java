package vision.rawInput;

import communication.ports.interfaces.RobotPort;
import strategy.Strategy;
import vision.*;
import vision.constants.Constants;
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

    public static boolean IS_IN_ROOM_1 = true;
    private static int CURRENT_REGION = -1;

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

    public static int currentRegion() {
        return CURRENT_REGION;
    }

    private ArrayList<PanelLocation> panelLocations = new ArrayList<>();

    public static final RawInput rawInputMultiplexer = new RawInput();
    public BufferedImage lastImage;
    private JTabbedPane tabbedPane;
    private AbstractRawInput[] rawInputs = {LiveCameraInput.liveCameraInput, StaticImage.staticImage};
    private LinkedList<RawInputListener> imageListeners;

    private RawInput() {
        super();
        this.setLayout(new BorderLayout(0, 0));

        this.tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        this.imageListeners = new LinkedList<RawInputListener>();

        panelLocations.add(0, new PanelLocation(120, 80));
        panelLocations.add(1, new PanelLocation(130, 0));
        panelLocations.add(2, new PanelLocation(120, -80));
        panelLocations.add(3, new PanelLocation(70, 55));
        panelLocations.add(4, new PanelLocation(70, 0));
        panelLocations.add(5, new PanelLocation(70, -60));
        panelLocations.add(6, new PanelLocation(0, 90));
        panelLocations.add(7, new PanelLocation(0, 0));
        panelLocations.add(8, new PanelLocation(0, -80));
        panelLocations.add(9, new PanelLocation(-80, 50));
        panelLocations.add(10, new PanelLocation(-80, 0));
        panelLocations.add(11, new PanelLocation(-80, 60));
        panelLocations.add(12, new PanelLocation(-120, 80));
        panelLocations.add(13, new PanelLocation(-120, 0));
        panelLocations.add(14, new PanelLocation(-120, -80));


        this.add(this.tabbedPane);

        for (AbstractRawInput rawInput : this.rawInputs) {
            rawInput.setInputListener(this);
            this.tabbedPane.addTab(rawInput.getTabName(), null, rawInput, null);
        }
    }

    public static void addRawInputListener(RawInputListener ril) {
        RawInput.rawInputMultiplexer.imageListeners.add(ril);
    }

    public void nextFrame(BufferedImage image, long time) {
        this.lastImage = image;

        try {
            CURRENT_REGION = closestPlateRegion(world.getRobot(RobotType.FRIEND_2).location.getX(), world.getRobot(RobotType.FRIEND_2).location.getY());
            String path = Constants.settingsFilePath(CURRENT_REGION);
            int ballRegion = closestPlateRegion(world.getBall().location.getX(), world.getBall().location.getY());
            String ballPath = Constants.settingsFilePath(ballRegion);
            System.out.println("ROBOT PATH :" + path);
            System.out.println("BALL PATH :" + ballPath);
            SettingsManager.reloadSettings(path, ballPath, CURRENT_REGION, ballRegion);
//      SettingsManager.loadBallSettings(ballPath);

        } catch (Exception x) {
        }

        for (RawInputListener ril : this.imageListeners) {
            ril.nextFrame(image, time);
        }
    }

    //CALCULATE POLAR DISTANCE

    public double distanceFrom(double r1, double r2, double theta1, double theta2) {
        return Math.sqrt(r1 * r1 + r2 * r2 - 2 * r1 * r2 * Math.cos(theta2 - theta1));
    }

    //CALCULATE CLOSEST PLATE CALIBRATION

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
        if (min_index > 0 && min < 10000000) {
            return min_index;
        }
        return -1;
    }


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
}
