package strategy;

import java.awt.Container;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import strategy.controllers.essentials.MotionController;
import strategy.robots.RobotBase;

/** Created by Simon Rovder */
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

  public void doesNothingButIsNecessarySoDontDelete() {}

  @Override
  public void keyTyped(KeyEvent e) {}

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
        case 'k':
          System.out.println("K clicked");
          this.robot.setControllersActive(true);
          break;
        case 'h':
        case ' ':
        default:
          this.robot.MOTION_CONTROLLER.setMode(MotionController.MotionMode.OFF);
          break;
      }
    }
  }

  @Override
  public void keyReleased(KeyEvent e) {
    r.setText("");
  }
}
