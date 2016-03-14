#! /bin/bash
#del /Q %~dp0*.log
#call sbt.bat test
sbt "testOnly *DictTest"
