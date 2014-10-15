@echo off
setlocal
call curl.bat -XPOST "http://epbygomw0024:9200/_snapshot/my_backup/snapshot1/_restore?wait_for_completion=true" -d @snapshotrestore.json
endlocal
