package strategy.actions;

/** Created by Simon Rovder, then purged by Rado Kirilchev */
public abstract class ActionBase implements ActionInterface {

  protected String rawDescription = null;
  private boolean hasStarted = false;

  // Blank implementations for these two
  // NOTE: you MUST turn all controllers on.
  @Override
  public void onStart() {
    hasStarted = true;
  }

  @Override
  public void onEnd() {
    hasStarted = false;
  }

  // I'll keep this
  @Override
  public String description() {
    String description = this.rawDescription;
    if (description == null) description = this.getClass().getName();
    return description;
  }

  public boolean hasStarted() {
    return hasStarted;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof ActionBase)) return false;

    ActionBase that = (ActionBase) o;

    return rawDescription != null
        ? rawDescription.equals(that.rawDescription)
        : that.rawDescription == null;
  }

  @Override
  public int hashCode() {
    return rawDescription != null ? rawDescription.hashCode() : 0;
  }
}
