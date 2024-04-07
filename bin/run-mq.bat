@echo off
echo.
echo [��Ϣ] ʹ��Jar��������Mq���̡�
echo.

cd %~dp0
cd ../module-management-service/module-service-mq/target

set JAVA_OPTS=-Xms128m -Xmx512m -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=512m

java -Dfile.encoding=utf-8 %JAVA_OPTS% -jar module-service-mq.jar

cd bin
pause