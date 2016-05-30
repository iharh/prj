#! /bin/bash
set -e
CUR_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
. $CUR_DIR/env-vars.sh
FILESCI_DIR=$CUR_DIR/filesci

ADD_USER_FILE=$FILESCI_DIR/add_host_user.sh
if ! [[ -f $ADD_USER_FILE ]]; then
  GUEST_GROUP_NAME=$GUEST_USER_NAME

  echo $ADD_USER_FILE does not exists - generating
  echo '#! /bin/sh' > $ADD_USER_FILE
  echo groupadd $GUEST_GROUP_NAME -f -g $(id -g) >> $ADD_USER_FILE
  echo useradd $GUEST_USER_NAME -l -u $(id -u) -g $(id -g) -G wheel >> $ADD_USER_FILE
fi
