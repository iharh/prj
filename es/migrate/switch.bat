@echo off
setlocal
call vars.bat
call curl.bat -XPOST "http://%AUTH%@%APP_HOSTPORT%/mobile/rest/index/switch/%PRJ_ID%?generation=1
endlocal
