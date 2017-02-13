// ROTARY . CPP
// Making use of the encoders
// ======================================================================

#include <Wire.h>

// ======================================================================
#define EncoderBoardAddr 5
#define RotaryCount 6
#define NumPorts 3
#define TolerableDelay 200 // ms
#define PollInterval 200   // ms

// locations of the encoders in the port.
#define FRONTLEFT_enc 1
#define FRONTRIGHT_enc 2
#define BACKWHEEL_enc 3

// Constants
int ports[NumPorts] = {FRONTRIGHT_enc, FRONTLEFT_enc, BACKWHEEL_enc};
int currentPositions[NumPorts] = {0};
int positions[RotaryCount] = {0}; // for DEBUG
int currentSpeed[NumPorts] = {0};

// ======================================================================
// Miscellanous Functionalities
int getCurrentSpeed()
{
  // poll the encoders every fixed poll interval (200ms)
  // and then return the speed.
  // Speed = dx / dt = (currentPosition - lastKnownPosition ) / 200

  int lastKnownPosition[3] = currentPositions;
  delay(PollInterval); // wait for 200ms to get new positions
  updateMotorPositions();

  // get the instantaneous speed
  for (int i = 0; i < 3; i++)
  {
    int dx = currentPositions[i] - lastKnownPosition[i];
    currentSpeed[i] = dx / 200;
  }
  return currentSpeed;
}

void updateMotorPositions()
{
  pollFromAll();
  for (int i = 0; i < NumPorts; i++)
  {
    currentPositions[i] = positions(ports[i]);
  }
}

// ======================================================================
// SOME FUNCTIONS FOR SANITY CHECKING

void pollFromAll()
{
  // poll all the ports for their current positions;
  Wire.requestFrom(EncoderBoardAddr, RotaryCount);

  for (int i = 0; i < RotaryCount; i++)
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

// ======================================================================
