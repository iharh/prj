@echo off
setlocal
call vars.bat
::call curl.bat -XGET "http://%ES_HOSTPORT%/?pretty=true"
::
::call curl.bat -XGET "http://%ES_HOSTPORT%/_cluster/health?pretty"
call curl.bat -XGET "http://%ES_HOSTPORT%/_cluster/health/10528?pretty&level=shards"

::call curl.bat -XGET "http://%ES_HOSTPORT%/_nodes/stats?pretty"
::call curl.bat -XGET "http://%ES_HOSTPORT%/_nodes/stats/fs?pretty"
::call curl.bat -XGET "http://%ES_HOSTPORT%/_nodes/stats/http?pretty"
::call curl.bat -XGET "http://%ES_HOSTPORT%/_nodes/stats/process?pretty"
::call curl.bat -XGET "http://%ES_HOSTPORT%/_nodes/stats/network?pretty"

::call curl.bat -XGET "http://%ES_HOSTPORT%/_cat/indices?v"
endlocal
