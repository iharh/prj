@echo off
setlocal
call vars.bat
call curl.bat -XGET "http://%ES_HOSTPORT%/%PRJ_ID%/_optimize?max_num_segments=1&wait_for_merge=true&force&pretty"
endlocal
