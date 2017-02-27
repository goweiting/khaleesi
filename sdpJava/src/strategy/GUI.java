package strategy;

import strategy.actions.offense.OffensiveKick;
import strategy.actions.other.*;
import strategy.behaviours.DefaultBehaviour;
import strategy.behaviours.PassiveBehaviour;
import strategy.controllers.essentials.MotionController;
import strategy.drives.ThreeWheelHolonomicDrive;
import strategy.points.basicPoints.ConstantPoint;
import strategy.robots.Khaleesi;
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
        this.maxSpeed.setText("200");
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
            this.robot.MOTION_CONTROLLER.setHeading(null);
            this.robot.MOTION_CONTROLLER.setDestination(null);
            this.robot.MOTION_CONTROLLER.clearObstacles();
            this.robot.port.sdpPort.commandSender("f");
            this.robot.port.sdpPort.commandSender("f");
            this.robot.port.sdpPort.commandSender("f");
            // I'm reorganising this, as it's over-encumbered right now.
            switch (e.getKeyChar()) {
                // W - ("work") FORCE DEFAULT BEHAVIOUR
                case 'w':
                    // Ideally, we shouldn't be injecting actions directly into the behaviour.
                    // They'd be overridden inside a REAL one, such as this one.
                    Strategy.setBehaviour(new DefaultBehaviour());
                // D - DEFEND GOAL (PASSIVE BEHAVIOUR)
                case 'd':
                    Strategy.setBehaviour(new PassiveBehaviour());
                    Strategy.getCurrentBehaviour().setCurrentAction(new DefendGoal());
                    break;
                // K - OFFENSIVE KICK (PASSIVE BEHAVIOUR)
                case 'k':
                    Strategy.setBehaviour(new PassiveBehaviour());
                    Strategy.getCurrentBehaviour().setCurrentAction(new OffensiveKick());
                    break;
                // B - APPROACH BALL (PASSIVE BEHAVIOUR)
                case 'b':
                    Strategy.setBehaviour(new PassiveBehaviour());
                    Strategy.getCurrentBehaviour().setCurrentAction(new GoToBall());
                    break;
                // S - RETREAT TO SAFE LOCATION (PASSIVE BEHAVIOUR)
                case 's':
                    Strategy.setBehaviour(new PassiveBehaviour());
                    Strategy.getCurrentBehaviour().setCurrentAction(new GoToSafeLocation());
                    break;
                // P - PATROL GOAL (PASSIVE BEHAVIOUR)
                case 'p':
                    Strategy.setBehaviour(new PassiveBehaviour());
                    Strategy.getCurrentBehaviour().setCurrentAction(new PatrolGoal());
                    break;

                // Manual kicker testing. BAD PRACTICE!
                case 'y':
                    Strategy.setBehaviour(new PassiveBehaviour());
                    this.robot.MOTION_CONTROLLER.setActive(false);
                    ((Khaleesi)this.robot).KICKER_CONTROLLER.setKickerHoldDuration(1250);
                    ((Khaleesi)this.robot).KICKER_CONTROLLER.setActive(true);
                    break;

                // NUMBERS MIRROR PITCH, I.E. PATTERN SAME AS NUMPAD
                // ALL NUMBER COMMANDS WILL CHANGE STRATEGY TO PASSIVE BEHAVIOUR
                case '1':
                    Strategy.setBehaviour(new PassiveBehaviour());
                    Strategy.getCurrentBehaviour().setCurrentAction(new HoldPosition(new ConstantPoint(-50, -50)));
                    break;
                case '2':
                    Strategy.setBehaviour(new PassiveBehaviour());
                    Strategy.getCurrentBehaviour().setCurrentAction(new HoldPosition(new ConstantPoint(0, -50)));
                    break;
                case '3':
                    Strategy.setBehaviour(new PassiveBehaviour());
                    Strategy.getCurrentBehaviour().setCurrentAction(new HoldPosition(new ConstantPoint(50, -50)));
                    break;
                case '4':
                    Strategy.setBehaviour(new PassiveBehaviour());
                    Strategy.getCurrentBehaviour().setCurrentAction(new HoldPosition(new ConstantPoint(-50, 0)));
                    break;
                case '5':
                    Strategy.setBehaviour(new PassiveBehaviour());
                    Strategy.getCurrentBehaviour().setCurrentAction(new HoldPosition(new ConstantPoint(0, 0)));
                    break;
                case '6':
                    Strategy.setBehaviour(new PassiveBehaviour());
                    Strategy.getCurrentBehaviour().setCurrentAction(new HoldPosition(new ConstantPoint(50, 0)));
                    break;
                case '7':
                    Strategy.setBehaviour(new PassiveBehaviour());
                    Strategy.getCurrentBehaviour().setCurrentAction(new HoldPosition(new ConstantPoint(-50, 50)));
                    break;
                case '8':
                    Strategy.setBehaviour(new PassiveBehaviour());
                    Strategy.getCurrentBehaviour().setCurrentAction(new HoldPosition(new ConstantPoint(0, 50)));
                    break;
                case '9':
                    Strategy.setBehaviour(new PassiveBehaviour());
                    Strategy.getCurrentBehaviour().setCurrentAction(new HoldPosition(new ConstantPoint(50, 50)));
                    break;

                // H or F or SPACE - STOP, DO NOTHING. (PASSIVE BEHAVIOUR)
                case 'h':
                case 'f':
                case ' ':
                    Strategy.setBehaviour(new PassiveBehaviour());
                    Strategy.getCurrentBehaviour().setCurrentAction(new Contemplating());
                    break;
            }
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (this.robot instanceof Khaleesi) {
            ThreeWheelHolonomicDrive drive = (ThreeWheelHolonomicDrive) this.robot.drive;
//            if (e.getSource() == this.maxSpeed) {
//                System.out.println("SpeedChange");
//                try {
//                    drive.MAX_MOTION = Integer.parseInt(this.maxSpeed.getText());
//                } catch (Exception ex) {
//                }
//                System.out.println("SpeedChange : " + drive.MAX_MOTION);
//            } else if (e.getSource() == this.turnSpeed) {
//                try {
//                    drive.MAX_ROTATION = Integer.parseInt(this.turnSpeed.getText());
//                } catch (Exception ex) {
//                }
//                System.out.println("TurnChange : " + drive.MAX_ROTATION);
//            }
        }
        r.setText("");
    }
}
