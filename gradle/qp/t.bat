@echo off
setlocal
del /Q %~dp0*.log
call gradle.bat test --info --tests *SyntaxOnlyRealTests
endlocal
