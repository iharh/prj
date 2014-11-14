@echo off
setlocal
call vars.bat
call curl.bat -XGET "http://localhost:9200/%PRJ_INDEX%/document/_search" -d @doc.json
endlocal
