@echo off
setlocal
call vars.bat
call curl.bat -XGET "http://%ES_HOSTPORT%/_nodes/stats/fs"
endlocal
