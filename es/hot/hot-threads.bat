@echo off
setlocal
call vars.bat

for /f "delims=" %%a in ('print-date-time.bat - - _') do set val_dt=%%a

::call curl.bat -XGET "http://%ES_HOSTPORT%/_nodes/hot_threads?threads=1000&type=wait&pretty" > w_%val_dt%.txt
::call curl.bat -XGET "http://%ES_HOSTPORT%/_nodes/hot_threads?threads=1000&type=block&pretty" > b_%val_dt%.txt
call curl.bat -XGET "http://%ES_HOSTPORT%/_nodes/thread_pool?pretty" > tp_%val_dt%.txt
call curl.bat -XGET "http://%ES_HOSTPORT%/_cat/thread_pool?v"
endlocal
