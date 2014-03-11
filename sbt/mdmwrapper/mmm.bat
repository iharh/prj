@echo off
setlocal EnableDelayedExpansion
pushd %~dp0
set CP_JARS=mdmwrapper-0.1-SNAPSHOT.jar
for %%i in (lib\*.jar) do set CP_JARS=!CP_JARS!;%%i
::echo %CP_JARS%
call run-java.bat -Xmx2500m -Xms2500m -Dlog4j.configuration="file:%~dp0\config\log4j.properties" -cp %CP_JARS% mdmwrapper.Main
popd
endlocal

