@echo off
setlocal
call vars.bat
::_0
call curl.bat -XGET "http://%ES_HOSTPORT%/%PRJ_ID%/sentence/164968103?pretty"

::_2
call curl.bat -XGET "http://%ES_HOSTPORT%/%PRJ_ID%_2/sentence/164968?pretty"

endlocal
