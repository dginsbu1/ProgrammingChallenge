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
    int inMemory;
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
//DONE
//make a new Car with the given parameters
//Test: idExists, noMemory, yesMemory,
int addCar(char* make, char* model, short year, long price, int uniqueID){
    Car newCar = Car{make, model, year, price, uniqueID};
    addCar(newCar);
    return 0;
}

//retrieve car with given id
//if no car found return 0
//place car in memory (shift all cars right and place in memory)
//if(in memory) take out and shift; else if full then write last car to disk
struct Car* getCarById(int id){
    Car* car
    int position = getPositionInMemory(id);//lowest position is 1
    if(position) {//it was found
        car = shiftCarsRight(positon);
        currentCarsAvailable++;//just "removed a car" will add back soon
    }
    else{
        car = getCarFromDisk(id);
    }
    //put car in new slot
    addCar(car);
    return &car;
}

//adds Car to available slot memory or disk
//TODO
int addCar(car){
    //check memory
    //if memory not initialized yet
    if(initialCarsAvailable == 0){
        writeToDisk(car);
    }
    else if(currentCarsAvailable){//not zero (there is space)
        //check to make sure the * is in right place
        //either need to shift and add it
        //or its been shifted and need to add it
        myCars[0] = car;
        currentCarsAvailable--;
    }
        //there is NO space so we MAKE IT
    else{//write last car to file, shift all cars over, put in place
        //write last car to file, shift all right, put in first spot
        writeToDisk(myCars[rightShift(initialCarsAvailable)]);//TODO off by one?
    }
    carTotal++;
    return 0;
}


//DON'T NEED
//returns 0/false if a car with id was found
//returns 1/true if no car was found with id
static int isUniqueID(int id){
    return findCar == NULL;
}


//DONE
//shifts all the cars from the given position to the right
//return the car at the given position
//makes room for new car
struct Car* shiftCarsRight(int position){
    //save the car at position before it get overridden
    position--;//positions are one off from "true" position
    Car removedCar*;
    removedCar = (myCars+position)*;
    for(int i = position; i > 0; i--){
        (myCars+i)* = (myCars+i -1)*;
    }
    return removedCar;
}
//DONE
//returns the position of car in memory or 0 if not found
int getPositionInMemory(int id){
    for(int i = 0; i < getNumberOfCarsInMemory(); i++){
        if(myCars[i].id == id){
            return i+1;//0 reserved for NULL
        }
    }
    return 0;
}

//returns a pointer to the car in memory
//also deletes the car from the disk
//TODO
struct Car* getCarFromDisk(id){
    //scan file,
    //  check for id (list ID first),
    //      if(id == id) scan the other items and make new car and delete car and shift all up
    //      if(id == id) return 0;

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