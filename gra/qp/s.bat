@echo off
setlocal
del /Q %~dp0*.log
call gradle.bat test -i --tests *SwimlineQPTests
:: info
endlocal
