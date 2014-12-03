@echo off
setlocal
call vars.bat
call curl.bat -XPUT "http://%APP_HOSTPORT%/_snapshot/es_snap" -d @snapshotrepo.json
endlocal
