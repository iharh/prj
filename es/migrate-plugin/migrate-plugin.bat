@echo off
setlocal
call vars.bat
call curl.bat -XPOST "http://%ES_HOSTPORT%/_migrate?projectId=2&sleepBetweenBatches=100ms"
::&sleepBetweenBatches=1s
endlocal
