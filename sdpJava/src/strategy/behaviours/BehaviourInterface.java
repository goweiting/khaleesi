package strategy.behaviours;

/** Created by Rado Kirilchev on 11/02/2017. */
public interface BehaviourInterface {
  // A basic interface for the lifecycle of a Behaviour.
  // These should be pretty self-explanatory, but just in case:

  // Called ONCE when the behaviour is set
  void onStart();

  // Called every single cycle. Put logic here.
  void update();

  // Called ONCE when/if behaviour stopped (OR replaced)
  void onEnd();

  // I'm also going to lift the descriptions
  String description();
}
