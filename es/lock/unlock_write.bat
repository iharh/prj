@echo off
setlocal
call vars.bat
call curl.bat -XPUT "http://%ES_HOSTPORT%/%PRJ_INDEX%/_settings" -d @unlock_write.json
endlocal
