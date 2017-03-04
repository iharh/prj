2017-03-04 10:49:22,437  WARN [qtp996125997-23] (DispatcherServlet.java:1147) - No mapping found for HTTP request with URI [/gwt/sentiment_transfer/sentiment_transfer_service/uploadfile] in DispatcherServlet with name 'SentimentTransferSpringDispatcherServlet'

.web.export.ExportService
TODO: remove
    @SecureParameter(permission = PermissionName.READ, strategyClass = ClassName.SENTIMENTS)
    @RequestMapping(value = "latest_sentiment_exports", method = RequestMethod.POST)
    void downloadLatestSentimentExport(@RequestParam("projectId") long projectId, @RequestParam("exportId") String exportId, HttpServletResponse response);

gradle
    clean
    build
    war
    jettyRunWar
