//#include "driver.c"
#include "dataStore.h"
#include <stddef.h>
#include <stdlib.h>
#include <stdio.h>

int currentCarsAvailable = 0;//how many cars are currently available in memory
int initialCarsAvailable = 0;//how many cars slots did we start with in memory
int carTotal = 0;//how many cars are there Total
int carPosition; //what spot in Car* am I at using pointer
int mallocCalled = 0;
Car *myCars;
int carSize = sizeof(Car);



//int addCar(char* make, char* model, short year, long price, int uniqueID);
//int setMaxMemory(size_t bytes);TODO DONE
//struct Car* getCarById(int id);//TODO DONE
//int deleteCarById(int id);
//int modifyCarById(int id, struct Car* myCar);
//int getNumberOfCarsInMemory();
//int getAmountOfUsedMemory();
//int getNumberOfCarsOnDisk();
//struct Car* getAllCarsInMemory();

//functions list for compiler
int addBuiltCar(Car* carPtr2);
int addBuiltCarToEnd(Car* car);
//int setMaxMemory(size_t bytes);
//int getNumberOfCarsInMemory();
int getPositionInMemory(int id);
//int deleteCarById(int id);
//int modifyCarById(int id, struct Car* myCar);
//int getAmountOfUsedMemory();
//int getNumberOfCarsOnDisk();
struct Car* getLastCarFromDisk();
struct Car* writeLastToDisk();
struct Car* shiftCarsRight(int position);
struct Car* writeToDisk(Car* carPtr);
//int addCar(char* make, char* model, short year, long price, int uniqueID);
struct Car* getCarFromDiskById(int id);
struct Car* getLastCarFromDisk();
//struct Car* getCarById(int id);
struct Car* retrieveCarById(int id);
//struct Car* getAllCarsInMemory();
//struct Car* getAllCarsOnDisk();

//File carFile;//used as disk to save cars
//make a hashMap based on ID. Key= ID, value= Car,location...
//Map unique Id


//DONE
//returns the position of car in memory or 0 if not found
int getPositionInMemory(int id){
   // //printf("started getPositionInMemory");
    for(int i = 0; i < getNumberOfCarsInMemory(); i++){
        if(myCars[i].uniqueID == id){
            return i+1;//0 reserved for NULL
        }
    }
    return 0;
}

//retrieve car with given id
//if no car found return 0
//place car in memory (shift all cars right and place in memory)
//if(in memory) take out and shift; else if full then write last car to disk
struct Car* getCarById(int id){
    //printf("started getCarById 215\n");
    Car* car;
    int position = getPositionInMemory(id);//lowest position is 1
    if(position) {//it was found. Shift right take out and put at beginning
        car = shiftCarsRight(position);//TODO worry about overflow
        myCars[0] = *car;
    }
    else{
        car = getCarFromDiskById(id);
        addBuiltCar(car);
    }
    return car;
}

//DONE
//shifts all the cars from the given position to the right
//return the car at the given position
//makes room for new car
struct Car* shiftCarsRight(int position){
    //save the car at position before it get overridden
    int realPos = position-1;//positions are one off from "true" position
    Car* removedCar;
    if(position != getNumberOfCarsInMemory()) {
        removedCar = &myCars[realPos];
    }
    for(int i = realPos; i > 0; i--){
        myCars[i] = myCars[i-1];
    }
    //myCars[0] = NULL;//clears the current spot
    return removedCar;
}

//DONE
//set the size of the array based on the bytes given.
//Divide the bytes into the
int setMaxMemory(size_t bytes){
    int newInitialCarsAvailable = bytes/carSize;
    if(mallocCalled){
        if(newInitialCarsAvailable > initialCarsAvailable){//more space
            myCars = realloc(myCars,bytes);
            while(currentCarsAvailable != 0){
                addBuiltCarToEnd(getLastCarFromDisk());
            }
            //TODO delete
            //printf("malloced %d space. Made bigger\n", bytes);
        }
        else if(newInitialCarsAvailable < initialCarsAvailable){//need to Shrink
            while(getNumberOfCarsInMemory() > newInitialCarsAvailable){
                writeLastToDisk();
            }
            myCars = realloc(myCars,bytes);
            //printf("mallocced %d space. Made smaller\n", bytes);
        }
        myCars = realloc(myCars, bytes);
        initialCarsAvailable = newInitialCarsAvailable;
        //printf("Realloc");
    }
    else{ //first time
        myCars = (Car *) malloc(bytes);
        //number of cars available is bytes / Car size
            currentCarsAvailable = bytes / carSize;
            initialCarsAvailable = bytes / carSize;
            //TODO
            //printf("I just malloced %d space\n", bytes);
    }
    //TODO delete
    //printf("currentCarsAvailable = %d. initialCarsAvailable = %d.\n", currentCarsAvailable, initialCarsAvailable);
    mallocCalled = 1;
    //check if malloc worked
    if (myCars == NULL) {
        //printf(stderr, "malloc failed\n");
        return (-1);
    }
    return 0;
}

