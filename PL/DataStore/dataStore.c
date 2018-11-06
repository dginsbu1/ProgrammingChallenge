//#include "driver.c"
#include "dataStore.h"
#include <stddef.h>
#include <stdlib.h>

int currentCarsAvailable = 0;//how many cars are currently available in memory
int initialCarsAvailable = 0;//how many cars slots did we start with in memory
int carTotal = 0;//how many cars are there Total
int carPosition; //what spot in Car* am I at using pointer
int mallocCalled = 0;
Car *myCars;
int carSize = sizeof(Car);

//functions list for compiler
int addBuiltCar(Car* carPtr2);
struct Car* getLastCarFromDisk();
struct Car* writeLastToDisk();
int setMaxMemory(size_t bytes);
struct Car* shiftCarsRight(int position);
struct Car* writeToDisk(Car* carPtr);
int addCar(char* make, char* model, short year, long price, int uniqueID);
struct Car* getCarFromDiskById(int id);
int getNumberOfCarsInMemory();
int getPositionInMemory(int id);
struct Car* getLastCarFromDisk();
struct Car* getCarById(int id);
struct Car* retrieveCarById(int id);
int deleteCarById(int id);
int modifyCarById(int id, struct Car* myCar);
int getAmountOfUsedMemory();
int getNumberOfCarsOnDisk();
struct Car* getAllCarsInMemory();
struct Car* getAllCarsOnDisk();

//File carFile;//used as disk to save cars
//make a hashMap based on ID. Key= ID, value= Car,location...
//Map unique Id


//DONE
//set the size of the array based on the bytes given.
//Divide the bytes into the
int setMaxMemory(size_t bytes){
    int newInitialCarsAvailable = bytes/carSize;
    if(mallocCalled){
        if(newInitialCarsAvailable > initialCarsAvailable){//more space
            myCars = realloc(myCars,bytes);
            while(currentCarsAvailable != 0){
                addBuiltCar(getLastCarFromDisk());
            }
            //TODO delete
            printf("malloced %d space. Made bigger", bytes);
        }
        else if(newInitialCarsAvailable < initialCarsAvailable){//need to Shrink
            while(getNumberOfCarsInMemory() > newInitialCarsAvailable){
                writeLastToDisk();
            }
            myCars = realloc(myCars,bytes);
            printf("malloced %d space. Made smaller", bytes);
        }
    } else {
            Car *myCars = (Car *) malloc(bytes);
            //number of cars available is bytes / Car size
            currentCarsAvailable = bytes / carSize;
            initialarsAvailable = bytes / carSize;
        }
        mallocCalled = 1;
        return 0;
    }
    initialCarsAvailable = newInitialCarsAvailable;
    mallocCalled = 1;
    return 0;
}


//DONE
//shifts all the cars from the given position to the right
//return the car at the given position
//makes room for new car
struct Car* shiftCarsRight(int position){
    //save the car at position before it get overridden
    position--;//positions are one off from "true" position
    Car* removedCar;
    removedCar = &myCars[position];
    for(int i = position; i > 0; i--){
        //(myCars+i)* = (myCars+i -1)*;
        myCars[i] = myCars[i-1];//FIXED
    }
    //TODO find way to delete current car
    //myCars[0] = (Car)(NULL);//clears the current spot
    return removedCar;
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
//TODO
int addBuiltCar(Car* carPtr2){
    //check memory
    //if memory not initialized yet
    if(initialCarsAvailable == 0){
        writeToDisk(carPtr2);
    }
    else if(currentCarsAvailable){//not zero (there is space)
        //check to make sure the * is in right place
        //either need to shift and add it
        //or its been shifted and need to add it
        myCars[0] = *carPtr2;
        currentCarsAvailable--;
    }
        //there is NO space so we MAKE IT
    else{//write last car to file, shift all cars over, put in place
        //write last car to file, shift all right, put in first spot
        writeToDisk(shiftCarsRight(initialCarsAvailable));//TODO off by one?
    }
    carTotal++;
    return 0;
}
//DONE
//make a new Car with the given parameters
//Test: idExists, noMemory, yesMemory,
int addCar(char* make, char* model, short year, long price, int uniqueID){
    Car newCar = {make, model, year, price, uniqueID};
    addBuiltCar(&newCar);
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

//DONE
//returns the position of car in memory or 0 if not found
int getPositionInMemory(int id){
    for(int i = 0; i < getNumberOfCarsInMemory(); i++){
        if(myCars[i].uniqueID == id){
            return i+1;//0 reserved for NULL
        }
    }
    return 0;
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


//retrieve car with given id
//if no car found return 0
//place car in memory (shift all cars right and place in memory)
//if(in memory) take out and shift; else if full then write last car to disk
struct Car* getCarById(int id){
    Car* car;
    int position = getPositionInMemory(id);//lowest position is 1
    if(position) {//it was found
        car = shiftCarsRight(position);
        currentCarsAvailable++;//just "removed a car" will add back soon
    }
    else{
        car = getCarFromDiskById(id);
    }
    //put car in new slot
    addBuiltCar(car);
    return car;
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