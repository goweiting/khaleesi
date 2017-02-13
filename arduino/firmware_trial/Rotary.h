// ROTARY . H
// ======================================================================

#ifndef HEADER_ROTARY
#define HEADER_ROTARY

#define NumPorts
int currentPositions[NumPorts];
int positions[RotaryCount];
int currentSpeed[NumPorts];
int getCurrentSpeed(void);
void getCurrentSpeed(void);
void pollFromAll(void);
void printAllPos(void);
#endif

// ======================================================================