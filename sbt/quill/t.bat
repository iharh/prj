@echo off
setlocal
del /Q %~dp0*.log

call sbt.bat test
::call sbt.bat "testOnly *ComponentTest"
endlocal
