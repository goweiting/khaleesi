#include "ThreeWheelMotion.h"
#include "SDPArduino.h"

#define VISION_TIMEOUT 300 // ms - the time to considered that the vision is no longer seeing the robot
#define TIMSTEP 100        // ms - generate prediction every 100ms
double FORCE_DECOUPLING[3][3] = {{-.33, .58, .33}, {-.33, -.58, .33}, {.67, 0, .33}};

unsigned long currentTime;

/*
 * @param myX, myY, myHeading : the robot's current X,Y, and heading (wrt to
 *                            the global frame)
 * @param forceX, forceY : the speed in X and Y components (wrt global frame)
 *                          that robot should move in
 * @param rotation: the angle that the robot should tilt at the final position
 */
void gotoXY(double myX, double myY, double myHeading,
            double forceX, double forceY, double rotation)
{
    currentTime = millis();
}

void forwardKinematics();

void inverseKinematics();
