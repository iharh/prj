@echo off
setlocal
call vars.bat
call curl.bat -XPUT "http://%ES_HOSTPORT%/%PRJ_WRITE_ALIAS%/document/%DOC_ID%?routing=tag:search.twitter.com,2005:524293909925486593" -d @doc1.json
:: PRJ_ID PRJ_WRITE_ALIAS
endlocal
