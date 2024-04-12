@echo off
echo.
echo module-web-view-admin
echo.

cd %~dp0
cd ../module-management-web-view/module-web-view-admin

npm run dev

cd bin
pause