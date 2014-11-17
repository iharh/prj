@echo off
setlocal
call vars.bat
call curl.bat -XGET "http://epbygomw0024:9200/%PRJ_INDEX%/document/7"
:: 1..6 are found OK
endlocal
