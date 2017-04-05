@echo off
setlocal
del /Q %~dp0*.log
call gradle.bat clean test
::--info --tests *Cld2Test
endlocal
