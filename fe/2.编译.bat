@echo off
if not "%JAVA_HOME%" == "" goto run
@echo �����ú�JAVA_HOME��������ͷ�����������
@echo set JAVA_HOME=C:\JDK1.8
pause>nul
goto exit
:run
cd ../android/WeexFrameworkWrapper
gradle build -x test
cd ../../fe
:exit
