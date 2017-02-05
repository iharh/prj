#! /bin/bash
CUR_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
# StreamSpec
#&& cargo build\
(cd $CUR_DIR\
    && rm -f *.log\
    && cargo run
)
