@echo off
set ES_HOST=epbygomw0024
set ES_PORT=9200
set PRJ_ID=1738
set GEN=0
set DOC_ID=1

set PRJ_INDEX=%PRJ_ID%_%GEN%
set PRJ_READ_ALIAS=read_%PRJ_ID%
set PRJ_WRITE_ALIAS=write_%PRJ_ID%
set ES_HOSTPORT=%ES_HOST%:%ES_PORT%