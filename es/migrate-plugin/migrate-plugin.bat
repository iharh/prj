@echo off
setlocal
call vars.bat
call curl.bat -XPOST "http://%ES_HOSTPORT%/_migrate?projectId=1"
::&sleepBetweenBatches=1s
endlocal

