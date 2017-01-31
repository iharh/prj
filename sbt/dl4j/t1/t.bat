@echo off
setlocal
del /Q %~dp0*.log
::call sbt.bat test
call sbt-huge.bat "testOnly *Dl4jTest"
endlocal
