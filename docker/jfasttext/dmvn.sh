#!/usr/bin/env bash

#!/usr/bin/env bash

set -o errexit
set -o pipefail

error() {
  local parent_lineno="$1"
  local message="$2"
  local code="${3:-1}"
  if [[ -n "$message" ]] ; then
    echo "Error on or near line ${parent_lineno}: ${message}; exiting with status ${code}"
  else
    echo "Error on or near line ${parent_lineno}; exiting with status ${code}"
  fi
  exit "${code}"
}
trap 'error ${LINENO}' ERR

SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

CB_NLP_DEV_IMAGE_NAME=cb-nlp-dev
CB_NLP_DEV_IMAGE_VERSION=latest
CB_NLP_DEV_IMAGE=$CB_NLP_DEV_IMAGE_NAME:$CB_NLP_DEV_IMAGE_VERSION
CB_NLP_DEV_IMAGE_BUILD_PATH=$SCRIPT_DIR/ci-pipeline/$CB_NLP_DEV_IMAGE_NAME

CB_JFT_IMAGE_NAME=jft-dev
CB_JFT_IMAGE_VERSION=latest
CB_JFT_IMAGE=$CB_JFT_IMAGE_NAME:$CB_JFT_IMAGE_VERSION
CB_JFT_IMAGE_BUILD_PATH=$SCRIPT_DIR/ci-pipeline/$CB_JFT_IMAGE_NAME

docker build \
	-t $CB_JFT_IMAGE \
	--build-arg uid=$(id -u) \
	$CB_JFT_IMAGE_BUILD_PATH

test -t 1 && USE_TTY='-it'

if [ $1 == '--shell' ]
then
    COMMAND=bash
else
    COMMAND="mvn $@"
fi

docker run \
    --rm \
    $USE_TTY \
    -v $SCRIPT_DIR:/jft \
    -w /jft \
    $CB_JFT_IMAGE_NAME:$CB_JFT_IMAGE_VERSION \
    $COMMAND
