@echo off
setlocal
del /Q %~dp0*.log
::call sbt.bat test
::call sbt.bat "testOnly *BaseDecodeTest"
::call sbt.bat "testOnly *ConfTest"
::call sbt.bat "testOnly *ProjTest"
call sbt.bat "testOnly *CBCMPTypeTest"
::call sbt.bat "testOnly *ProjCountTest"
::call sbt.bat "testOnly *CleanCountTest"
::call sbt.bat "testOnly *CleanExportTest"
endlocal
