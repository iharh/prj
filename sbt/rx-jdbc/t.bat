@echo off
setlocal
del /Q %~dp0*.log
call sbt.bat "testOnly *ConfTest"
::call sbt.bat "testOnly *TestSimple"
::call sbt.bat test
endlocal
