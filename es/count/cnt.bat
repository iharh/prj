@echo off
setlocal
call vars.bat

set ES_HOSTPORT=%ES_HOST%:9200
set IDX_ALIAS=read_%IDX%

echo counts for host: %ES_HOST% index: %IDX%
echo.

call:doCnt document
call:doCnt verbatim
call:doCnt sentence
call:doCnt .percolator
goto:done


:doCnt
echo count for %1:
echo.
call curl.bat -XGET "http://%ES_HOSTPORT%/%IDX_ALIAS%/%1/_count?pretty"
echo.


:done
endlocal
