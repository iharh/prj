@echo off
setlocal
set VAGRANT_LOG=info
call vagrant.bat provision
endlocal
