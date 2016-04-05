#! /bin/bash
CUR_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
(cd $CUR_DIR\
    && rm -f *.log\
    && java -jar $CUR_DIR/target/ld-assembly-0.1-SNAPSHOT.jar ld.LD /data/wrk/clb/ld "$@"\
)
    #&& java -jar $CUR_DIR/target/ld-assembly-0.1-SNAPSHOT.jar ld.LD /data/wrk/clb/ld "$@"\
    #&& sbt "run ld.LD /data/wrk/clb/ld $@"\
    #&& sbt "run-main ld.LD"\
