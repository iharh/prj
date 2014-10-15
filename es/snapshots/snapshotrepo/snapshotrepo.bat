@echo off
setlocal
call curl.bat -XPUT "http://epbygomw0024:9200/_snapshot/my_backup" -d @snapshotrepo.json
endlocal
