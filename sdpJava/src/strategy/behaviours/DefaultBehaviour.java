package strategy.behaviours;

import strategy.actions.other.Contemplating;

/**
 * Created by Rado Kirilchev on 11/02/2017.
 */
public class DefaultBehaviour extends BehaviourBase {

    public DefaultBehaviour() {
        this.rawDescription = "Default behaviour";
    }

    // Override 'onStart()' and 'onEnd()' if you wish.
    @Override
    public void onStart() {
        // Right now, the "default behaviour" is to just do NOTHING.
        setCurrentAction(new Contemplating());
    }

    // Put actual robot *strategy* here.
    @Override
    public void update() {

    }
}
