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
