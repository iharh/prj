@echo off
setlocal
call vars.bat
call curl.bat -XPOST "http://%ES_HOSTPORT%/%PRJ_READ_ALIAS%/document/_search" -d @search-doc-nat-id.json
endlocal
