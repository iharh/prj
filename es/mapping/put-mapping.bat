@echo off
setlocal
call vars.bat
call curl.bat -XPUT "http://%ES_HOSTPORT%/%PRJ_INDEX%" -d @mapping.json
endlocal
