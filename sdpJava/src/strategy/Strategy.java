package strategy;

import communication.PortListener;
import communication.ports.robotPorts.KhaleesiRobotPort;
import strategy.actions.ActionBase;
import strategy.actions.offense.OffensiveKick;
import strategy.actions.other.Contemplating;
import strategy.actions.other.DefendGoal;
import strategy.actions.other.GoToSafeLocation;
import strategy.actions.other.HoldPosition;
import strategy.behaviours.BehaviourBase;
import strategy.behaviours.DefaultBehaviour;
import strategy.behaviours.PassiveBehaviour;
import strategy.points.basicPoints.*;
import strategy.robots.Khaleesi;
import strategy.robots.RobotBase;
import vision.*;
import vision.Robot;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Simon Rovder
 * Edited by Wildfire
 * Mutilated, disemboweled, and remade by Rado Kirilchev
 */
public class Strategy implements VisionListener, PortListener, ActionListener {


    /**
     * SDP2017NOTE The following variable is a static variable always containing the very last known
     * state of the world. It is accessible from anywhere in the project at any time as
     * Strategy.world
     */
    public static DynamicWorld world = null;
    private Timer timer;
    private String action;
    private Vision vision;

    // This is the current robot, as seen by the vision. Check locations, velocities, etc. with it.
    // Can be null if the vision can't see where we are.
    public static Robot curVisionRobot = null;
    // This is the current robot as an actual instance of Khaleesi. It never changes, and cannot be null;
    public static RobotBase currentRobotBase;
    //private RobotBase[] robots;
    private static BehaviourBase currentBehaviour;


