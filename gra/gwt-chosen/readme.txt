gradle --stop
    rm -rf .gradle/

gradle
    clean
    build
    war
    jettyRunWar

curl http://localhost:8080/gwt/exporting/export/sampleExp
curl http://localhost:8080/gwt/recent_sentiment_exports_service

Loading inherited module 'mygwt.Example'
   Loading inherited module 'com.arcbees.chosen.Chosen'
      Loading inherited module 'com.arcbees.gsss.animation.Animation'
         [ERROR] The property CssResource.allowedFunctions must already exist as a configuration property
         [ERROR] Line 7: Unexpected exception while processing element 'extend-configuration-property'
com.google.gwt.core.ext.UnableToCompleteException: (see previous log entries)
