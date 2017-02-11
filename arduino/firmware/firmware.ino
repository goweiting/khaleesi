#include "SerialCommand.h"
#include "SDPArduino.h"
#include <Wire.h>
#include <Arduino.h>
#include <I2CPort.h>

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

#define OPADDR 0x5A
#define REGADDR 0x04

#define KICKERDELAY 10

boolean requestStopKick = 0;
boolean kickerStatus = 0;

int zeroPosition;


// Encoders
#define ROTARY_SLAVE_ADDRESS 5
#define ROTARY_COUNT 6
#define PRINT_DELAY 200

int positions[ROTARY_COUNT] = {0}; // Initial motor position is 0.

int run = 0;

SerialCommand sCmd;


// =====================================================
void setup(){
        Wire.begin();
        sCmd.addCommand("ping", pingMethod);

        // GENERAL
        sCmd.addCommand("h", completeHalt);

        // MOTION
        sCmd.addCommand("f", dontMove);
        sCmd.addCommand("r", rationalMotors);
        sCmd.addCommand("mm", manualMoveMotor);
        sCmd.addCommand("goto", gotoXY);

        // KICKING and DRIBBLING
        sCmd.addCommand("sk", stopKicker);
        sCmd.addCommand("dk", dribblerKick);
        sCmd.addCommand("kick", kicker);

        SDPsetup();
        Serial.println(F("I am Khaleesi"));
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
        if (power == 0) { motorStop(motor); }
        else if (power > 0) { motorForward(motor, power); }
        else { motorBackward(motor, -power); }
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
        } else if (state == 1) {
                Serial.println(positions[0] % 40);
                Serial.println("Starting From: ");
                motorForward(KICKERS, 100);
                kickerStatus = 1;
        } else {
                motorBackward(KICKERS, 100);
                kickerStatus = -1;
        }
}


//  ====================================
//      SENSORS and ENCODERS
//  ====================================
void updateMotorPositions() {
        // Request motor position deltas from rotary slave board
        Wire.requestFrom(ROTARY_SLAVE_ADDRESS, ROTARY_COUNT);

        // Update the recorded motor positions
        for (int i = 0; i < ROTARY_COUNT; i++) {
                positions[i] += (int8_t) Wire.read(); // Must cast to signed 8-bit type
        }
}

void printMotorPositions() {
        Serial.print("Motor positions: ");
        for (int i = 0; i < ROTARY_COUNT; i++) {
                Serial.print(positions[i]);
                Serial.print(' ');
        }
        Serial.println();
        delay(PRINT_DELAY); // Delay to avoid flooding serial out
}

//  ====================================
//      MISC
//  ====================================
void printMotorPositions() {
        Serial.print("Motor positions: ");
        for (int i = 0; i < ROTARY_COUNT; i++) {
                Serial.print(positions[i]);
                Serial.print(' ');
        }
        Serial.println();
        delay(PRINT_DELAY); // Delay to avoid flooding serial out
}




// ======================================================
// we did not use this! this is part of the original code in FRED.
// See moveMotor instead!
void motorControl(int motor, int power){
        if(power == 0) {
                Wire.beginTransmission(OPADDR);
                Wire.write(motor);
                Wire.write(0);
                Wire.endTransmission();
        } else if(power > 0) {
                Wire.beginTransmission(OPADDR);
                Wire.write(motor);
                Wire.write(1);
                Wire.endTransmission();
                Wire.beginTransmission(OPADDR);
                Wire.write(motor + 1);
                Wire.write(power);
                Wire.endTransmission();
        } else {
                Wire.beginTransmission(OPADDR);
                Wire.write(motor);
                Wire.write(2);
                Wire.endTransmission();
                Wire.beginTransmission(OPADDR);
                Wire.write(motor + 1);
                Wire.write(-power); // note change of sign
                Wire.endTransmission();
        }
}


void muxTest(){
        int motor = atoi(sCmd.next());
        int dir  = atoi(sCmd.next());
        int pow  = atoi(sCmd.next());
        Wire.beginTransmission(OPADDR);
        Wire.write(motor);
        Wire.write(dir);
        Serial.println(Wire.endTransmission());
        Wire.beginTransmission(OPADDR);
        Wire.write(motor+1);
        Wire.write(pow);
        Serial.println(Wire.endTransmission());
        delay(2000);
        Wire.beginTransmission(OPADDR);
        Wire.write(motor);
        Wire.write(0);
        Serial.println(Wire.endTransmission());
}
// ======================================================
