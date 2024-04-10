@echo off
echo.
echo Cart
echo.

cd %~dp0
cd ../module-management-service/module-service-cart/target

set JAVA_OPTS=-Xms128m -Xmx512m -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=512m

java -Dfile.encoding=utf-8 %JAVA_OPTS% -jar module-service-cart.jar

cd bin
pause