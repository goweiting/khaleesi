package vision.robotAnalysis;

import vision.colorAnalysis.SDPColor;
import vision.colorAnalysis.SDPColorInstance;

import javax.swing.*;

/**
 * Created by Simon Rovder
 */
public class SDPColorSelector extends JComboBox<String> {

    public SDPColorSelector() {
        super();
        for (SDPColor c : SDPColor.values()) {
            this.addItem(c.toString());
        }
    }

    public SDPColor getSelectedSDPColorInstance() {
        return SDPColor.valueOf((String) this.getSelectedItem());
    }

    public void setSelectedSDPColorInstance(SDPColorInstance instance) {
        this.setSelectedItem(instance.name);
    }
}
