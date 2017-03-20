#include <Servo.h>
#include "SDPArduino.h"
#include "SerialCommand.h"
#include <Wire.h>
#include <Arduino.h>
#include <I2CPort.h>

#define FRONT 5
#define RIGHT 3
#define BACK 4
#define LEFT 2
#define KICKER 0

#define SERVO 9
Servo grabber;


// serial monitor
SerialCommand sCmd;

//time kicker started
boolean kicking = 0;
int kickCounter = 0;

void motorControl(int motor, int power) {
  if(power == 0) {
    motorStop(motor);
  } else if(power > 0) {
    motorForward(motor,power);
  } else {
    motorBackward(motor,-power);
  }
}

void rationalMotors() {
  int front = atoi(sCmd.next());
  int back  = atoi(sCmd.next());
  int left  = atoi(sCmd.next());
  int right = atoi(sCmd.next());
  motorControl(FRONT, front);
  motorControl(BACK, -back);
  motorControl(LEFT, - left);
  motorControl(RIGHT, right);
}

void pingMethod() {
  Serial.println("pang");
}

void completeHalt() {
  motorAllStop();
  motorControl(FRONT, 0);
  motorControl(BACK, 0);
  motorControl(LEFT, 0);
  motorControl(RIGHT, 0);
  motorControl(KICKER, 0);
}

void startKick() {
  motorControl(KICKER, 100);
  Serial.println("STARTING KICKER");
  kicking = 1;
}

void stopKick() {
  Serial.println("STOPPING KICKER");
  motorControl(KICKER, 0);
  kicking = 0;
  kickCounter = 0;
}

void setup() {
  Wire.begin();
  sCmd.addCommand("h", completeHalt);
  sCmd.addCommand("f", completeHalt);
  sCmd.addCommand("r", rationalMotors);
  sCmd.addCommand("ping", pingMethod);
  sCmd.addCommand("k", startKick);
  sCmd.addCommand("g", grabTest);
  SDPsetup();
  helloWorld();
  completeHalt();
  grabber.attach(9);
  grabber.write(180);
}

void loop() {
  sCmd.readSerial();

  if (kicking && (millis()%3000==0)){
    kickCounter++;
    Serial.print("kickCounter @ ");
    Serial.println(kickCounter);
  }

  if (kickCounter==10){
    stopKick();
  }
}

void grabTest() {
  //0 //80
  int pos = atoi(sCmd.next());
  grabber.write(pos);
}
