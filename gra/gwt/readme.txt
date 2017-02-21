.web.server.SentimentImportServiceAction

gradle --stop
    rm -rf .gradle/

gradle
    clean
    build
    war
    jettyRunWar
