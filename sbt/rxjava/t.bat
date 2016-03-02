@echo off
setlocal
del /Q %~dp0*.log
::sbt-du

::call sbt.bat test
call sbt.bat "testOnly *SubscribeTest"
::call sbt.bat "testOnly *SchedulerTest"
endlocal
