#! /usr/bin/env bash
run_dir=$(dirname $(readlink -f ${BASH_SOURCE[0]}))

DEV_IMAGE_NAME=dev-deb
DEV_IMAGE_VERSION=latest
DEV_IMAGE=$DEV_IMAGE_NAME:$DEV_IMAGE_VERSION
DEV_IMAGE_BUILD_PATH=$run_dir/$DEV_IMAGE_NAME

docker build -t $DEV_IMAGE --pull \
    --build-arg uid=$(id -u) \
    $DEV_IMAGE_BUILD_PATH
