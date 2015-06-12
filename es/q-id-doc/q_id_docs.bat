@echo off
setlocal
call vars.bat
::call curl.bat -XPOST "http://%ES_HOSTPORT%/%PRJ_ID%/document,verbatim/_search?pretty" -d @q_id_document.json

for /f "delims=" %%a in (doc-ids.txt) do call:single %%a

::call curl.bat -XPOST "http://%ES_HOSTPORT%/%PRJ_ID%/document/_search?pretty" -d @q_id_document.json > wb1-doc.json
::call curl.bat -XPOST "http://%ES_HOSTPORT%/%PRJ_ID%/verbatim/_search?pretty" -d @q_id_document.json > wb1-verb.json
::call curl.bat -XPOST "http://%ES_HOSTPORT%/%PRJ_ID%/sentence/_search?pretty" -d @q_id_document.json > wb1-sent.json
goto:done


:single
set P=%1
set QFILE=q-%P%.json

echo single - %p%
call pystache.bat q_id_document.mustache "{\"id\":\"%P%\"}" > %QFILE%

call curl.bat -XPOST "http://%ES_HOSTPORT%/%PRJ_ID%/document/_search?pretty" -d @%QFILE% > docs\doc-%P%.json
call curl.bat -XPOST "http://%ES_HOSTPORT%/%PRJ_ID%/verbatim/_search?pretty" -d @%QFILE% > vebs\verb-%P%.json
call curl.bat -XPOST "http://%ES_HOSTPORT%/%PRJ_ID%/sentence/_search?pretty" -d @%QFILE% > sent\sent-%P%.json

del %QFILE%
goto:eof

:done
endlocal
