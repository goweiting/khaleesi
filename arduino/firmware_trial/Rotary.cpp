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
double currentSpeed[NumPorts] = {0.0}; // starts from halt
int _positions[RotaryCount] = {0}; // for ALL the ports
int positions[NumPorts] = {0}; // for ports of interest (listed above) [FL, FR, BACK]
// ======================================================================
// SOME FUNCTIONS FOR SANITY CHECKING



void pollFromAll()
{
// poll all the ports for their current positions;
Wire.requestFrom(EncoderBoardAddr, RotaryCount);
int i;
for (i = 0; i < RotaryCount; i++)
{
_positions[i] += (int8_t) Wire.read(); // Must cast to signed 80bit type
}
}

void printAllPos()
{
Serial.print("Motor positions: ");
for (int i = 0; i < RotaryCount; i++)
{
Serial.print(_positions[i]);
Serial.print(' ');
}
Serial.println();
} 


void resetMotorPositions(int array[]){
memset(array, 0, sizeof(int)*sizeof(array));
}

void resetDoubleArray(double array[]){
  memset(array, 0, sizeof(double) * sizeof(array));
}

void resetAll(){
  resetMotorPositions(_positions);
  resetMotorPositions(positions);
}

// ======================================================================
void pollWheels()
{
// poll all the ports for their current positions;
Wire.requestFrom(EncoderBoardAddr, RotaryCount);
int i;
for (i = 0; i<NumPorts; i++)
{
positions[i] += (int8_t) Wire.read(); // Must cast to signed 80bit type
}
}

void updateWheelPositions()
{
pollWheels();
// debug output
Serial.print("       updateWheelPositions:");
Serial.print(positions[0]);
Serial.print(" ");
Serial.print(positions[1]);
Serial.print(" ");
Serial.print(positions[2]);
Serial.println();
}

double * getCurrentSpeed(int interval)
{
// poll the encoders every fixed poll interval (e.g. 200ms)
// return an array of speed - representing the [FL , FR , BACK] wheels.
// Speed = dx / dt = (currentPosition - lastKnownPosition ) / 200
// interval is the time between each polling
resetDoubleArray(currentSpeed);
int lastKnownPositions[3] = {positions[0], positions[1], positions[2]};
delay(interval);
updateWheelPositions();
// get the instantaneous speed
currentSpeed[0] = (double) (positions[0] - lastKnownPositions[0]) / interval;    // FL
currentSpeed[1] = (double) (positions[1] - lastKnownPositions[1]) / interval;    // FR
currentSpeed[2] = (double) (positions[2] - lastKnownPositions[2]) / interval;    // Back

return currentSpeed;
}


void poll101(){
// poll and print for 10 seconds N.B. LEFT WHEEL NOT FLIPPERD
resetAll();
int time = millis();
int runtime = 0;
Serial.print(runtime); //debug
Serial.print("  ");

while (runtime < 1000){
pollWheels();
Serial.print("Motor positions: ");
for (int i = 0; i < NumPorts; i++)
{
Serial.print(positions[i]);
Serial.print(' ');
}
Serial.println();
delay(PollInterval);
runtime += millis() - time;
time = millis();
Serial.print(runtime); //debug
 Serial.print("  ");
}
}

void speed101(){
// find instantaneous speed every 1000ms
static int interval = 1000;
Serial.println("Polling speed!");
resetAll();
// time
int time = millis();
int runtime = 0;
Serial.println(runtime);

while (runtime < 10000){ // get some readings
double * cs = getCurrentSpeed(interval);
Serial.print("     Speed ");
for (int i=0; i<NumPorts; i++){
Serial.print(" ");
Serial.print(cs[i]);
}

runtime = runtime + millis() - time;
time = millis();
Serial.println();
Serial.println(runtime); //debug
}

}




// ======================================================================

