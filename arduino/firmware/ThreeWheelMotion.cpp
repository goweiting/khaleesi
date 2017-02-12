#include "ThreeWheelMotion.h"
#include "SDPArduino.h"

#define VISION_TIMEOUT 300 // ms - the time to considered that the vision is no longer seeing the robot
#define TIMSTEP 100 // ms - generate prediction every 100ms
double FORCE_COUPLING[3][3] ={{-.33, .58, .33}, {-.33, -.58, .33}, {.67, 0, .33}};

#define P_const 1;
#define D_const 0.5;

/*
 * @param myX, myY, myHeading : the robot's current X,Y, and heading (wrt to
 *                            the global frame)
 * @param forceX, forceY : the speed in X and Y components (wrt global frame)
 *                          that robot should move in
 * @param rotation: the angle that the robot should tilt at the final position
 */
void gotoXY(double myX, double myY, double myHeading,
            double forceX, double forceY, double rotation ) {
    
}

void forwardKinematics();

void inverseKinematics();
