@echo off
setlocal
del /Q %~dp0*.log
call sbt.bat "testOnly *JacksonTest"
endlocal
