package strategy.behaviours;

import strategy.Strategy;
import strategy.actions.OffensiveAction;

/** Created by Rado Kirilchev on 11/02/2017. */
public class DefaultBehaviour extends BehaviourBase {

    public DefaultBehaviour() {
        this.rawDescription = "Default behaviour";
    }

    @Override
    public void onStart() {
        // PITCH SCHEMA
        // ┌──────────────────────────┬──────────────────────────┐(W/2, H/2)
        // │                          │                          │
        // │                          │                          │
        // │                          │                          │ (W = PITCH_WIDTH
        // ├────────────┐             │             ┌────────────┤  H = PITCH_HEIGHT)
        // │            │             │             │            │
        // │            │             │             │            │
        // │OUR         │             │(0, 0)       │        FOE │(W/2, 0)
        // ├────────────┼─────────────┼─────────────┼────────────┤
        // │GOAL        │             │             │        GOAL│
        // │            │             │             │            │
        // │            │             │             │            │
        // ├────────────┘             │             └────────────┤
        // │                          │                          │
        // │                          │                          │
        // │                          │                          │(W/2, -H/2)
        // └──────────────────────────┴──────────────────────────┘  x -->

        // As decided, we're sticking to the LEFT side, our own.
        // On start, we can assume our teammate charges forward and attempts to score.
        // Ergo, we'll retreat and patrol our own goal.
        //setCurrentAction(new PatrolGoal());

        // We're currently NOT doing anything special if we stop seeing ourselves!


        // EDIT: Just make sure movement is on and assume offensive behaviour.
        Strategy.currentRobotBase.MOTION_CONTROLLER.setActive(true);
        setCurrentAction(new OffensiveAction());

        super.onStart();
    }

    @Override
    public void update() {

    }

//    @Override
//    public void update() {
//        // Check where the ball is.
//        Status curBallStatus = new Status(Strategy.world);
//        Ball ball = Strategy.world.getBall();
//
//        Robot us = Strategy.curVisionRobot;
//        Robot teammate = Strategy.world.getRobot(RobotType.FRIEND_1);
//        Robot opponent1 = Strategy.world.getRobot(RobotType.FOE_1);
//        Robot opponent2 = Strategy.world.getRobot(RobotType.FOE_2);
//
//        // Don't interrupt the kicker if we're currently fiddling with the ball.
//        OnagerKicker kicker = ((Khaleesi) Strategy.currentRobotBase).KICKER_CONTROLLER;
//        if (!kicker.isInManualMode() && kicker.isKickInProgress()) return;
//
//        VectorGeometry opposingGoal =
//                new VectorGeometry(new EnemyGoal().getX(), new EnemyGoal().getY());
//
//        if (curBallStatus.ballState == Status.BallState.LOST) {
//            ball = Strategy.world.getLastKnownBall();
//        }
//        // We should know where the last known ball is.
//        // Just to be safe, assume the goal is in danger if we
//        // don't even known where the ball might've recently been,
//        // or if we don't know where WE are.
//        if (ball == null || us == null) {
//            setCurrentAction(new DefendGoal());
//            return;
//        }
//
//        // Decide what to do based on where the ball is.
//        switch (curBallStatus.ballState) {
//            // If we've got the ball, either try sniping, or passing to our friend
//            case ME:
//                // In general, it's better to shoot - less complex, more chance to work out.
//                // However...
//                // If we can't see our mate, shoot at the goal.
//                if (teammate == null) {
//                    setCurrentAction(new OffensiveAction());
//                    return;
//                }
//                // If we CAN see our mate, look for the opponents.
//                boolean goalObstructed = false;
//                if (opponent1 != null
//                        && VectorGeometry.isBetweenPoints(opponent1.location, us.location, opposingGoal))
//                    goalObstructed = true;
//                if (opponent2 != null
//                        && VectorGeometry.isBetweenPoints(opponent2.location, us.location, opposingGoal))
//                    goalObstructed = true;
//
//                if (!goalObstructed) {
//                    setCurrentAction(new OffensiveAction());
//                    return;
//                } else {
//                    setCurrentAction(new PassingKick());
//                    return;
//                }
//                // If our teammate has it, revert to patrolling
//            case FRIEND:
//                setCurrentAction(new PatrolGoal());
//                return;
//            // If the opponents hold it, defend goal
//            case THEM:
//                setCurrentAction(new DefendGoal());
//                return;
//            // If nobody has it, look:
//            case FREE:
//                // If the ball is on the opposing side of the pitch, retreat to patrolling mode
//                if (WorldTools.isPointInEnemyDefenceArea(ball.location)) {
//                    setCurrentAction(new PatrolGoal());
//                    return;
//                }
//                // Otherwise, assume we need to handle the ball.
//                setCurrentAction(new OffensiveAction());
//        }
//    }
}
