@echo off
setlocal
call vars.bat
call curl.bat -XGET "http://%ES_HOSTPORT%/_migrate?projectId=2"
endlocal
