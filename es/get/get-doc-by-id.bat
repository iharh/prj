@echo off
setlocal
call vars.bat
call curl.bat -XGET "http://%ES_HOSTPORT%/%PRJ_INDEX%/document/%DOC_ID%?pretty"
endlocal
