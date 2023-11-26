@echo off
echo.
echo [信息] 下载依赖文件。
echo.

%~d0
cd %~dp0

cd ..
call mvn   install -Dmaven.test.skip=true

pause