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

#define LSENSOR A3
#define RSENSOR A0
#define WINCH 9
#define CATCH 5

// serial monitor
SerialCommand sCmd;

//time kicker started
boolean kicking = 0;
int kickCounter = 0;

//servos for wings
Servo winchServo;
Servo catchServo;
boolean canFlap = false;


//  ==================== MOTORS ==============================
void motorControl(int motor, int power) {
    if (power == 0) {
        motorStop(motor);
    } else if (power > 0) {
        motorForward(motor, power);
    } else {
        motorBackward(motor, -power);
    }
}

void rationalMotors() {
    int front = atoi(sCmd.next());
    int back = atoi(sCmd.next());
    int left = atoi(sCmd.next());
    int right = atoi(sCmd.next());

      motorControl(FRONT, front);
      motorControl(BACK, back);
      motorControl(LEFT, left);
      motorControl(RIGHT, right);
}

void completeHalt() {
    motorAllStop();
    motorControl(FRONT, 0);
    motorControl(BACK, 0);
    motorControl(LEFT, 0);
    motorControl(RIGHT, 0);
    motorControl(KICKER, 0);
}

//  ====================== COMMUNICATION ========================
void pingMethod() {
    Serial.println("pang");
}


//  ====================== KICKERS ===============================
void startKick() {
    if(kickCounter==0){
        motorControl(KICKER,-100);
        delay(200);
    }
      motorStop(BACK);
      motorStop(FRONT); 
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


//  ============================ WINGS ==========================
void winchTest() {
  int pos = atoi(sCmd.next());
  winchServo.write(pos);
  //180 winch in
  //0 winch out
}

void catchTest() {
  int pos = atoi(sCmd.next());
  catchServo.write(pos);
  // up 160
  // down 80
}

void toggleFlap() {
  canFlap = atoi(sCmd.next());
}

void flap() {
  //release catch
  catchServo.write(120);
  delay(200);
  //retract wings
  winchServo.write(180);
  delay(1000);
  //engage catch
  catchServo.write(40);
  delay(200);
  catchServo.write(55);
  //ready wings
  winchServo.write(0);
  delay(700);
}

void sensor() {
  float rRead = analogRead(RSENSOR);
  float lRead = analogRead(LSENSOR);

  if (min(rRead, lRead) < 500) {
      flap();
  }

  //Serial.println(min(rRead, lRead));
  //if (irReading > 0.9 && !kicking) {
    //startKick();
  //}
}


//  =========================== INIT ===============================
void setup() {
    Wire.begin();
    sCmd.addCommand("h", completeHalt);
    sCmd.addCommand("f", completeHalt);
    sCmd.addCommand("r", rationalMotors);
    sCmd.addCommand("ping", pingMethod);
    sCmd.addCommand("k", startKick);
    sCmd.addCommand("flap", flap);
    sCmd.addCommand("toggle", toggleFlap);
    SDPsetup();
    helloWorld();
    completeHalt();
    winchServo.attach(WINCH);
    catchServo.attach(CATCH);

    flap();
}

void loop() {
  sCmd.readSerial();
  if (canFlap) {
    sensor();
  }

  if (kicking && (millis()%1000==0)){
    kickCounter++;
    Serial.print("kickCounter @ ");
    Serial.println(kickCounter);
  }

  if (kickCounter==7){
    stopKick();
  }
}
