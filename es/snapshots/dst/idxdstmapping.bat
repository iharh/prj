@echo off
setlocal
call curl.bat -XPUT "http://epbygomw0024:9200/idxdst" -d @idxdstmapping.json
endlocal
