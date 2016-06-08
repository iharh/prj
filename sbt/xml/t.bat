@echo off
setlocal
del /Q %~dp0*.log
::del /Q %~dp0out\*.xml
call sbt.bat test
::call sbt.bat "testOnly *XmlSpec"
endlocal
