@echo off
setlocal
:: -V --version
:: -t --configtest
:: -v --verbose
:: -vv --debug
:: -pluginpath %~dp0 inputs,filters,outputs
call logstash.bat agent -f ls.conf -l %~dp0.ls\ls.log --pluginpath %~dp0
endlocal

