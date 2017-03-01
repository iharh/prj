curl 'http://localhost:8080/gwt/sentiment_transfer_service'\
    -H 'Origin: http://localhost:8080'\
    -H 'Accept-Encoding: gzip, deflate, br'\
    -H 'Accept-Language: en-US,en;q=0.8,ru;q=0.6'\
    -H 'X-GWT-Module-Base: http://localhost:8080/gwt/app/'\
    -H 'X-Requested-With: mygwt.foundation.client.csrf.CsrfRpcRequestBuilder'\
    -H 'Connection: keep-alive'\
    -H 'Cache-Control: no-cache'\
    -H 'Pragma: no-cache'\
    -H 'Content-Type: text/x-gwt-rpc; charset=UTF-8'\
    -H 'X-GWT-Permutation: 430BD1A07A055B2A6E546241F88C433C'\
    -H 'Referer: http://localhost:8080/gwt/'\
    --data-binary '7|0|5|http://localhost:8080/gwt/app/|4105B824758CFC8DFED85DCC0FF664D1|mygwt.common.client.service.SentimentImportService|cleanupSentimentsWithUploadedData|J|1|2|3|4|1|5|A|'\
    --compressed

    #-H 'Cookie: JSESSIONID=pbepgrqs3xrc9camqpltx3j9'\
#    -H 'User-Agent: Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36'\
    #-H 'Accept: */*'\
