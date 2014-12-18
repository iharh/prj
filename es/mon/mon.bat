@echo off
setlocal
call vars.bat

for /f "delims=" %%a in ('print-date-time.bat - - _') do set val_dt=%%a

::call curl.bat -XGET "http://%ES_HOSTPORT%/_nodes/cluster?pretty"
::call curl.bat -XGET "http://%ES_HOSTPORT%/_nodes/thread_pool?pretty" > tp_%val_dt%.txt
call curl.bat -XGET "http://%ES_HOSTPORT%/_nodes/stats?pretty" > stats_%val_dt%.txt

call curl.bat -XGET "http://%ES_HOSTPORT%/_cat/pending_tasks?v" > pending_tasks_%val_dt%.txt

::call curl.bat -XGET "http://%ES_HOSTPORT%/_cat/thread_pool?v"
call curl.bat -XGET "http://%ES_HOSTPORT%/_cat/thread_pool?v&h=ba,bq,br,bl,ia,iq,ir,il,sa,sq,sr,sl,fa,fq,fr,fl,gea,geq,ger,gel,ma,mq,mr,ml,maa,maq,mar,mal,oa,oq,or,ol,ra,rq,rr,rl" > thread_pool_%val_dt%.txt

call curl.bat -XGET "http://%ES_HOSTPORT%/_nodes/hot_threads?threads=1000&type=wait&pretty" > hot_wait_%val_dt%.txt
call curl.bat -XGET "http://%ES_HOSTPORT%/_nodes/hot_threads?threads=1000&type=block&pretty" > hot_bulk_%val_dt%.txt

endlocal
