package strategy;

import PolarCoordNavigation.Coordinates.CartesianCoordinate;
import strategy.actions.ActionBase;
import strategy.actions.Contemplating;
import strategy.actions.TargetPosition;
import strategy.behaviours.BehaviourBase;
import strategy.behaviours.DefaultBehaviour;
import strategy.behaviours.PassiveBehaviour;
import strategy.controllers.essentials.MotionController;
import strategy.robots.RobotBase;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Created by Simon Rovder
 */
public class GUI extends JFrame implements KeyListener {

    public static final GUI gui = new GUI();
    public JTextField action;
    public JTextField searchType;
    public JTextField behaviour;
    private JTextField r;
    private JTextField maxSpeed;
    private JTextField turnSpeed;
    private RobotBase robot;

    private GUI() {
        super("Strategy");
        this.setSize(640, 480);
        this.setLayout(null);
        Container c = this.getContentPane();

        JLabel label = new JLabel("Action:");
        label.setBounds(20, 20, 200, 30);
        c.add(label);

        this.action = new JTextField();
        this.action.setBounds(220, 20, 300, 30);
        this.action.setEditable(false);
        c.add(this.action);

        label = new JLabel("NavigationInterface:");
        label.setBounds(20, 60, 200, 30);
        c.add(label);

        this.searchType = new JTextField();
        this.searchType.setBounds(220, 60, 300, 30);
        this.searchType.setEditable(false);
        c.add(this.searchType);

        label = new JLabel("Behavior Mode:");
        label.setBounds(20, 100, 200, 30);
        c.add(label);

        this.behaviour = new JTextField();
        this.behaviour.setBounds(220, 100, 300, 30);
        this.behaviour.setEditable(false);
        c.add(this.behaviour);
        this.addKeyListener(this);

        //
        //
        //
        //
        //        this.behaviour = new JTextField();
        //        this.behaviour.setBounds(20,100,300,30);
        //        this.behaviour.setEditable(false);
        //        c.add(this.behaviour);
        //        this.addKeyListener(this);
        this.setVisible(true);

        label = new JLabel("Maximum Speed:");
        label.setBounds(20, 140, 200, 30);
        c.add(label);
        this.maxSpeed = new JTextField();
        this.maxSpeed.setBounds(220, 140, 300, 30);
        this.maxSpeed.setText("100");
        c.add(this.maxSpeed);
        this.maxSpeed.addKeyListener(this);

        label = new JLabel("Maximum rotation speed:");
        label.setBounds(20, 180, 200, 30);
        c.add(label);
        this.turnSpeed = new JTextField();
        this.turnSpeed.setBounds(220, 180, 300, 30);
        this.turnSpeed.setText("30");
        c.add(this.turnSpeed);
        this.turnSpeed.addKeyListener(this);

        label = new JLabel("Command box:");
        label.setBounds(20, 250, 200, 30);
        c.add(label);
        r = new JTextField();
        r.setBounds(220, 250, 300, 30);
        c.add(r);
        r.addKeyListener(this);
    }

