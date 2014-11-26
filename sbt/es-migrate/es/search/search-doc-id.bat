@echo off
setlocal
call vars.bat
call curl.bat -XPOST "http://%ES_HOSTPORT%/%PRJ_ID%/document/_search" -d @search-doc-nat-id.json
:: PRJ_READ_ALIAS
endlocal
