@echo off
setlocal
call vars.bat
call curl.bat -XPOST "http://admin:admin@epbygomw0024:18080/mobile/rest/index/switch/%PRJ_ID%?generation=0"
endlocal
