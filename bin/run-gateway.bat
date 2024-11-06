@echo off
echo.
echo Gateway
echo.

cd %~dp0
cd ../module-gateway/target

set JAVA_OPTS=-Xms128m -Xmx256m -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=256m

java -Dfile.encoding=utf-8 %JAVA_OPTS% -jar module-gateway.jar

cd bin
pause