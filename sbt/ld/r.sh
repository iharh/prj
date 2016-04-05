#! /bin/bash
CUR_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
(cd $CUR_DIR\
    && rm -f *.log\
    && sbt "run-main ld.LD"\
)
#java %JAVA_OPTS% -jar "%~dp0target\dl4j-0.1-SNAPSHOT-one-jar.jar" %EXTRA_ARGS% %* > console.log 2>&1
