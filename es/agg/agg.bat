@echo off
setlocal
call vars.bat
call curl.bat -XGET "http://%ES_HOSTPORT%/%PRJ_INDEX%/_search" -d @agg.json
endlocal
