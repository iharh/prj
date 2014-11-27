@echo off
setlocal
call vars.bat
call curl.bat -XGET "http://epbygomw0024:9200/%PRJ_INDEX%/verbatim/17?routing=9&fields=_parent"
:: id 9
endlocal
