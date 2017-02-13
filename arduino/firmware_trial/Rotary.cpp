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
// Need to flip the sign for FRONTLEFT_enc!
int currentPositions[NumPorts] = {0};
int positions[RotaryCount] = {0}; // for DEBUG
int currentSpeed[NumPorts] = {0}; // starts from halt

// ======================================================================
// Miscellanous Functionalities
void getCurrentSpeed()
{
  // poll the encoders every fixed poll interval (200ms)
  // and then return the speed.
  // Speed = dx / dt = (currentPosition - lastKnownPosition ) / 200

  int lastKnownPositions[3] = {currentPositions[0], currentPositions[1], currentPositions[2]};
  delay(PollInterval); // wait for 200ms to get new positions
  updateMotorPositions();

  // get the instantaneous speed
  currentSpeed[0] = (currentPositions[0] - lastKnownPositions[0]) / 200;
  currentSpeed[1] = (currentPositions[1] - lastKnownPositions[1]) / 200;
  currentSpeed[2] = (currentPositions[2] - lastKnownPositions[2]) / 200;
}

void updateMotorPositions()
{
  pollFromAll();
  currentPositions[0] = -1 * positions[FRONTLEFT_enc]; // FRONTLEFT_enc flips sign here
  currentPositions[1] = positions[FRONTRIGHT_enc];
  currentPositions[2] = positions[BACKWHEEL_enc];
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