    public void doesNothingButIsNecessarySoDontDelete() {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    public void setRobot(RobotBase robot) {
        this.robot = robot;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getSource() == this.r) {
            this.robot.MOTION_CONTROLLER.setMode(MotionController.MotionMode.ON);
            this.robot.port.sdpPort.commandSender("f");
            this.robot.port.sdpPort.commandSender("f");
            this.robot.port.sdpPort.commandSender("f");
            switch (e.getKeyChar()) {
                // K - offensive kick
                case 'k':
                    // Currently, this does nothing special. Just makes sure we're up and running and attacking.
                    Strategy.setBehaviour(new DefaultBehaviour());
                    break;

                // Disabled for now
//                // Manual kicker testing. BAD PRACTICE!
//                case 'y':
//                    Strategy.setBehaviour(new PassiveBehaviour());
//                    this.robot.MOTION_CONTROLLER.setActive(false);
//                    ((Khaleesi)this.robot).KICKER_CONTROLLER.setKickerHoldDuration(1250);
//                    ((Khaleesi)this.robot).KICKER_CONTROLLER.setActive(true);
//                    break;

                // Drives no longer interchangeable
//                // DRIVE SETTINGS:
//                // MINUS uses default Fred drive, i.e. strategy and navigation
//                case '-':
//                    ((Khaleesi)this.robot).setDrive(new FourWheelHolonomicDrive());
//                    break;
//                // PLUS uses group 12 drive, i.e. strategy completely unrelated to the other one.
//                case '+':
//                    ((Khaleesi)this.robot).setDrive(new HorizVertSimpleDrive());
//                    break;


                // NUMBERS MIRROR PITCH, I.E. PATTERN SAME AS NUMPAD
                // ALL NUMBER COMMANDS WILL CHANGE STRATEGY TO PASSIVE BEHAVIOUR
                // do we really need these? probably not
                case '1':
                    Strategy.setBehaviour(new PassiveBehaviour());
                    Strategy.getCurrentBehaviour()
                            .setCurrentAction(new TargetPosition(new CartesianCoordinate(-50, -50)));
                    break;
                case '2':
                    Strategy.setBehaviour(new PassiveBehaviour());
                    Strategy.getCurrentBehaviour()
                            .setCurrentAction(new TargetPosition(new CartesianCoordinate(0, -50)));
                    break;
                case '3':
                    Strategy.setBehaviour(new PassiveBehaviour());
                    Strategy.getCurrentBehaviour()
                            .setCurrentAction(new TargetPosition(new CartesianCoordinate(50, -50)));
                    break;
                case '4':
                    Strategy.setBehaviour(new PassiveBehaviour());
                    Strategy.getCurrentBehaviour()
                            .setCurrentAction(new TargetPosition(new CartesianCoordinate(-50, 0)));
                    break;
                case '5':
                    Strategy.setBehaviour(new PassiveBehaviour());
                    Strategy.getCurrentBehaviour()
                            .setCurrentAction(new TargetPosition(new CartesianCoordinate(0, 0)));
                    break;
                case '6':
                    Strategy.setBehaviour(new PassiveBehaviour());
                    Strategy.getCurrentBehaviour()
                            .setCurrentAction(new TargetPosition(new CartesianCoordinate(50, 0)));
                    break;
                case '7':
                    Strategy.setBehaviour(new PassiveBehaviour());
                    Strategy.getCurrentBehaviour()
                            .setCurrentAction(new TargetPosition(new CartesianCoordinate(-50, 50)));
                    break;
                case '8':
                    Strategy.setBehaviour(new PassiveBehaviour());
                    Strategy.getCurrentBehaviour()
                            .setCurrentAction(new TargetPosition(new CartesianCoordinate(0, 50)));
                    break;
                case '9':
                    Strategy.setBehaviour(new PassiveBehaviour());
                    Strategy.getCurrentBehaviour()
                            .setCurrentAction(new TargetPosition(new CartesianCoordinate(50, 50)));
                    break;

                // H or F or SPACE - STOP, DO NOTHING. (PASSIVE BEHAVIOUR)
                case 'h':
                case 'f':
                case ' ':
                    Strategy.setBehaviour(new PassiveBehaviour());
                    Strategy.getCurrentBehaviour().setCurrentAction(new Contemplating());
                    break;
            }

            // Actually update the label which is supposed to display current behaviour
            BehaviourBase behaviour = Strategy.getCurrentBehaviour();
            String info = (behaviour == null) ? "UNDEFINED behaviour" : behaviour.description();
            info += ": ";
            if (behaviour == null) info += "UNDEFINED action";
            else {
                ActionBase action = Strategy.getCurrentBehaviour().getCurrentAction();
                if (action == null) info += "UNDEFINED action";
                else info += action.description();
            }
            this.action.setText(info);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        r.setText("");
    }
}
