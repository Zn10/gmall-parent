@echo off
echo.
echo package
echo.

%~d0
cd %~dp0

cd ..
call mvn -T 6C clean package -P local -Dmaven.test.skip=true

pause