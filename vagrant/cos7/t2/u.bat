@echo off
setlocal
set VAGRANT_LOG=info
call vagrant.bat up --no-provision
:: --debug
endlocal
