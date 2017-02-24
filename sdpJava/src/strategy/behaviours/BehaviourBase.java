package strategy.behaviours;

import strategy.Strategy;
import strategy.actions.ActionBase;

/**
 * Created by Rado Kirilchev on 11/02/2017.
 */
// And a base class for the behaviours.
public abstract class BehaviourBase implements BehaviourInterface {
    private ActionBase currentAction = null;
    protected String rawDescription = null;
    private boolean hasStarted = false;

    @Override
    public void onStart() {
        hasStarted = true;
    }

    // We don't implement 'update()' here, because that's essential for every behaviour itself.

    @Override
    public void onEnd() {
        hasStarted = false;
    }

    @Override
    public String description() {
        String description = this.rawDescription;
        if (description == null) description = this.getClass().getName();
        return description;
    }

    // Making these public so that we can force-set actions through the console and command box.
    // This does sort of break the encapsulation, but hey, it's probably useful.
    public void restartAction() {
        if (currentAction == null) return;
        currentAction.onEnd();
        currentAction.onStart();
    }

    public void setCurrentAction(ActionBase action) {
        if (currentAction.equals(action)) return; // Do not restart. There's a function for that.
        if (currentAction != null) currentAction.onEnd();
        Strategy.currentRobotBase.setControllersActive(false);
        currentAction = action;
        currentAction.onStart();
    }

    public ActionBase getCurrentAction() {
        return currentAction;
    }

    public boolean hasStarted() {
        return hasStarted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BehaviourBase)) return false;

        BehaviourBase that = (BehaviourBase) o;

        return rawDescription != null ? rawDescription.equals(that.rawDescription) : that.rawDescription == null;
    }

    @Override
    public int hashCode() {
        return rawDescription != null ? rawDescription.hashCode() : 0;
    }
}
