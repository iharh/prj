@echo off
setlocal
del /q %~dp0*.log
:: TwitSpec TxtToCSVSpec
call sbt.bat "testOnly *TwitSpec"
endlocal
