@echo off
setlocal
call vars.bat
call curl.bat -XPUT "http://%APP_HOSTPORT%/_snapshot/es_snap/snapshot1?wait_for_completion=true" -d @snapshottake.json
endlocal
