package vision.colorAnalysis;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Simon Rovder
 */
public class ColorCalibration extends JPanel implements ActionListener {

    public static final ColorCalibration colorCalibration = new ColorCalibration();
    private List list;

    private ColorCalibration() {
        super();
        this.setLayout(null);
        list = new List();
        list.setBounds(10, 10, 273, 350);
        this.add(list);

        JButton btnCalibrate = new JButton("Calibrate");
        btnCalibrate.setBounds(289, 10, 222, 33);
        btnCalibrate.addActionListener(this);
        this.add(btnCalibrate);
        for (SDPColorInstance c : SDPColors.colors.values()) {
            this.list.add(c.name);
        }
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        String selected = this.list.getSelectedItem();
        if (selected != null) {
            SDPColors.colors.get(SDPColor.valueOf(selected)).setVisible(true);
            SDPColors.colors.get(SDPColor.valueOf(selected)).transferFocus();
        }
    }
}
