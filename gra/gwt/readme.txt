gradle --stop
    rm -rf .gradle/

gradle
    clean
    build
    war
    jettyRunWar

curl http://localhost:8080/gwt/exporting/export/sampleExp
curl http://localhost:8080/gwt/recent_sentiment_exports_service
