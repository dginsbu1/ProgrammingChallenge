#include <stddef.h>

int addCar(char* make, char* model, short year, long price, int uniqueID);
int setMaxMemory(size_t bytes);
struct Car* getCarById(int id);
int deleteCarById(int id);
int modifyCarById(int id, struct Car* myCar);
int getNumberOfCarsInMemory();
int getAmountOfUsedMemory();
int getNumberOfCarsOnDisk();
struct Car* getAllCarsInMemory();


typedef struct Car{
    char* make;//leave blank space
    char* model;//leave blank space
    short year;
    long price;
    int uniqueID;
    //int inMemory;
} Car;
//get rid off
Car *myCars;
