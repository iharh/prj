@echo off
setlocal
del /Q %~dp0*.log
del /Q %~dp0out\*.csv

call sbt.bat test
::call sbt.bat "testOnly *ComponentTest"
endlocal
