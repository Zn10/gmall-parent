@echo off
echo.
echo install
echo.

%~d0
cd %~dp0

cd ..
call mvn   install -P dev -Dmaven.test.skip=true

pause