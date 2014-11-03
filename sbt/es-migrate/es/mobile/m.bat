@echo off
setlocal
::call curl.bat -XPOST "http://epbygomw0024:18080/mobile/rest/index/migrate/1404?shards=7"
::call curl.bat -XPOST "http://admin:admin@epbygomw0024:18080/mobile/rest/index/migrate/1404"
call curl.bat -XPOST "http://admin:admin@epbygomw0024:18080/mobile/rest/index/migrate/1404"
::call curl.bat -XPOST "http://admin:admin@epbygomw0024:18080/mobile/rest/index/migrate/1404?shards=7"
endlocal
