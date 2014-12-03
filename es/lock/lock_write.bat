@echo off
setlocal
call vars.bat
call curl.bat -XPUT "http://%ES_HOSTPORT%/%PRJ_INDEX%/_settings" -d @lock_write.json
endlocal
