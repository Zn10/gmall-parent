@echo off
echo.
echo install
echo.

%~d0
cd %~dp0

cd ..
call mvn -T 6C   install -P local -Dmaven.test.skip=true

pause