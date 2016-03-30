#! /bin/bash
set -e

CTX_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )/.." && pwd )"
. $CTX_DIR/env-vars.sh

RUN_CMD="$@"
if [[ -z "$RUN_CMD" ]]; then
  RUN_FLAGS="--rm -ti"
  RUN_PREF=
  RUN_CMD=/bin/bash
else
  RUN_PREF="/bin/bash -cl"
fi

CMN_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )/../../../.." && pwd )"
CACHE_DIR=$CMN_DIR/docker-cache

USER_HOME=/home/$GUEST_USER_NAME

docker run $RUN_FLAGS\
 -v $CACHE_DIR/.gradle:$USER_HOME/.gradle\
 -v $(pwd):/prj\
 -w /prj\
 $CUR_GROUP/$CUR_NAME_CI:$CUR_VER\
 $RUN_PREF "$RUN_CMD"
