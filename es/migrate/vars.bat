@echo off
set APP_HOST=epbygomw0024
set APP_PORT=18080
::9200
set USER=admin
set PWD=admin
set PRJ_ID=2084
::1404 1738
set GEN=0
set DOC_ID=1

set APP_HOSTPORT=%APP_HOST%:%APP_PORT%
set AUTH=%USER%:%PWD%
set PRJ_INDEX=%PRJ_ID%_%GEN%
set PRJ_READ_ALIAS=read_%PRJ_ID%
set PRJ_WRITE_ALIAS=write_%PRJ_ID%
