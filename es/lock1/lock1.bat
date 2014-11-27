@echo off
setlocal
call curl.bat -XPUT "http://epbygomw0024:9200/1/_settings" -d @lock1.json
endlocal
