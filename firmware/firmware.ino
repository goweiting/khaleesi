#include "SerialCommand.h"
#include "SDPArduino.h"
#include <Wire.h>
#include <Arduino.h>
#include <I2CPort.h>

// Wheels
#define FRONTLEFT 3
#define FRONTRIGHT 5
#define BACK 4

// Kickers
#define DRIBBLER 0
#define KICKERS 1
#define KICKERS2 2

#define OPADDR 0x5A
#define REGADDR 0x04

#define KICKERDELAY 10

boolean requestStopKick = 0;
boolean kickerStatus = 0;

int zeroPosition;


#define ROTARY_SLAVE_ADDRESS 5
#define ROTARY_COUNT 6
#define PRINT_DELAY 200

// Initial motor position is 0.
int positions[ROTARY_COUNT] = {0};

int run = 0;

SerialCommand sCmd;


// =====================================================
void setup(){
        Wire.begin();
        sCmd.addCommand("f", dontMove);
        sCmd.addCommand("h", completeHalt);
        sCmd.addCommand("r", rationalMotors);
        sCmd.addCommand("dk", dribblerKick);
        sCmd.addCommand("ping", pingMethod);
        sCmd.addCommand("kick", kicker);
        sCmd.addCommand("mux", muxTest);
        SDPsetup();
}

void loop(){
        sCmd.readSerial();
}

void pingMethod(){
        Serial.println("pang");
}

void completeHalt(){
        motorAllStop();
}

//  ====================================
//      WHEELS
//  ====================================

void dontMove(){
        // stop the three wheels
        motorStop(FRONTLEFT);
        motorStop(BACK);
        motorStop(FRONTRIGHT);
}

void moveMotor(int motor, int power) {
        if (power == 0) { motorStop(motor); }
        else if (power > 0) { motorForward(motor, power); }
        else { motorBackward(motor, -power); }
}


void rationalMotors(){
        // positive power causes the motor to go COUNTER clockwise
        // note that FRONTLEFT comes first
        int frontLeft = atoi(sCmd.next());
        int frontRight = atoi(sCmd.next());
        int back  = atoi(sCmd.next());

        // changed the polarity here. software should be idiot proof then
        moveMotor(FRONTLEFT, frontLeft);
        moveMotor(FRONTRIGHT, -frontRight);
        moveMotor(BACK, back);
}


//  ====================================
//      DRIBBLER AND KICKERS
//  ====================================

void stopKicker(){
        motorStop(KICKER);
}

void stopDribbler(){
    motorStop(DRIBBLER);
}

void dribblerKick(){
        // first one is the power, second one is the power for kicker
        int dribbler = atoi(sCmd.next());
        int kickPower = atoi(sCmd.next());
        moveMotor(DRIBBLER, dribbler);
        moveMotor(KICKERS, kickPower);
        moveMotor(KICKERS2, -kickPower);
}


void kicker(){
        int type = atoi(sCmd.next());
        if(type == 0) {
                motorStop(KICKERS);
        } else if (type == 1) {
                Serial.print("Starting From: ");
                Serial.println(positions[0] % 40);
                motorForward(KICKERS, 100);
                kickerStatus = 1;
        } else {
                motorBackward(KICKERS, 100);
                kickerStatus = -1;
        }
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
