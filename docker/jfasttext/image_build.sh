#!/usr/bin/env bash

set -o errexit
set -o pipefail

SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

CB_NLP_DEV_IMAGE_NAME=jft-dev
CB_NLP_DEV_IMAGE_VERSION=latest
CB_NLP_DEV_IMAGE=$CB_NLP_DEV_IMAGE_NAME:$CB_NLP_DEV_IMAGE_VERSION
CB_NLP_DEV_IMAGE_BUILD_PATH=$SCRIPT_DIR

docker build \
	-t $CB_NLP_DEV_IMAGE \
	--build-arg uid=$(id -u) \
	$CB_NLP_DEV_IMAGE_BUILD_PATH

test -t 1 && USE_TTY='-it'
