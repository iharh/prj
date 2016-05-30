#! /bin/bash
set -e

CTX_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )/.." && pwd )"
. $CTX_DIR/env-vars.sh

CMN_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )/../../../.." && pwd )"
CACHE_DIR=$CMN_DIR/docker-cache
CLB_BASE=$CMN_DIR/clb
SVN_BASE=$CLB_BASE/svnmain
GIT_BASE=$CLB_BASE/platform

RUN_CMD="$@"
if [[ -z "$RUN_CMD" ]]; then
  RUN_FLAGS="--rm -ti"
  RUN_PREF=
  RUN_CMD=/bin/bash
else
  RUN_PREF="/bin/bash -cl"
fi

USER_HOME=/home/$GUEST_USER_NAME

if [[ "${PWD:0:${#GIT_BASE}}" == "$GIT_BASE" ]]; then
    WRK_BASE=/cmp/$(realpath --relative-to=$GIT_BASE $PWD)
elif [[ "${PWD:0:${#SVN_BASE}}" == "$SVN_BASE" ]]; then
    WRK_BASE=/fx/$(realpath --relative-to=$SVN_BASE $PWD)
else
    echo Able to work only inside: $GIT_BASE or $SVN_BASE
    exit 1
fi

docker run $RUN_FLAGS\
 -v $CACHE_DIR/.gradle:$USER_HOME/.gradle\
 -v $SVN_BASE:/fx\
 -v $GIT_BASE:/cmp\
 -w $WRK_BASE\
 $CUR_GROUP/$CUR_NAME_CI:$CUR_VER\
 $RUN_PREF "$RUN_CMD"
