// ROTARY . H
// ======================================================================

#ifndef HEADER_ROTARY
#define HEADER_ROTARY

#define NumPorts 3
#define RotaryCount 6


// generic
void pollFromAll(void);
void printAllPos(void);
void resetMotorPositions(int array[]);
void resetAll(void);
void resetDoubleArray(double array[]);
// wheels specific
void updateWheelPositions(void);
double* getCurrentSpeed(int interval);

// wheels debugging tools
void poll101(void);
void speed101(void);

#endif

// ======================================================================

