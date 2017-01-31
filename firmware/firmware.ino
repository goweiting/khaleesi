#include "SerialCommand.h"
#include "SDPArduino.h"
#include <Wire.h>
#include <Arduino.h>
#include <I2CPort.h>


//Kickers in back
//define FRONTLEFT 1
//define FRONTRIGHT 7
//define BACK 5
//define DRIBLER 3

//Kickers in front
#define FRONTLEFT 3
#define FRONTRIGHT 5
#define BACK 4

#define DRIBLER 0
#define KICKERS 1
#define KICKERS2 2

//#define SPEAKER 2

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


void loop(){
        sCmd.readSerial();


}

void moveMotor() {
        int motor = atoi(sCmd.next());
        int power = atoi(sCmd.next());
        if (power == 0) {
                motorForward(motor,0);
        }
        else if (power > 0) {
                motorForward(motor, power);
        }
        else {
                motorBackward(motor, -power);
        }
}


void dontMove(){
        motorControl(FRONTLEFT, 0);
        motorControl(BACK, 0);
        motorControl(DRIBLER, 0);
        motorControl(FRONTRIGHT, 0);

        motorForward(0,0);
        motorForward(1,0);
        motorForward(2,0);
        motorForward(3,0);
        motorForward(4,0);
        motorForward(5,0);
        motorForward(6,0);
}

void spinmotor(){
        int motor = atoi(sCmd.next());
        int power = atoi(sCmd.next());
        motorForward(motor, power);
}

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
                Wire.write(-power);
                Wire.endTransmission();
        }
}


void dribblerKick(){
  int dribbler = atoi(sCmd.next());
  int kickPower = atoi(sCmd.next());

  motorControl(DRIBLER, dribbler);
  motorControl(KICKERS, kickPower);
  motorControl(KICKERS2, -kickPower);
}

void rationalMotors(){
        int frontLeft = atoi(sCmd.next());
        int frontRight = atoi(sCmd.next());
        int back  = atoi(sCmd.next());

        motorControl(FRONTLEFT, -frontLeft);
        motorControl(FRONTRIGHT, -frontRight);
        motorControl(BACK, -back);
}

void pingMethod(){
        Serial.println("pang");
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

void completeHalt(){
        motorAllStop();
        motorControl(FRONTLEFT, 0);
        motorControl(BACK, 0);
        motorControl(DRIBLER, 0);
        motorControl(FRONTRIGHT, 0);
}


void setup(){
        Wire.begin();
        sCmd.addCommand("f", dontMove);
        sCmd.addCommand("h", completeHalt);
        sCmd.addCommand("motor", moveMotor);
        sCmd.addCommand("r", rationalMotors);
        sCmd.addCommand("dk", dribblerKick);
        sCmd.addCommand("ping", pingMethod);
        sCmd.addCommand("kick", kicker);
        sCmd.addCommand("mux", muxTest);
        SDPsetup();
        helloWorld();
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
