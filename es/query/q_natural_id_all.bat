@echo off
setlocal
call vars.bat
::call curl.bat -XGET "http://%ES_HOSTPORT%/%PRJ_ID%/_mapping/verbatim?pretty"

::call curl.bat -XPOST "http://%ES_HOSTPORT%/%PRJ_ID%/document/_search?pretty" -d @q_natural_id_all.json
::call curl.bat -XPOST "http://%ES_HOSTPORT%/%PRJ_ID%/verbatim/_search?pretty" -d @q_natural_id_all.json

call curl.bat -XPOST "http://%ES_HOSTPORT%/%PRJ_ID%/document,verbatim/_search?pretty" -d @q_natural_id_all.json

endlocal
