package vision;

import java.awt.BorderLayout;
import java.util.LinkedList;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import vision.colorAnalysis.ColorCalibration;
import vision.distortion.Distortion;
import vision.distortion.DistortionPreview;
import vision.gui.MiscellaneousSettings;
import vision.gui.Preview;
import vision.gui.SDPConsole;
import vision.rawInput.RawInput;
import vision.robotAnalysis.DynamicWorldListener;
import vision.robotAnalysis.RobotAnalysisBase;
import vision.robotAnalysis.RobotPreview;
import vision.robotAnalysis.newRobotAnalysis.NewRobotAnalysis;
import vision.spotAnalysis.SpotAnalysisBase;
import vision.spotAnalysis.approximatedSpotAnalysis.ApproximatedSpotAnalysis;
import vision.spotAnalysis.recursiveSpotAnalysis.RecursiveSpotAnalysis;
import vision.tools.CommandLineParser;

/**
 * Created by Simon Rovder
 *
 * <p>SDP2017NOTE This is the main Vision class. It creates the entire vision system. Run this file
 * to see the magic. :)
 */
public class Vision extends JFrame implements DynamicWorldListener {

  private LinkedList<VisionListener> visionListeners;

  /**
   * Vision system constructor. Please please please only call this once, or else it goes haywire.
   */
  public Vision(String[] args) {
    super("Vision");

    this.visionListeners = new LinkedList<VisionListener>();
    JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);

    SpotAnalysisBase recursiveSpotAnalysis = new RecursiveSpotAnalysis();
    SpotAnalysisBase approximateSpotAnalysis = new ApproximatedSpotAnalysis();

    // SDP2017NOTE
    // This part builds the vision system pipeline
    RawInput.addRawInputListener(recursiveSpotAnalysis);
    RawInput.addRawInputListener(Preview.preview);
    RawInput.addRawInputListener(Distortion.distortion);
    recursiveSpotAnalysis.addSpotListener(Distortion.distortion);
    DistortionPreview.addDistortionPreviewClickListener(Distortion.distortion);
    Distortion.addDistortionListener(RobotPreview.preview);

    RobotAnalysisBase robotAnalysis = new NewRobotAnalysis();
    Distortion.addDistortionListener(robotAnalysis);
    robotAnalysis.addDynamicWorldListener(RobotPreview.preview);
    robotAnalysis.addDynamicWorldListener(this);

    // Active vision listener
    //		RawInput.addRawInputListener(new ActiveVisionBase());

    tabbedPane.addTab("Input Selection", null, RawInput.rawInputMultiplexer, null);
    tabbedPane.addTab("Color Calibration", null, ColorCalibration.colorCalibration, null);
    tabbedPane.addTab("Distortion", null, Distortion.distortion, null);
    //		tabbedPane.addTab("Robots", null, RobotAnalysis.strategy.robots, null);
    tabbedPane.addTab("Misc Settings", null, MiscellaneousSettings.miscSettings, null);

    SDPConsole.console.setVisible(true);

    this.getContentPane().add(tabbedPane, BorderLayout.CENTER);
    this.setSize(640, 480);
    this.addWindowListener(
        new java.awt.event.WindowAdapter() {
          public void windowClosing(java.awt.event.WindowEvent evt) {
            terminateVision();
          }
        });
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    CommandLineParser.parser.newParse(args, this);
    this.setVisible(true);
  }

  public static void main(String[] args) {
    new Vision(args);
  }

  /**
   * Add a vision listener. The Listener will be notified whenever the vision system has a new
   * world.
   *
   * @param visionListener Your class
   */
  public void addVisionListener(VisionListener visionListener) {
    this.visionListeners.add(visionListener);
  }

  /** Call this function to safely turn off all the Vision stuff. */
  public void terminateVision() {
    RawInput.rawInputMultiplexer.stopAllInputs();
  }

  @Override
  public void nextDynamicWorld(DynamicWorld state) {
    for (VisionListener visionListener : this.visionListeners) {
      visionListener.nextWorld(state);
    }
  }
}
