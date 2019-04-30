#!/usr/bin/env bash

set -o errexit
set -o pipefail

SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

CB_JFT_IMAGE_NAME=jft-dev
CB_JFT_IMAGE_VERSION=latest
CB_JFT_IMAGE=$CB_JFT_IMAGE_NAME:$CB_JFT_IMAGE_VERSION
CB_JFT_IMAGE_BUILD_PATH=$SCRIPT_DIR

docker build \
	-t $CB_JFT_IMAGE \
	--build-arg uid=$(id -u) \
	$CB_JFT_IMAGE_BUILD_PATH

test -t 1 && USE_TTY='-it'

docker run \
    --rm \
    $USE_TTY \
    -v /data/wrk/repo/JFastText:/jft \
    -w /jft \
    $CB_JFT_IMAGE_NAME:$CB_JFT_IMAGE_VERSION \
    mvn $@
