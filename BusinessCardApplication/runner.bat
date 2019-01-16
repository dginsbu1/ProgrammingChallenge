@echo off
title runner

echo BusinessCardApplication:
echo.
javac  "%CD%\src\main\java\App"\*.java
java -cp "%CD%\src\main\java"\ App.Driver %*
echo.

