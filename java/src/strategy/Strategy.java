package strategy;

import communication.PortListener;
import communication.ports.robotPorts.KhaleesiRobotPort;
import strategy.actions.ActionBase;
import strategy.behaviours.BehaviourBase;
import strategy.behaviours.PassiveBehaviour;
import strategy.robots.Khaleesi;
import strategy.robots.RobotBase;
import vision.DynamicWorld;
import vision.RobotType;
import vision.Vision;
import vision.VisionListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/** Created by Simon Rovder */
public class Strategy implements VisionListener, PortListener, ActionListener {

    public static final int TICK_INTERVAL_MSEC = 200;
    /**
     * SDP2017NOTE The following variable is a static variable always containing the very last known
     * state of the world. It is accessible from anywhere in the project at any time as Strategy.world
     */
    public static DynamicWorld world = null;
    // This is the current robot, as seen by the vision. Check locations, velocities, etc. with it.
    // Can be null if the vision can't see where we are.
    public static vision.Robot curVisionRobot = null;
    // This is the current robot as an actual instance of Khaleesi. It never changes, and cannot be null;
    public static RobotBase currentRobotBase;
    //private RobotBase[] robots;
    private static BehaviourBase currentBehaviour;
    private Timer timer;
    private String action;
    private Vision vision;

    public Strategy(String[] args) {

    /*
     * SDP2017NOTE
     * Create your robots in the following line. All these robots will be instantly connected to the
     * navigation system and all its controllers will be launched every cycle.
     */
        // RK: Replaced this with static reference to one robot, because honestly, we don't need more
        //this.robots = new RobotBase[]{new Khaleesi(RobotType.FRIEND_2)};
        Khaleesi khaleesi = new Khaleesi(RobotType.FRIEND_2);
        KhaleesiRobotPort port = (KhaleesiRobotPort) khaleesi.port;
        currentRobotBase = khaleesi;

        // Assign default behaviour by... well... default.
        // Actually, we're using the PASSIVE behaviour by default. The "default" one actually does things
        currentBehaviour = new PassiveBehaviour(); // this prevents it from starting to do things before ref's command

        final Strategy semiStrategy = this;
        semiStrategy.vision = new Vision(args);
        semiStrategy.vision.addVisionListener(semiStrategy);

        this.action = "";
        GUI.gui.doesNothingButIsNecessarySoDontDelete();
        GUI.gui.setRobot(khaleesi);
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
                    khaleesi.setControllersActive(true);
                    break;
                case "!":
                    System.out.print(" Motion: ");
                    System.out.print(khaleesi.MOTION_CONTROLLER.isActive());
                    break;
                case "?":
                    System.out.println(currentBehaviour.description() + ": "
                            + currentBehaviour.getCurrentAction().description());
                    break;
                case "h":
                    port.halt();
                    port.halt();
                    port.halt();
                    break;
                default:
                    khaleesi.setControllersActive(false);
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

    // RK: Support methods for behavioural control.
    // I don't see why we might need this specific one, but hey, who knows.
    public static void restartBehaviour() {
        if (currentBehaviour == null) return; // This should never happen in the first place.
        currentBehaviour.onEnd();
        currentBehaviour.onStart();
    }

    public static void setBehaviour(BehaviourBase behaviour) {
        if (currentBehaviour != null && currentBehaviour.equals(behaviour)) return;
        if (currentBehaviour != null) currentBehaviour.onEnd();
        currentRobotBase.setControllersActive(false);
        currentBehaviour = behaviour;
        currentBehaviour.onStart();
    }

    public static BehaviourBase getCurrentBehaviour() {
        return currentBehaviour;
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
        //status = new Status(world);
    }

    /**
     * SDP2017NOTE This is the main loop of the entire strategy module. It is launched every couple of
     * milliseconds. Insert all your clever things here. You can access Strategy.world from here and
     * control robots.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (world != null) {
            // UPDATE REFERENCE TO OURSELVES
            curVisionRobot = world.getRobot(currentRobotBase.robotType);

            //System.out.println("Strategy core ticking.");

            // TICK ALL OF OUR CONTROLLERS (and do the beeping thing if we can't see ourselves)
            if (curVisionRobot == null) Toolkit.getDefaultToolkit().beep();
            try {
                currentRobotBase.perform();
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            // UPDATE BEHAVIOUR AND ITS ACTION.
            if (currentBehaviour.hasStarted()) currentBehaviour.update();
            else currentBehaviour.onStart();
            ActionBase curAction = currentBehaviour.getCurrentAction();
            if (curAction.hasStarted()) curAction.update();
            else curAction.onStart();
        }
    }

    @Override
    public void receivedStringHandler(String string) {}
}
