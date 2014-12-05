@echo off
setlocal
call vars.bat
call curl.bat -XPUT "http://%ES_HOSTPORT%/%PRJ_ID%" -d @map-%PRJ_ID%.json
endlocal
