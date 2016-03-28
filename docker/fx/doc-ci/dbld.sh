#! /bin/bash
set -e
CUR_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
CTX_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )/.." && pwd )"
. $CTX_DIR/env-vars.sh
$CTX_DIR/dgen.sh
docker build -t $CUR_GROUP/$CUR_NAME_CI:$CUR_VER -f $CUR_DIR/Dockerfile $CTX_DIR
