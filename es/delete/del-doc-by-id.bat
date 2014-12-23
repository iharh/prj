@echo off
setlocal
call vars.bat
call curl.bat -XDELETE "http://%ES_HOSTPORT%/%PRJ_ID%/document/%DOC_ID%"
::refresh=true
::call curl.bat -XDELETE "http://%ES_HOSTPORT%/%PRJ_INDEX%/document/%DOC_ID%"
endlocal
