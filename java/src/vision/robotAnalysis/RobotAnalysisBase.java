package vision.robotAnalysis;

import java.util.LinkedList;
import vision.DynamicWorld;
import vision.distortion.DistortionListener;

/** Created by Simon Rovder */
public abstract class RobotAnalysisBase implements DistortionListener {

  protected DynamicWorld lastKnownWorld = null;
  private LinkedList<DynamicWorldListener> listeners;

  public RobotAnalysisBase() {
    this.listeners = new LinkedList<DynamicWorldListener>();
  }

  public void addDynamicWorldListener(DynamicWorldListener listener) {
    this.listeners.add(listener);
  }

  protected void informListeners(DynamicWorld world) {

    this.lastKnownWorld = world;
    for (DynamicWorldListener listener : this.listeners) {
      listener.nextDynamicWorld(world);
    }
  }
}
