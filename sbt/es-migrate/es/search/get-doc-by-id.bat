@echo off
setlocal
call vars.bat
call curl.bat -XGET "http://%ES_HOSTPORT%/%PRJ_ID%/document/3857671?routing=tag:search.twitter.com,2005:524293909925486593"
:: PRJ_ID PRJ_READ_ALIAS
endlocal
