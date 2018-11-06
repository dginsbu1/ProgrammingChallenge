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

/* this function does NOT cause the cars on disk to displace those
 * that were already in memory. It uses separate memory to load them
 * and return them to the caller. THE CALLER MUST FREE THIS MEMORY
 * WHEN FINISHED WITH THESE CARS.
*/
struct Car* getAllCarsOnDisk();
typedef struct Car{
    char* make;//leave blank space
    char* model;//leave blank space
    short year;
    long price;
    int uniqueID;
    //int inMemory;
} Car;
