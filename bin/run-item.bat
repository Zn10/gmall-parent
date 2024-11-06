@echo off
echo.
echo Item
echo.

cd %~dp0
cd ../module-management-service/module-service-item/target

set JAVA_OPTS=-Xms128m -Xmx256m -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=256m

java -Dfile.encoding=utf-8 %JAVA_OPTS% -jar module-service-item.jar

cd bin
pause