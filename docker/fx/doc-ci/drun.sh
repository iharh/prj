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
  #RUN_FLAGS=
  RUN_PREF="/bin/bash -cl"
fi
#echo RUN: $RUN_PREF "$RUN_CMD"
#echo RUN_FLAGS: $RUN_FLAGS
#echo RUN_PREF: $RUN_PREF
#echo RUN_CMD: $RUN_CMD

#WRK_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )/../.." && pwd )"
#CMN_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )/../../.." && pwd )"
#CACHE_DIR=$CMN_DIR/docker-cache

#USER_HOME=/home/$GUEST_USER_NAME

#-v $CACHE_DIR/.gradle:$USER_HOME/.gradle\
docker run $RUN_FLAGS\
 -v $(pwd):/prj\
 -w /prj\
 $CUR_GROUP/$CUR_NAME_CI:$CUR_VER\
 $RUN_PREF "$RUN_CMD"
# TODO: use $(realpath --relative-to=/data/wrk/svnmain $(pwd)