//TODO
int addBuiltCarToEnd(Car* car){
    return 0;
}

//writes the last most recently used car to Disk
struct Car* writeLastToDisk(){
    return writeLastToDisk(myCars[initialCarsAvailable-currentCarsAvailable-1]);//whatever is at end of line
}

//TODO writes the last most recently used car
struct Car* writeToDisk(Car* carPtr){
    currentCarsAvailable--;
    //put cars at end of array
    return (Car*)(NULL);
}

//adds Car to available slot memory or disk
//TODO DOES RIGHT SHIFT
int addBuiltCar(Car* carPtr){
    ////printf("I just did addBuiltCar\n");
    ////printf("this is the ID:%d\n",(*carPtr).uniqueID);
    //check memory
    //if memory not initialized yet
    if(initialCarsAvailable == 0){
        //printf("No memory\n");
        writeToDisk(carPtr);
    }
    else if(currentCarsAvailable){//not zero (there is space)
        //should shift from the slot after the last car
        shiftCarsRight(getNumberOfCarsInMemory()+1);
        //printf("Currently available\n");
        myCars[0] = *carPtr;
        currentCarsAvailable--;
    }//there is NO space so we MAKE IT
    else{//write last car to file, shift all cars over, put in place
        //write last car to file, shift all right, put in first spot
        //printf("No space, must shift \n");
        writeToDisk(shiftCarsRight(initialCarsAvailable-1));//TODO off by one?
    }
    ////printf("I finished addBuilt\n");
    //carTotal++;
    return 0;
}
//DONE
//make a new Car with the given parameters
//Test: idExists, noMemory, yesMemory,
int addCar(char* make, char* model, short year, long price, int uniqueID){
    Car newCar = {make, model, year, price, uniqueID};
    addBuiltCar(&newCar);
    carTotal++;
    //printf("Added car with id: %d. size of car %d \n", uniqueID, sizeof(newCar));
    return 0;
}

//returns a pointer to the car in memory
//also deletes the car from the disk
//TODO
struct Car* getCarFromDiskById(int id){
    //scan file,
    //  check for id (list ID first),
    //      if(id == id) scan the other items and make new car and delete car and shift all up
    //      if(id == id) return 0;
    return (Car*)(NULL);
}

//DONE
//return the total cars in the car array
int getNumberOfCarsInMemory(){
    return initialCarsAvailable - currentCarsAvailable;
}



//returns the last car on Disk
//TODO
struct Car* getLastCarFromDisk(){
    //scan file
    //find car
    //make car
    //delete car
    //shift other cars
    //return car
    return (Car*)(NULL);
}


struct Car* retrieveCarById(int id){
    Car* car;
    int position = getPositionInMemory(id);//lowest position is 1
    if(position) {//it was found
        car = shiftCarsRight(position);
        currentCarsAvailable++;
    }
    else{
        car = getCarFromDiskById(id);
    }
    return car;
}

//finds car in memory or on disk then deletes
int deleteCarById(int id){
    //don't do anything with car but helps compiler
    Car* car = retrieveCarById(id);
    carTotal--;
    return 0;
}



int modifyCarById(int id, struct Car* myCar){
    retrieveCarById(id);
    addBuiltCar(myCar);
    //Car car = retrieveCarById(id);
    //car = *myCar;//TODO does this work
    // car.make = (*myCar).make; car.model = (*myCar).model; car.year = (*myCar).year; car.price = (*myCar).price; car.uniqueID = (*myCar).uniqueID;
    //addBuiltCar(car);
    return 0;
}


//DONE
//returns the number of cars used * the carSize;
//returns memory used in bytes
int getAmountOfUsedMemory(){
    return getNumberOfCarsInMemory() * carSize;
}

//DONE
int getNumberOfCarsOnDisk(){
  return carTotal - getNumberOfCarsInMemory();
}

struct Car* getAllCarsInMemory(){
    return myCars;
}
/* this function does NOT cause the cars on disk to displace those
 * that were already in memory. It uses separate memory to load them
 * and return them to the caller. THE CALLER MUST FREE THIS MEMORY
 * WHEN FINISHED WITH THESE CARS.
*/
struct Car* getAllCarsOnDisk(){
    return myCars;
}