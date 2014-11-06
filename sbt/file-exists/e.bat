@echo off
setlocal
call java.bat -cp target\file-exists-0.1-SNAPSHOT.jar fe.CLI %*
endlocal
