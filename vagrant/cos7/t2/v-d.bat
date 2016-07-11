@echo off
setlocal
::set VAGRANT_LOG=debug
call vagrant.bat destroy -f
:: knife node delete epbygomw0024t2 -y
:: knife client delete epbygomw0024t2 -y
endlocal
