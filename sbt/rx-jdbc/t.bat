@echo off
setlocal
del /Q %~dp0*.log
call sbt.bat test
::call sbt.bat "testOnly *TestSimple"
::call sbt.bat "testOnly *TestPoi"
endlocal
