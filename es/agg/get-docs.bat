@echo off
setlocal
call vars.bat
::_0
call curl.bat -XGET "http://%ES_HOSTPORT%/%PRJ_ID%/document/62910727?pretty"

::_2
call curl.bat -XGET "http://%ES_HOSTPORT%/%PRJ_ID%_2/document/62908017?pretty"

::not-found
::call curl.bat -XGET "http://%ES_HOSTPORT%/%PRJ_ID%_2/document/62910727?pretty"
endlocal
