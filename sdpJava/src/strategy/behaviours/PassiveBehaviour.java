package strategy.behaviours;

/**
 * RK This is only to make sure the robot does NOTHING before given a command
 */
public class PassiveBehaviour extends BehaviourBase {

    public PassiveBehaviour() {
        this.rawDescription = "Passive behaviour";
    }

    @Override
    public void onStart() {
        // When we start up, we'd like to

        super.onStart();
    }

    // Actual strategy.
    @Override
    public void update() {

    }
}
