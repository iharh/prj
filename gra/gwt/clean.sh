#! /bin/bash
set -e
ORIGIN="http://localhost:8080"
URL_BASE="$ORIGIN/gwt"
# trailing slash is important
URL_APP="$URL_BASE/app/"
PERMUTATION="430BD1A07A055B2A6E546241F88C433C"
PACK_PREF="mygwt"

curl "$URL_BASE/sentiment_transfer_service"\
    -H "Origin: $ORIGIN"\
    -H 'Accept-Encoding: gzip, deflate, br'\
    -H "X-GWT-Module-Base: $URL_APP"\
    -H "X-Requested-With: $PACK_PREF.foundation.client.csrf.CsrfRpcRequestBuilder"\
    -H 'Connection: keep-alive'\
    -H 'Cache-Control: no-cache'\
    -H 'Pragma: no-cache'\
    -H 'Content-Type: text/x-gwt-rpc; charset=UTF-8'\
    -H "Referer: $URL_BASE/"\
    -H "X-GWT-Permutation: $PERMUTATION"\
    --data-binary "7|0|5|$URL_APP|4105B824758CFC8DFED85DCC0FF664D1|$PACK_PREF.common.client.service.SentimentImportService|cleanupSentimentsWithUploadedData|J|1|2|3|4|1|5|A|"\
    --compressed

#   -H 'Cookie: JSESSIONID=pbepgrqs3xrc9camqpltx3j9'\
#   -H 'User-Agent: Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36'\
#   -H 'Accept-Language: en-US,en;q=0.8,ru;q=0.6'\
#   -H 'Accept: */*'\
