@echo off
setlocal
call curl.bat -XPOST "http://epbygomw0024:9200/_bulk" --data-binary @bulk1.json
endlocal