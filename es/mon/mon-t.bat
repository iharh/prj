@echo off
setlocal
call vars.bat

:start
for /f "delims=" %%a in ('print-date-time.bat - - _') do set val_dt=%%a

call curl.bat -XGET "http://%ES_HOSTPORT%/_cat/thread_pool?v&h=ba,bq,br,bl,ia,iq,ir,il,sa,sq,sr,sl,fa,fq,fr,fl,gea,geq,ger,gel,ma,mq,mr,ml,maa,maq,mar,mal,oa,oq,or,ol,ra,rq,rr,rl" > thread_pool_%val_dt%.txt

goto:start

endlocal
