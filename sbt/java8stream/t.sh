#! /bin/bash
CUR_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
(cd $CUR_DIR\
    && rm -f *.log\
    && sbt "testOnly *StreamSpec"\
)
