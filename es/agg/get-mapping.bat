@echo off
setlocal
call vars.bat
call curl.bat -XGET "http://%ES_HOSTPORT%/%PRJ_ID%/_mapping?pretty"
endlocal
