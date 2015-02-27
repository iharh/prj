@echo off
setlocal
:: -V --version
:: -t --configtest
:: -v --verbose
:: -vv --debug
call logstash.bat agent -f ls.conf -l %~dp0.ls\ls.log
endlocal

