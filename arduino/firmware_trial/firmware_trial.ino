
#include <Wire.h>
#include <Arduino.h>
#include "SerialCommand.h"
#include "SDPArduino.h"
#include "Rotary.h"

#define OPADDR 0x5A
#define REGADDR 0x04

// MotorBoard Mapping
// 0 - KICKER LEFT
// 1 - KICKER RIGHT
// 2 -   ---
// 3 - FRONT LEFT (-1x)
// 4 - BACK
// 5 - FRONT RIGHT

// Wheels
// The port for the motor board and the encoder port is the same.
#define FRONTLEFT 3
#define FRONTRIGHT 5
#define BACK 4

// Kickers
#define KICKLEFT 0
#define KICKRIGHT 1

// Encoders
#define ENCODER 5

// Variables
SerialCommand sCmd;
boolean debug = true;
boolean kickerStatus = 0;

void setup()
{
  Wire.begin();
  SDPsetup();

  sCmd.addCommand("ping", pingMethod);

  // GENERAL
  sCmd.addCommand("h", completeHalt);

  // MOTION
  sCmd.addCommand("f", dontMove);
  sCmd.addCommand("r", rationalMotors);
  sCmd.addCommand("mm", manualMoveMotor);
  //sCmd.addCommand("goto", gotoXY);

  // KICKING and DRIBBLING
  sCmd.addCommand("sk", stopKicker);
  sCmd.addCommand("dk", dribblerKick);
  sCmd.addCommand("kick", kicker);

  Serial.println("READY");
  Serial.println("I am Khaleesi");

  if (debug)
  {
    debug();
  }
}

void debug()
{

  Serial.println("DEBUG MODE ON");
  Serial.println("Forward");
  motorForward(FRONTLEFT, 80);
  motorForward(FRONTRIGHT, 80);
  motorForward(BACK, 80);
  delay(1000);
  dontMove();

  Serial.println("Backwards");
  motorBackward(FRONTLEFT, 80);
  motorBackward(FRONTRIGHT, 80);
  motorBackward(BACK, 80);
  delay(1000);
  dontMove();
  motorAllStop();
}

void loop()
{
  sCmd.readSerial();
}

void pingMethod()
{
  Serial.println("pang");
}

void completeHalt()
{
  motorAllStop();
  motorAllStop();
  motorAllStop();
}

//  ====================================
//      WHEELS
//      - dontMove
//      - moveMotor
//      - rationalMotors
//      - manualMoveMotor
//  ====================================

void dontMove()
{
  // stop the three wheels
  motorStop(FRONTLEFT);
  motorStop(BACK);
  motorStop(FRONTRIGHT);
}

void moveMotor(int motor, int power)
{
  // Function to move each individual wheel indepdent of the signed of
  // the power of the motor.
  // Note negative sign in the last line
  if (power == 0)
  {
    motorStop(motor);
  }
  else if (power > 0)
  {
    motorForward(motor, power);
  }
  else
  {
    motorBackward(motor, -power);
  }
}

void rationalMotors()
{
  // positive power causes the motor to go COUNTER clockwise
  // note that FRONTLEFT comes first
  // e.g. r 100 100 100 - spin CCW
  int frontLeft = atoi(sCmd.next());
  int frontRight = atoi(sCmd.next());
  int back = atoi(sCmd.next());

  // changed the polarity here .due to the structure of the robot.
  // software is hence *idiot* proof and does not require any flipping
  // of signs in the command
  moveMotor(FRONTLEFT, -frontLeft);
  moveMotor(FRONTRIGHT, frontRight);
  moveMotor(BACK, back);
}

// For debugging purposes
void manualMoveMotor()
{
  int motor = atoi(sCmd.next());
  int power = atoi(sCmd.next());
  moveMotor(motor, power);
}

//  ====================================
//      DRIBBLER AND KICKERS
//      - stopKicker
//      - resetKicker
//      - dribblerKick  // naming is a legacy issue
//      - kicker
//  ====================================

void stopKicker()
{
  motorStop(KICKLEFT);
  motorStop(KICKRIGHT);
}

void resetKicker()
{
  // reset the dribbler and kicker to the desired position
}

// for debugging the kicker at an input speed
// Kick for 200ms and then halt.
void kick()
{
  int kickPower = atoi(sCmd.next());
  moveMotor(KICKLEFT, kickPower);
  moveMotor(KICKRIGHT, kickPower);
  delay(200);
  stopKicker();
}

// ======================================================
