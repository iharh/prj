#! /bin/bash
CUR_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
# StreamSpec
(cd $CUR_DIR\
    && rm -f *.log\
    && sbt "testOnly *SimpleTest"\
)
