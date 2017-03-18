package strategy.actions;

/** Created by Simon Rovder, then everything inside was deleted and re-forged by Rado Kirilchev */
public interface ActionInterface {

  // Called ONCE when it all starts
  void onStart();

  // Called every single update cycle when this action is active.
  // Set destination, heading, and controllers here
  void update();

  // Called ONCE when the action ends.
  void onEnd();

  // Description for the Command box and the question mark.
  String description();
}
