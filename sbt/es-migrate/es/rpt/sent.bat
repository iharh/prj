@echo off
setlocal
call vars.bat
call curl.bat -XGET "http://localhost:9200/%PRJ_INDEX%/sentence/_search" -d @sent.json
endlocal
