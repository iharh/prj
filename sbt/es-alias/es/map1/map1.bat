@echo off
setlocal
call curl.bat -XPUT "http://epbygomw0024:9200/1" -d @map1.json
endlocal
