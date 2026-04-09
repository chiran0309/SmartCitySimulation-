@echo off
cd /d %~dp0

javac -d bin src\exceptions\*.java src\systems\*.java src\entities\*.java src\managers\*.java src\SmartCitySimulation.java

java -cp bin SmartCitySimulation

pause
