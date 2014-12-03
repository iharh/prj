@echo off
setlocal
call vars.bat
:: TODO get rid of the copy&paste
call curl.bat -XGET "http://%ES_HOSTPORT%/%PRJ_INDEX%/document/_search?pretty" -d @agg_document.json > resp_document.json
call curl.bat -XGET "http://%ES_HOSTPORT%/%PRJ_INDEX%/verbatim/_search?pretty" -d @agg_verbatim.json > resp_verbatim.json
call curl.bat -XGET "http://%ES_HOSTPORT%/%PRJ_INDEX%/sentence/_search?pretty" -d @agg_sentence.json > resp_sentence.json
endlocal
