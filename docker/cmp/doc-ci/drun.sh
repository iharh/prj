#! /bin/bash
set -e

CTX_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )/.." && pwd )"
. $CTX_DIR/env-vars.sh

#TODO: move to env-vars
SVN_BASE=/data/wrk/clb/svnmain

RUN_CMD="$@"
if [[ -z "$RUN_CMD" ]]; then
  RUN_FLAGS="--rm -ti"
  RUN_PREF=
  RUN_CMD=/bin/bash
else
  RUN_PREF="/bin/bash -cl"
fi

#WRK_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )/../.." && pwd )"
#CMN_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )/../../.." && pwd )"
#CACHE_DIR=$CMN_DIR/docker-cache

#USER_HOME=/home/$GUEST_USER_NAME

if [[ "${PWD:0:${#SVN_BASE}}" != "$SVN_BASE" ]]; then
    echo Able to work only inside: $SVN_BASE
    exit 1
fi

#-v $CACHE_DIR/.gradle:$USER_HOME/.gradle\
docker run $RUN_FLAGS\
 -v $SVN_BASE:/prj\
 -w /prj/$(realpath --relative-to=$SVN_BASE $PWD)\
 $CUR_GROUP/$CUR_NAME_CI:$CUR_VER\
 $RUN_PREF "$RUN_CMD"
