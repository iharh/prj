@echo off
setlocal
call vars.bat
call curl.bat -XPOST "http://epbygomw0024:9200/%PRJ_INDEX%/document/_search" -d @cnt.json
endlocal
