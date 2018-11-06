#include "dataStore.h"
//#include "dataStore.c"
#include <stdio.h>

//int addCar(char* make, char* model, short year, long price, int uniqueID);TODO DONE
//struct Car* getAllCarsInMemory();//todo
//int setMaxMemory(size_t bytes);TODO DONE
//struct Car* getCarById(int id);TODO DONE
//int getNumberOfCarsInMemory();//todo done
//int getAmountOfUsedMemory();todo done


//int deleteCarById(int id);
//int modifyCarById(int id, struct Car* myCar);todo NONONONO

//int getNumberOfCarsOnDisk();

int printOut(Car* car);


int main(){
    //allocate space from the heap
    setMaxMemory(1200);
    //Car a = {"Toyota", "light", 2001, 101, 1};
    addCar("Toyota", "light", 2001, 101, 1);
    addCar("JewMobile", "star", 2002, 102, 2);
    addCar("Camry", "C", 2003, 103, 3);
    addCar("Lexus", "L", 2004, 104, 4);

    Car *cars = getAllCarsInMemory();
    printf("printing cars in memory\n");

    for(int i =0; i< 4; i++){
        printOut(&(cars[i]));
    }
    printf("get and print car with id 4\n");
    Car *d = getCarById(4);
    printOut(d);

    printf("Used Memory: %d\n",getAmountOfUsedMemory());
    printf("Numbers of cars in memory: %d\n",getNumberOfCarsInMemory());
    printf("Numbers of cars on disk: %d\n",getNumberOfCarsOnDisk());




//
//    for(int i =0; i< 4; i++){
//        printOut(&(cars[i]));
//    }
//    printf("=========\n");
//    //Car newishCar = {"new", "car", 2030, 1111, 5};
//    //modifyCarById(1, &newishCar);
//    deleteCarById(1);
//    for(int i =0; i< 4; i++){
//        printOut(&(myCars[i]));
//    }
//    printf("=========\n");
//
//    printf("==========\n");
//    Car newishCar = {"new", "car", 2030, 1111, 5};
//    modifyCarById(1, &newishCar);
//    for(int i =0; i< 4; i++){
//        printOut(&(myCars[i]));
//    }
//
//
//
//    for(int i =0; i< 4; i++){
//        printOut(&(myCars[i]));
//    }
//
//    //find 1st
//    //delete last
//    Car newCar = {"new", "car", 1111, 111, 5};
//    modifyCarById(2, &newCar);
//    for(int i =0; i< 4; i++){
//        printOut(&(myCars[i]));
//    }
//
////
////    printOut(getCarById(2));
////    printOut(getCarById(3));
////    printOut(getCarById(4));
//
//    return 0;
}
int  printOut(Car* car){
    for(int i = 0; i<1; i++){
        printf("%s ", car[i].make);
        printf("%s ", car[i].model);
        printf("%d ", car[i].year);
        printf("%ld ", car[i].price);
        printf("%d ", car[i].uniqueID);
        printf("\n");
    }
    return 0;
}

