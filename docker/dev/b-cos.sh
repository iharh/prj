#! /usr/bin/env bash
run_dir=$(dirname $(readlink -f ${BASH_SOURCE[0]}))

IMAGE_NAME=cos
IMAGE_VERSION=latest

docker build -t dev-$IMAGE_NAME:$IMAGE_VERSION --pull \
    --build-arg uid=$(id -u) \
    $run_dir/$IMAGE_NAME
