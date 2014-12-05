@echo off
set APP_HOST=blackbox10.clarabridge.net
::10.60.35.26
::epbygomw0024
set APP_PORT=18080
set ES_PORT=9200
set USER=admin
set PWD=admin
set PRJ_ID=17666
::1404 1738 2084
set GEN_SUFF=
::set GEN_SUFF=_1

set APP_HOSTPORT=%APP_HOST%:%APP_PORT%
set ES_HOSTPORT=%APP_HOST%:%ES_PORT%
set AUTH=%USER%:%PWD%
set PRJ_INDEX=%PRJ_ID%%GEN_SUFF%
