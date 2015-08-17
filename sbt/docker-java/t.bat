@echo off
setlocal
del /Q %~dp0*.log
::sbt dependencyUpdates

::call sbt.bat test
call sbt.bat "testOnly *DockerTest"
endlocal
