// ROTARY . H
// ======================================================================

#ifndef HEADER_ROTARY
#define HEADER_ROTARY

#define NumPorts 3
#define RotaryCount 6

// generic
void pollFromAll(void);
void printAllPos(void);
void resetMotorPositions(int8_t array[], int len);
void resetDoubleArray(double array[]);
void resetAll(void);
double findMaxSpeed(double array[]);
// wheels specific
void updateWheelPositions(void);
double *getCurrentSpeed(int interval);

// wheels debugging tools
void poll101(void);
void speed101(void);
double *normaliseSpeed(double currentSpeed[], double base);

#endif

// ======================================================================
