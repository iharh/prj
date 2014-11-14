@echo off
setlocal
call vars.bat
call curl.bat -XPOST "http://admin:admin@epbygomw0024:18080/mobile/rest/index/migrate/%PRJ_ID%?shards=7"
::&docvalues=_tokendata,natural_id,_id_source"
endlocal
