@echo off
if not "%JAVA_HOME%" == "" goto run
@echo 请设置好JAVA_HOME，或者在头部添加下面行
@echo set JAVA_HOME=C:\JDK1.8
pause>nul
goto exit
:run
cd ../android/WeexFrameworkWrapper
gradle build -x test
cd ../../fe
:exit
