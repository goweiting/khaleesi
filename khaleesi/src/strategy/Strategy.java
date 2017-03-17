package strategy;

import communication.PortListener;
import communication.ports.robotPorts.Diag4RobotPort;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.swing.Timer;
import strategy.robots.Diag4;
import strategy.robots.RobotBase;
import vision.DynamicWorld;
import vision.RobotType;
import vision.Vision;
import vision.VisionListener;

/** Created by Simon Rovder */
public class Strategy implements VisionListener, PortListener, ActionListener {

  /**
   * SDP2017NOTE The following variable is a static variable always containing the very last known
   * state of the world. It is accessible from anywhere in the project at any time as Strategy.world
   */
  public static DynamicWorld world = null;

  public static Status status;
  private Timer timer;
  private String action;
  private Vision vision;
  private RobotBase[] robots;

  public Strategy(String[] args) {

    /*
     * SDP2017NOTE
     * Create your robots in the following line. All these robots will be instantly connected to the
     * navigation system and all its controllers will be launched every cycle.
     */
    this.robots = new RobotBase[] {new Diag4(RobotType.FRIEND_2)};

    Diag4 diag4 = (Diag4) this.robots[0];
    Diag4RobotPort port = (Diag4RobotPort) diag4.port;

    final Strategy semiStrategy = this;
    semiStrategy.vision = new Vision(args);
    semiStrategy.vision.addVisionListener(semiStrategy);

    this.action = "";
    GUI.gui.doesNothingButIsNecessarySoDontDelete();
    GUI.gui.setRobot(diag4);
    this.timer = new Timer(100, this);
    this.timer.start();

    while (true) {
      /*
       * SDP2017NOTE
       * This is a debug loop. You can add manual control over the robots here so as to make testing easier.
       * It simply loops forever. Vision System and Strategy run concurrently.
       *
       */
      System.out.print(">> ");
      this.action = this.readLine();
      if (this.action.equals("exit")) {
        break;
      }
      switch (this.action) {
        case "a":
          diag4.setControllersActive(true);
          break;
        case "!":
          System.out.print(" Motion: ");
          System.out.print(diag4.MOTION_CONTROLLER.isActive());
          System.out.print(" Propeller: ");
          break;
        case "h":
          port.halt();
          port.halt();
          port.halt();
          break;
        default:
          diag4.setControllersActive(false);
          break;
      }
    }

    this.vision.terminateVision();
    System.exit(0);
  }

  /** SDP2017NOTE This is the main() you want to run. It launches everything. */
  public static void main(String[] args) {
    new Strategy(args);
  }

  private String readLine() {
    try {
      return new BufferedReader(new InputStreamReader(System.in)).readLine();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return "";
  }

  @Override
  public void nextWorld(DynamicWorld dynamicWorld) {
    world = dynamicWorld;
    status = new Status(world);
  }

  /**
   * SDP2017NOTE This is the main loop of the entire strategy module. It is launched every couple of
   * milliseconds. Insert all your clever things here. You can access Strategy.world from here and
   * control robots.
   */
  @Override
  public void actionPerformed(ActionEvent e) {
    if (world != null) {
      for (RobotBase robot : this.robots) {
        if (world.getRobot(robot.robotType) == null) {
          // Angry yelling.
          Toolkit.getDefaultToolkit().beep();
        }
        try {
          // Tells all the Controllers of each robot to do what they need to do.
          robot.perform();
        } catch (Exception ex) {
          ex.printStackTrace();
        }
      }
    }
  }

  @Override
  public void receivedStringHandler(String string) {}
}
