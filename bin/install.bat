@echo off
echo.
echo [��Ϣ] ���������ļ���
echo.

%~d0
cd %~dp0

cd ..
call mvn   install -Dmaven.test.skip=true

pause