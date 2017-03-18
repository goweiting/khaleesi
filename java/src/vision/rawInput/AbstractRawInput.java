package vision.rawInput;

import javax.swing.JPanel;
/** Created by Simon Rovder */
abstract class AbstractRawInput extends JPanel implements RawInputInterface {

  public String tabName;
  protected RawInput listener;
  private boolean active;

  public void setInputListener(RawInput listener) {
    this.listener = listener;
  }

  public boolean isActive() {
    return this.active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

  public String getTabName() {
    return this.tabName;
  }
}
