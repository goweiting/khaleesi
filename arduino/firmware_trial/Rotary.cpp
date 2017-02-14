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
_positions[i] += (int) Wire.read(); // Must cast to signed 80bit type
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
memset(array, 0, sizeof(int)*3);
}

void resetDoubleArray(double array[]){
  memset(array, 0, sizeof(double) * 3);
}

void resetAll(){
  resetMotorPositions(_positions);
  resetMotorPositions(positions);
}

double findMaxSpeed(double array[]){
  double largest = abs(array[0]);
  for (int i=1; i<NumPorts; i++){
    if (largest<abs(array[i])){
      largest = abs(array[i]);
    }
  }
  return largest;
}


// ======================================================================
void pollWheels()
{
// poll all the ports for their current positions;
Wire.requestFrom(EncoderBoardAddr, RotaryCount);
int i;
for (i = 0; i<NumPorts; i++)
{
positions[i] += (int) Wire.read(); // Must cast to signed 80bit type
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
resetAll();
resetDoubleArray(currentSpeed);
int lastKnownPositions[3] = {positions[0], positions[1], positions[2]};
delay(interval);
updateWheelPositions();
// get the instantaneous speed
currentSpeed[0] = (double) -1 * (positions[0] - lastKnownPositions[0]) / interval;    // FL
currentSpeed[1] = (double) (positions[1] - lastKnownPositions[1]) / interval;    // FR
currentSpeed[2] = (double) (positions[2] - lastKnownPositions[2]) / interval;    // Back

return currentSpeed;
}

double * normaliseSpeed(double currentSpeed[], double base){
  double largest = findMaxSpeed(currentSpeed);

  currentSpeed[0] = (currentSpeed[0] / largest) * base;
  currentSpeed[1] = (currentSpeed[1] / largest) * base;
  currentSpeed[2] = (currentSpeed[2] / largest) * base;
  
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
static int interval = 200;
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

