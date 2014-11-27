@echo off
setlocal
call vars.bat
call curl.bat -XGET "http://%ES_HOSTPORT%/%PRJ_INDEX%/_mapping/sentence?pretty"
:: PRJ_ID PRJ_WRITE_ALIAS
endlocal
