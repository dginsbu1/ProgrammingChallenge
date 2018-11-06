#include "dataStore.h"
//#include "dataStore.c"
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



int  printOut(Car* car){
    for(int i = 0; i<4; i++){
        printf("%s ", car[i].make);
        printf("%s ", car[i].model);
        printf("%d ", car[i].year);
        printf("%ld ", car[i].price);
        printf("%d ", car[i].uniqueID);
    }
    return 0;
}
int main() {
    printf("HI its working\n");
    setMaxMemory(2000);
    Car a = {"Toyota", "light", 2001, 101, 1};
    //printOut(&a);
    addCar("Toyota", "light", 2001, 101, 1);
    addCar("Toyota", "Dark", 2002, 102, 2);
    addCar("Camry", "C", 2003, 103, 3);
    addCar("Lexus", "L", 2004, 104, 4);
    printOut(getCarById(2));
    return 0;
}
//int addCar(char* make, char* model, short year, long price, int uniqueID);
//int setMaxMemory(size_t bytes);
//struct Car* getCarById(int id);
//int deleteCarById(int id);
//int modifyCarById(int id, struct Car* myCar);
//int getNumberOfCarsInMemory();
//int getAmountOfUsedMemory();
//int getNumberOfCarsOnDisk();
//struct Car* getAllCarsInMemory();



