@echo off
echo.
echo package
echo.

%~d0
cd %~dp0

cd ..
call mvn clean package -P dev -Dmaven.test.skip=true

pause