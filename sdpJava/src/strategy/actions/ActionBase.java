package strategy.actions;

/**
 * Created by Simon Rovder, then purged by Rado Kirilchev
 */
public abstract class ActionBase implements ActionInterface {

    protected String rawDescription = null;

    // Blank implementations for these two - again, they're unnecessary
    @Override
    public void onStart() {

    }

    @Override
    public void onEnd() {

    }

    // I'll keep this
    @Override
    public String description() {
        String description = this.rawDescription;
        if (description == null) description = this.getClass().getName();
        return description;
    }
}
