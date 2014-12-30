@echo off
setlocal
call vars.bat
call curl.bat -XPOST "http://%ES_HOSTPORT%/_bulk" --data-binary @bulk-%PRJ_ID%.json
endlocal