    public Strategy(String[] args) {

        /*
         * SDP2017NOTE
         * Create your robots in the following line. All these robots will be instantly connected to the
         * navigation system and all its controllers will be launched every cycle.
         */
        // RK: Replaced this with static reference to one robot, because honestly, we don't need more
        //this.robots = new RobotBase[]{new Khaleesi(RobotType.FRIEND_2)};
        Khaleesi khaleesi = new Khaleesi(RobotType.FRIEND_2);
        KhaleesiRobotPort port = (KhaleesiRobotPort)khaleesi.port;
        currentRobotBase = khaleesi;

        // Assign default behaviour by... well... default.
        // Actually, we're using the PASSIVE behaviour by default. The "default" one actually does things
        currentBehaviour = new PassiveBehaviour();

        final Strategy semiStrategy = this;
        semiStrategy.vision = new Vision(args);
        semiStrategy.vision.addVisionListener(semiStrategy);

//  khaleesi.PROPELLER_CONTROLLER.setActive(false); // comment out if not in use

        this.action = "";
        GUI.gui.doesNothingButIsNecessarySoDontDelete();
        GUI.gui.setRobot(currentRobotBase);
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
//        khaleesi.PROPELLER_CONTROLLER.setActive(false);
//        port.propeller(0);
//        port.propeller(0);
//        port.propeller(0);
                break;
            }
            switch (this.action) {
                case "a":
                    khaleesi.setControllersActive(true);
                    break;
                case "!":
                    System.out.print("Motion active: ");
                    System.out.print(khaleesi.MOTION_CONTROLLER.isActive());
                    System.out.print(" Kicker active: ");
                    System.out.print(khaleesi.KICKER_CONTROLLER.isActive());
                    System.out.print(", kick in progress ");
                    System.out.print(khaleesi.KICKER_CONTROLLER.isKickInProgress() + "\n");
                    break;
                case "?":
                    System.out.println(currentBehaviour.description() + ": " +
                                       currentBehaviour.getCurrentAction().description());
                    break;
                case "hold":
                    currentBehaviour.setCurrentAction(new HoldPosition(new MidFoePoint()));
                    break;
                case "kick":
                    currentBehaviour.setCurrentAction(new OffensiveKick());
                    break;
                case "h":
                    currentBehaviour.setCurrentAction(new Contemplating());
                    khaleesi.MOTION_CONTROLLER.setDestination(null);
                    khaleesi.MOTION_CONTROLLER.setHeading(null);
                    port.halt();
                    port.halt();
                    port.halt();
                    break;
                case "reset":
                    currentBehaviour.setCurrentAction(new HoldPosition(new ConstantPoint(0, 0)));
                    break;
                case "behave":
                    // Set proper gameplay behaviour here.
                    currentBehaviour = new DefaultBehaviour();
                    break;
                case "safe":
                    currentBehaviour.setCurrentAction(new GoToSafeLocation());
                    break;
                case "def":
                    currentBehaviour.setCurrentAction(new DefendGoal());
                    break;
                case "annoy":
                    currentBehaviour.setCurrentAction(null);
                    khaleesi.MOTION_CONTROLLER.setDestination(new InFrontOfRobot(RobotAlias.FELIX));
                    khaleesi.MOTION_CONTROLLER.setHeading(new RobotPoint(RobotAlias.FELIX));
                    break;
//        case "rot":
//          khaleesi.PROPELLER_CONTROLLER.setActive(false);
//          ((KhaleesiRobotPort) khaleesi.port).propeller(0);
//          ((KhaleesiRobotPort) khaleesi.port).propeller(0);
//          ((KhaleesiRobotPort) khaleesi.port).propeller(0);
//          khaleesi.ACTION_CONTROLLER.setActive(false);
//          khaleesi.MOTION_CONTROLLER.setDestination(new Rotate());
//          khaleesi.MOTION_CONTROLLER.setHeading(new BallPoint());
//          break;
//        case "p":
//          boolean act = khaleesi.PROPELLER_CONTROLLER.isActive();
//          khaleesi.PROPELLER_CONTROLLER.setActive(!act);
//          if (!act) {
//            ((KhaleesiRobotPort) khaleesi.port).propeller(0);
//            ((KhaleesiRobotPort) khaleesi.port).propeller(0);
//            ((KhaleesiRobotPort) khaleesi.port).propeller(0);
//          }
//          System.out.println(khaleesi.PROPELLER_CONTROLLER.isActive());
//          break;
                case "test":
                    khaleesi.MOTION_CONTROLLER.setHeading(new EnemyGoal());
                    khaleesi.MOTION_CONTROLLER.setDestination(new EnemyGoal());
                    khaleesi.MOTION_CONTROLLER.perform();
                    break;

                //drives 2 front wheels forward
                case "driveForward":
                    ((KhaleesiRobotPort) khaleesi.port).threeWheelHolonomicMotion(100, 100, 0);
                    ((KhaleesiRobotPort) khaleesi.port).threeWheelHolonomicMotion(100, 100, 0);
                    ((KhaleesiRobotPort) khaleesi.port).threeWheelHolonomicMotion(100, 100, 0);
                    break;
                case "driveStop":
                    ((KhaleesiRobotPort) khaleesi.port).threeWheelHolonomicMotion(0, 0, 0);
                    ((KhaleesiRobotPort) khaleesi.port).threeWheelHolonomicMotion(0, 0, 0);
                    ((KhaleesiRobotPort) khaleesi.port).threeWheelHolonomicMotion(0, 0, 0);
                    break;

                /******************** KHALEESI'S ACTION ********************/
                // spins dribbler and kicker
                case "dk":
                    ((KhaleesiRobotPort) khaleesi.port).dribblerKicker(100, 100);
                    break;
                case "dkStop":
                    ((KhaleesiRobotPort) khaleesi.port).dribblerKicker(0, 0);
                    break;
                case "attemptKick":
                    khaleesi.KICKER_CONTROLLER.setActive(true);
                    khaleesi.KICKER_CONTROLLER.perform();
                    break;
                case "stopKick":
                    khaleesi.KICKER_CONTROLLER.setActive(false);
                    break;

                case "fullstop":
                    ((KhaleesiRobotPort) khaleesi.port).threeWheelHolonomicMotion(0, 0, 0);
                    ((KhaleesiRobotPort) khaleesi.port).threeWheelHolonomicMotion(0, 0, 0);
                    ((KhaleesiRobotPort) khaleesi.port).threeWheelHolonomicMotion(0, 0, 0);
                    khaleesi.KICKER_CONTROLLER.setActive(false);
                    //khaleesi.DRIBBLER_CONTROLLER.setActive(false);
                    break;
            }
        }

        this.vision.terminateVision();
        System.exit(0);

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
     * SDP2017NOTE
     * This is the main() you want to run. It launches everything.
     */
    public static void main(String[] args) {
        new Strategy(args);
    }

    /**
     * SDP2017NOTE This is the main loop of the entire strategy module. It is launched every couple of
     * milliseconds. Insert all your clever things here. You can access Strategy.world from here and
     * control robots.
     */
    // Actually, don't touch this. Stuff all the logic inside the currently-running Behaviour.
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
    public void receivedStringHandler(String string) {
    }

    // Support methods for behavioural control.
    // I don't see why we might need this specific one, but hey, who knows.
    public static void restartBehaviour() {
        if (currentBehaviour == null) return; // This should never happen in the first place.
        currentBehaviour.onEnd();
        currentBehaviour.onStart();
    }

    public static void setBehaviour(BehaviourBase behaviour) {
        if (currentBehaviour.equals(behaviour)) return;
        if (currentBehaviour != null) currentBehaviour.onEnd();
        currentRobotBase.setControllersActive(false);
        currentBehaviour = behaviour;
        currentBehaviour.onStart();
    }

    public static BehaviourBase getCurrentBehaviour() {
        return currentBehaviour;
    }


}
