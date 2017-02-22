// ROTARY . CPP
// Making use of the encoders
// ======================================================================

#include <Wire.h>
#include <Arduino.h>

// ======================================================================
#define EncoderBoardAddr 5
#define RotaryCount 6
#define NumPorts 3

// locations of the encoders in the port.
#define FRONTLEFT_enc 0
#define FRONTRIGHT_enc 1
#define BACKWHEEL_enc 2

// Constants
#define TolerableDelay 200 // ms
int PollInterval = 200;    // ms

// Need to flip the sign for FRONTLEFT_enc!
double currentSpeed[NumPorts] = {0.0}; // starts from halt
int _positions[RotaryCount] = {0};     // for ALL the ports
int positions[NumPorts] = {0};         // for ports of interest (listed above) [FL, FR, BACK]
// ======================================================================
// SOME FUNCTIONS FOR SANITY CHECKING

/**
  *   poll all the ports for their current positions
  */
void pollFromAll()
{
  Wire.requestFrom(EncoderBoardAddr, RotaryCount);
  int i;
  for (i = 0; i < RotaryCount; i++)
  {
    _positions[i] += (int8_t) Wire.read(); // Must cast to signed 8bit type
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

/**
  *   Returns the maximum absolute speed in the current speed array
  */
double findMaxSpeed(double array[])
{
  double largest = abs(array[0]);
  for (int i = 1; i < NumPorts; i++)
  {
    if (largest < abs(array[i]))
    {
      largest = abs(array[i]);
    }
  }
  return largest;
}

/**
  *   Misc reset functions for the lazy soul
  */
void resetMotorPositions(int array[], int len)
{
  for (int i=0; i<len; i++){
    array[i] = 0;
  }
}

void resetDoubleArray(double array[])
{
  for (int i=0; i<NumPorts; i++){
    array[i] = (double) 0;
  }
}

void resetAll()
{
  memset(_positions, 0, RotaryCount * sizeof(int));
  memset(positions, 0, NumPorts * sizeof(int));
  memset(currentSpeed, 0, sizeof(double));
//  resetMotorPositions(_positions,RotaryCount);
//  resetMotorPositions(positions, NumPorts);
//  resetDoubleArray(currentSpeed);
}

// ======================================================================
/**
  *   Find the current encoder positions for all the ports that we are 
  *   interested in - i.e. the FRONTLEFT, FRONTRIGHT, BACK
  */
void pollWheels()
{
  // poll all the ports for their current positions;
  Wire.requestFrom(EncoderBoardAddr, RotaryCount);
  positions[0] -= (int8_t) Wire.read(); // polarity switch is here!
  positions[1] += (int8_t) Wire.read();
  positions[2] += (int8_t) Wire.read();
}

void updateWheelPositions()
{
    // debug output
  Serial.print("currentpos:  ");
  Serial.print(positions[0]);
  Serial.print(" ");
  Serial.print(positions[1]);
  Serial.print(" ");
  Serial.print(positions[2]);
  Serial.println();
  pollWheels();
  Serial.print("polled:  ");
  Serial.print(positions[0]);
  Serial.print(" ");
  Serial.print(positions[1]);
  Serial.print(" ");
  Serial.print(positions[2]);
  Serial.println();

}

/**
  * poll the encoders every fixed poll interval (e.g. 200ms)
  * return an array of speed - represupdateWheelPositionsenting the [FL , FR , BACK] wheels.
  * Speed = dx / dt = (currentPosition - lastKnownPosition ) / interval
  * @param interval - time between each poll for the wheel encoders
  */
double * getCurrentSpeed(int interval)
{
  resetAll();
  delay(interval);
  // pollWheels();
  updateWheelPositions(); // DEBUG
  // get the instantaneous speed

  currentSpeed[0] = (double) (positions[0] / interval);      // FL
  currentSpeed[1] = (double) (positions[1] / interval);      // FR
  currentSpeed[2] = (double) (positions[2] / interval);      // Back

  return currentSpeed;
}

double *normaliseSpeed(double* currentSpeed, double base)
{
  double largest = findMaxSpeed(currentSpeed);

  currentSpeed[0] = (currentSpeed[0] / largest) * base;
  currentSpeed[1] = (currentSpeed[1] / largest) * base;
  currentSpeed[2] = (currentSpeed[2] / largest) * base;

  return currentSpeed;
}

// ======================================================================
// Functions for testing
void poll101()
{
  // poll and print for 10 seconds N.B. LEFT WHEEL NOT FLIPPERD
  resetAll();
  int time = millis();
  int runtime = 0;
  Serial.print(runtime); //debug
  Serial.print("  ");

  while (runtime < 5000)
  {
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

void speed101()
{
  // find instantaneous speed every 1000ms
  static int interval = 200;
  Serial.println("Polling speed!");
  resetAll();
  // time
  int time = millis();
  int runtime = 0;
  Serial.println(runtime);

  while (runtime < 10000)
  { // get some readings
    double *cs = getCurrentSpeed(interval);
    Serial.print("     Speed ");
    for (int i = 0; i < NumPorts; i++)
    {
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
