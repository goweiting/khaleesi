// ROTARY . H
// ======================================================================

#ifndef HEADER_ROTARY
#define HEADER_ROTARY

#define NumPorts 3
#define RotaryCount 6
double* getCurrentSpeed(int interval);
void resetMotorPositions(void);
void pollFromAll(void);
void printAllPos(void);
void poll101(void);
void speed101(void);
#endif

// ======================================================================

