dataStore.c

int currentCarsAvailable = 0;//how many cars are currently available in memory
int initialCarsAvailable = 0;//how many cars slots did we start with in memory
int carSize = sizeof(Car);
int carTotal = 0;//how many cars are there Total
int carPosition; //what spot in Car* am I at using pointer
int mallocCalled = 0;
File carFile;//used as disk to save cars
//make a hashMap based on ID. Key= ID, value= Car,location...
//Map unique Id


typedef struct Car{
    char* make[16];//leave blank space
    char* model[26];//leave blank space
    short year;
    long price;
    int uniqueID;
} Car;

//DONE
//set the size of the array based on the bytes given.
//Divide the bytes into the
int setMaxMemory(size_t bytes){
    if(mallocCalled){
        realloc
    }
    else {
        Car *myCars = (Car *) malloc(bytes);
        //number of cars available is bytes / Car size
        currentCarsAvailable = bytes / carSize;
        initialarsAvailable = bytes / carSize;
    }
    mallocCalled = 1;
    return 0;
}
//make a new Car with the given parameters
//Test: idExists, noMemory, yesMemory,
int addCar(char* make, char* model, short year, long price, int uniqueID){
    //dont allow 0 as id
    if(uniqueID == 0){
        //throw some error
    }
    //TODO
    if(make > 16 | model > 16){/*throw error*/}
    if(!isUniqueID()) {
        //throw some error or somethinfg
    }
    //check for size issues
    Car newCar = Car{make, model, year, price, uniqueID};
    //TODO
    //check memory
    if(currentCarsAvailable){//not zero
        //check o make sure the * is in right place
        *(myCars+getNumberOfCarsInMemory() = newCar;
        currentCarsAvailable--;
    }
    else{
        //do a bucn
    }
    carTotal++;
    return 0;
}



//DONE
//returns 0/false if a car with id was found
//returns 1/true if no car was found with id
static int isUniqueID(int id){
    return !findCar(id);
}

//either itterate through all cars, or use HashMap
//if no car return 0
//Test: Car in memory, car in disk, no Car
static struct Car* findCar(int id){
    if(id == 0){
        return 0;
    }

}

//retrieve car with given id
//if no car found return 0
//place car in memory (shift all cars right and place in memory)
//if(in memory) take out and shift; else if full then write last car to disk
struct Car* getCarById(int id){

}
//DONE
//shifts all the cars from the given position to the right
//return the car at the given position
//makes room for new car
static Car shiftCarsRight(int position){
    //save the car at position before it get overridden
    Car removedCar*;
    removedCar = (myCars+position)*;
    for(int i = position; i > 0; i--){
        (myCars+i)* = (myCars+i -1)*;
    }
    return removedCar;
}







//
int deleteCarById(int id){

    carTotal--;
    //change currentCarsAvailable
    return 0;
}
int modifyCarById(int id, struct Car* myCar);

//DONE
//return the total cars in the car array
int getNumberOfCarsInMemory(){
    return initialCarsAvailable - currentCarsAvailable;
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

struct Car* getAllCarsInMemory();
/* this function does NOT cause the cars on disk to displace those
 * that were already in memory. It uses separate memory to load them
 * and return them to the caller. THE CALLER MUST FREE THIS MEMORY
 * WHEN FINISHED WITH THESE CARS.
*/
struct Car* getAllCarsOnDisk();