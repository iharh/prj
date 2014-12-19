@echo off
setlocal
del /Q %~dp0*.log
:: test
::call sbt.bat "testOnly com.clarabridge.elasticsearch.index.migrate.TestSimple"
call sbt.bat "testOnly *TestSimple"
endlocal
