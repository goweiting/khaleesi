package strategy.behaviours;

import strategy.actions.ActionBase;

/**
 * Created by Rado Kirilchev on 11/02/2017.
 */
// And a base class for the behaviours.
public abstract class BehaviourBase implements BehaviourInterface {
    private ActionBase currentAction = null;
    protected String rawDescription = null;

    @Override
    public void onStart() {

    }

    // We don't implement 'update()' here, because that's essential for every behaviour itself.

    @Override
    public void onEnd() {

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
        currentAction.onEnd();
        currentAction.onStart();
    }

    public void setCurrentAction(ActionBase action) {
        currentAction.onEnd();
        currentAction = action;
        currentAction.onStart();
    }

    public ActionBase getCurrentAction() {
        return currentAction;
    }

}
