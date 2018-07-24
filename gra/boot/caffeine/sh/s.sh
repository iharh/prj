#! /usr/bin/env bash
run_dir=$(dirname $(readlink -f ${BASH_SOURCE[0]}))/..
BUILD_PATH=$run_dir/build
JAR_NAME=`find $BUILD_PATH/libs -name "caffeine*.jar"`
java -jar $JAR_NAME