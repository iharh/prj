@echo off
setlocal
call vars.bat
call curl.bat -XPOST "http://%AUTH%@%APP_HOSTPORT%/mobile/rest/index/migrate/%PRJ_ID%?shards=7
::&docvalues=_lc_tokens,_lc,_tokendata,author,age,rating
::usable for DocValues:
::  _lc_tokens,_lc,_tokendata
::non-usable for DocValues because they are analyzed:
::  _words,_mstokenname
::    ?? whether do we really need _mstokenname here ?
::    ?? ask ES forums
::just strings are also analyzed:
::  author, age, rating
::
::"natural_id,_id_source"
endlocal
