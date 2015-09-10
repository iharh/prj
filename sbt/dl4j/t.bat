@echo off
setlocal
del /Q %~dp0*.log
::call "sbt-du" for dependency updates

::call sbt.bat test
call sbt.bat "testOnly *Dl4jTest"
endlocal
