// ROTARY . CPP
// Making use of the encoders
// ======================================================================

#include <Wire.h>
#include <Arduino.h>

// ======================================================================
#define EncoderBoardAddr 5
#define RotaryCount 6
#define NumPorts 3
#define TolerableDelay 200 // ms
int PollInterval =  200;   // ms

// locations of the encoders in the port.
#define FRONTLEFT_enc 0
#define FRONTRIGHT_enc 1
#define BACKWHEEL_enc 2

// Constants
// Need to flip the sign for FRONTLEFT_enc!
int currentPositions[NumPorts] = {0};
int positions[RotaryCount] = {0}; // for DEBUG

// ======================================================================
// SOME FUNCTIONS FOR SANITY CHECKING



void pollFromAll()
{
// poll all the ports for their current positions;
Wire.requestFrom(EncoderBoardAddr, RotaryCount);
int i;
for (i = 0; i < RotaryCount; i++)
{
positions[i] += (int8_t)Wire.read(); // Must cast to signed 80bit type
}
}

void printAllPos()
{
Serial.print("Motor positions: ");
for (int i = 0; i < RotaryCount; i++)
{
Serial.print(positions[i]);
Serial.print(' ');
}
Serial.println();
} 

void poll101(){
// poll and print for 10 seconds
int time = millis();
int runtime = 0;
Serial.print(runtime); //debug
Serial.print("  ");

while (runtime < 1000){
pollFromAll();
printAllPos();
delay(200);
runtime = runtime + millis() - time;
time = millis();
Serial.print(time); //debug
 Serial.print("  ");
}
}

void resetMotorPositions(){
pollFromAll();
memset(currentPositions, 0, sizeof(currentPositions));
memset(positions, 0, sizeof(positions));
}

// ======================================================================
// Miscellanous Functionalities
void updateMotorPositions()
{
pollFromAll();
currentPositions[0] = -1 * positions[FRONTLEFT_enc]; // FRONTLEFT_enc flips sign here
currentPositions[1] = positions[FRONTRIGHT_enc];
currentPositions[2] = positions[BACKWHEEL_enc];
// debug
Serial.print("       updateMotorPositions:");
Serial.print(currentPositions[0]);
Serial.print(" ");
Serial.print(currentPositions[1]);
Serial.print(" ");
Serial.print(currentPositions[2]);
Serial.println();
}

double * getCurrentSpeed(int interval)
{
// poll the encoders every fixed poll interval (200ms)
// and then return the speed.
// Speed = dx / dt = (currentPosition - lastKnownPosition ) / 200
// interval is the time between each polling

// double lastKnownPositions[3] = {currentPositions[0], currentPositions[1], currentPositions[2]}; // this will give ACCELERATION!!
double lastKnownPositions[3] = {0.0};
resetMotorPositions();
delay(interval);
updateMotorPositions();

double currentSpeed[NumPorts] = {0.0}; // starts from halt
// get the instantaneous speed
currentSpeed[0] = (currentPositions[0] - lastKnownPositions[0]) / interval;    // FL
currentSpeed[1] = (currentPositions[1] - lastKnownPositions[1]) / interval;    // FR
currentSpeed[2] = (currentPositions[2] - lastKnownPositions[2]) / interval;    // Back

return currentSpeed;
}


void speed101(){
// find instantaneous speed every 1000ms
// Note that updateMotorPositions will naturally flip the signs for the FrontLeft motor.
resetMotorPositions();
Serial.println("Polling speed! FL FR B");
int time = millis();
int runtime = 0;
Serial.println(runtime);

while (runtime < 10000){
double * cs = getCurrentSpeed(PollInterval);
Serial.print("     Speed ");
for (int i=0; i<NumPorts; i++){
Serial.print(" ");
Serial.print(cs[i]);
}
delay(1000); // poll every second

runtime = runtime + millis() - time;
time = millis();
Serial.println();
Serial.println(runtime); //debug

}
}


// ======================================================================

