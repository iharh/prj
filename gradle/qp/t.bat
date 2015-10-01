@echo off
setlocal
del /Q %~dp0*.log
::call gradle.bat test
::call gradle.bat test --tests *SwimlineQPTests
call gradle.bat test --tests *SyntaxOnlyRealTests
endlocal
