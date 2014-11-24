@echo off
setlocal
call vars.bat
call curl.bat -XPOST "http://admin:admin@epbygomw0024:18080/mobile/rest/index/migrate/%PRJ_ID%?shards=7&docvalues=_lc_tokens,_lc,_tokendata,author,age,rating
::_words,_mstokenname is analyzed
::just strings are also analyzed:
::  author, age, rating
::
::"natural_id,_id_source"
endlocal
