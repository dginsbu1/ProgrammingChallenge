cmake_minimum_required(VERSION 3.12)
project(DataStore C)

set(CMAKE_C_STANDARD 11)

include_directories(.)

add_executable(DataStore
        dataStore.c
        dataStore.h
        driver.c)
