@echo off
setlocal
:: -V --version
:: -t --configtest
:: -v --verbose
:: -v -v --debug
call logstash.bat agent -f ls.conf -l %~dp0.ls\ls.log
endlocal

