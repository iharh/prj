@echo off
setlocal
call vars.bat
::call curl.bat -XPOST "http://%ES_HOSTPORT%/%PRJ_ID%/document,verbatim/_search?pretty" -d @q_id_document.json

::call curl.bat -XPOST "http://%ES_HOSTPORT%/%PRJ_ID%/document/_search?pretty" -d @q_id_document.json
::call curl.bat -XPOST "http://%ES_HOSTPORT%/%PRJ_ID%/verbatim/_search?pretty" -d @q_id_document.json
call curl.bat -XPOST "http://%ES_HOSTPORT%/%PRJ_ID%/sentence/_search?pretty" -d @q_id_document.json
endlocal
