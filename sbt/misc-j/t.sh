#! /bin/bash
::del /Q %~dp0*.log

::call sbt.bat test
call sbt.bat "testOnly *MiscTest"
:: misc.MiscTest
