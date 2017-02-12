
#include <Wire.h>
#include <Arduino.h>
#include "SerialCommand.h"
#include "SDPArduino.h"

#define DEBUG 1 // change to 0 to off debug mode

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
#define FRONTLEFT 3
#define FRONTRIGHT 5
#define BACK 4

// Kickers
#define DRIBBLER 2
#define KICKERS 0
#define KICKERS2 1


SerialCommand sCmd;
//boolean kickerStatus = 0;

void setup(){
  Wire.begin();
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

  SDPsetup();

  Serial.println("READY");
  Serial.println("I am Khaleesi");

}

void debug(){

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

void loop(){
  sCmd.readSerial();
}

void pingMethod(){
  Serial.println("pang");
}

void completeHalt(){
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

void dontMove(){
  // stop the three wheels
  motorStop(FRONTLEFT);
  motorStop(BACK);
  motorStop(FRONTRIGHT);
}

void moveMotor(int motor, int power) {
  // Function to move each individual wheel indepdent of the signed of
  // the power of the motor.
  // Note negative sign in the last line
  if (power == 0) { 
    motorStop(motor); 
  }
  else if (power > 0) { 
    motorForward(motor, power); 
  }
  else { 
    motorBackward(motor, -power); 
  }
}


void rationalMotors(){
  // positive power causes the motor to go COUNTER clockwise
  // note that FRONTLEFT comes first
  // e.g. r 100 100 100 - spin CCW
  int frontLeft = atoi(sCmd.next());
  int frontRight = atoi(sCmd.next());
  int back  = atoi(sCmd.next());

  // changed the polarity here .due to the structure of the robot.
  // software is hence *idiot* proof and does not require any flipping
  // of signs in the command
  moveMotor(FRONTLEFT, -frontLeft);
  moveMotor(FRONTRIGHT, frontRight);
  moveMotor(BACK, back);
}

// For debugging purposes
void manualMoveMotor(){
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

void stopKicker(){
  motorStop(KICKERS);
  motorStop(KICKERS2);
}

void resetKicker(){
  // reset the dribbler and kicker to the desired position
}

void dribblerKick(){
  // first integer is the power for dribbler
  // second integer is the power for kicker
  int dribbler = atoi(sCmd.next());
  int kickPower = atoi(sCmd.next());
  moveMotor(DRIBBLER, dribbler);
  moveMotor(KICKERS, kickPower);
  moveMotor(KICKERS2, kickPower);
}


void kicker(){
  // 3 states for kicker module = {0, 1, -1}
  // state 0 = halt KICKERS
  // state 1 = ??
  // state -1 = ??
  // do we really use this?

  int state = atoi(sCmd.next());
  if(state == 0) {
    motorStop(KICKERS);
  } 
  else if (state == 1) {
//    Serial.println(positions[0] % 40);
//    Serial.println("Starting From: ");
//    motorForward(KICKERS, 100);
//    kickerStatus = 1;
//  } 
//  else {
//    motorBackward(KICKERS, 100);
//    kickerStatus = -1;
  }
}

// ======================================================



