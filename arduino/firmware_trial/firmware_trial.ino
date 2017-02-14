
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

// Variables
SerialCommand sCmd;
boolean DEBUG = 0;
boolean kickerStatus = 0;

// Encoders

void setup()
{
  Wire.begin();
  sCmd.addCommand("ping", pingMethod);

  // MOTION
  sCmd.addCommand("debug", debug);
  sCmd.addCommand("f", dontMove);
  sCmd.addCommand("r", rationalMotors);
  sCmd.addCommand("mm", manualMoveMotor);
  sCmd.addCommand("md", monitoredDrive);
  //sCmd.addCommand("goto", gotoXY);

  // KICKERS
  sCmd.addCommand("sk", stopKicker);
  //sCmd.addCommand("dk", dribblerKick);
  //sCmd.addCommand("kick", kicker);
  
  // ROTARY
  sCmd.addCommand("poll101", poll101);
  sCmd.addCommand("speed101", speed101);  
  


  SDPsetup();
  resetMotorPositions(); // reset the encoders
  Serial.println("READY");
  Serial.println("I am Khaleesi");

}

void debug()
{

  Serial.println("DEBUG MODE ON");
  Serial.println("Counter clockwise");
  motorBackward(FRONTLEFT, 80);
  motorForward(FRONTRIGHT, 80);
  motorForward(BACK, 80);
  delay(1000);
  dontMove();

  Serial.println("Clockwise");
  motorForward(FRONTLEFT, 80);
  motorBackward(FRONTRIGHT, 80);
  motorBackward(BACK, 80);
  delay(1000);
  dontMove();
  motorAllStop();
  Serial.println("Exiting Debug...");
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
  Serial.println("comepletHalt");
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
  Serial.println("dontMove");
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


int pollinterval_drive = 200; // ms
void monitoredDrive()
{
  // Drive and monitored by the encoders
  int frontLeft = atoi(sCmd.next());
  int frontRight = atoi(sCmd.next());
  int back = atoi(sCmd.next());

  moveMotor(FRONTLEFT, -frontLeft);
  moveMotor(FRONTRIGHT, frontRight);
  moveMotor(BACK, back);
  double *currentSpeed = getCurrentSpeed(pollinterval_drive);
  int sumSpeed_output = abs(currentSpeed[0]) + abs(currentSpeed[1]) + abs(currentSpeed[2]);
  int sumSpeed_input = abs(frontLeft) + abs(frontRight) + abs(back);

  Serial.print("<<FL,FR,B>> "); 
  double tolerant = 0.5;
  int interval = 10; // decrement by this amount
  // LEFT:
  int frontLeft_input = abs(frontLeft) / sumSpeed_input;
  int frontLeft_output = abs(currentSpeed[0]) / sumSpeed_output;
  int delta = frontLeft_input - frontLeft_output;
  if (abs(delta) > tolerant)
  {
    int new_left = -frontLeft - (interval * delta);
    moveMotor(FRONTLEFT, new_left);
    Serial.print(new_left);
  }

  // RIGHT:
  int frontRight_input = abs(frontRight) / sumSpeed_input;
  int frontRight_output = abs(currentSpeed[1]) / sumSpeed_output;
  delta = frontRight_input - frontRight_output;
  if (abs(delta) > tolerant)
  {
    int new_right = frontRight - (interval * delta);
    moveMotor(FRONTRIGHT, new_right);
    Serial.print(new_right);
  }

  // BACK:
  int back_input = abs(back) / sumSpeed_input;
  int back_output = abs(currentSpeed[2]) / sumSpeed_output;
  delta = back_input - back_output;
  if (abs(delta) > tolerant) 
  {
    int new_back = back - (interval * delta);
    moveMotor(BACK, new_back);
    Serial.print(new_back);
  }

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
