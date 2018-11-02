#include "dataStore.h"
#include "dataStore.c"
#include <stdio.h>

//int addCar(char* make, char* model, short year, long price, int uniqueID);
//int setMaxMemory(size_t bytes);
//struct Car* getCarById(int id);
//int deleteCarById(int id);
//int modifyCarById(int id, struct Car* myCar);
//int getNumberOfCarsInMemory();
//int getAmountOfUsedMemory();
//int getNumberOfCarsOnDisk();
//struct Car* getAllCarsInMemory();


driver.c
int main() {
    setMaxMemory(30);
    addCar("Toyota", "light", 2001, 101, 1);
    addCar("Toyota", "Dark", 2002, 102, 2);
    addCar("Camry", "C", 2003, 103, 3);
    addCar("Lexus", "L", 2004, 104, 4);
    printOut(getNumberOfCarsInMemory());

    return 0;
}
printOut(Car* car){
    for(int i = 0; i<4; i++){
        print(car[i].make);
        print(car[i].model);
        print(car[i].year);
        print(car[i].price);
        print(car[i].uniqueID);
    }
}



