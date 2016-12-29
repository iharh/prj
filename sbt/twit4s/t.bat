@echo off
setlocal
del /q %~dp0*.log
call sbt.bat "testOnly *TwitSpec"
endlocal
