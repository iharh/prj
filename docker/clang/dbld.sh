#! /bin/sh
FILES_CI_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )/filesci" && pwd )"

ADD_USER_FILE=$FILES_CI_DIR/add_host_user.sh
if ! [[ -f $ADD_USER_FILE ]]; then
  GUEST_USER_NAME=c1
  GUEST_GROUP_NAME=$GUEST_USER_NAME

  echo $ADD_USER_FILE does not exists - generating
  echo '#! /bin/sh' > $ADD_USER_FILE
  echo groupadd $GUEST_GROUP_NAME -f -g $(id -g) >> $ADD_USER_FILE
  echo useradd $GUEST_USER_NAME -l -u $(id -u) -g $(id -g) -G wheel >> $ADD_USER_FILE
fi

docker build -t iharh/clang-c1:0.1 .
