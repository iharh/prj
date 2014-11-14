@echo off
setlocal
call vars.bat
call curl.bat -XGET "http://epbygomw0024:9200/%PRJ_INDEX%/verbatim/17?routing=8_20141114071005&fields=_parent"
::call curl.bat -XGET "http://epbygomw0024:9200/%PRJ_INDEX%/verbatim/17?routing=8_20141114071005"
:: id 9
endlocal
