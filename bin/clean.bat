@echo off
echo.
echo clean
echo.

%~d0
cd %~dp0

cd ..
call mvn clean

pause