@echo off
setlocal
call curl.bat -XGET "http://epbygomw0024:9200/idxsrc/type1/_search" -d @idxsrcquery.json
endlocal
