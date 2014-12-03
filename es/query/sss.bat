@echo off
setlocal
call vars.bat
call curl.bat -XPOST "http://epbygomw0024:9200/%PRJ_INDEX%/verbatim/17"
:: _id is _id_sentence
endlocal
