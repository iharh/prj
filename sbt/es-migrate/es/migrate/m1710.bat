@echo off
setlocal
call curl.bat -XPOST "http://admin:admin@epbygomw0024:18080/mobile/rest/index/migrate/1710?shards=7"
endlocal
