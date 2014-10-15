@echo off
setlocal
call curl.bat -XPUT "http://epbygomw0024:9200/idxsrc" -d @idxsrcmapping.json
endlocal
