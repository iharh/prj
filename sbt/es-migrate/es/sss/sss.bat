@echo off
setlocal
call vars.bat
::call curl.bat -XPOST "http://epbygomw0024:9200/%PRJ_INDEX%/_id_sentence/_search" -d @cnt.json
:: _id is _id_sentence
endlocal
