@echo off
echo.
echo List
echo.

cd %~dp0
cd ../module-management-service/module-service-list/target

set JAVA_OPTS=-Xms128m -Xmx512m -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=512m

java -Dfile.encoding=utf-8 %JAVA_OPTS% -jar module-service-list.jar

cd bin
pause