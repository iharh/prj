@echo off
setlocal
call vars.bat
call curl.bat -XPOST "http://%ES_HOSTPORT%/%PRJ_ID%/document,verbatim/_search?pretty" -d @q_id_document.json
endlocal
