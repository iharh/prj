#! /usr/bin/env bash
run_dir=$(dirname $(readlink -f ${BASH_SOURCE[0]}))/..
echo run_dir: $run_dir
BUILD_PATH=$run_dir/build
JAR_NAME=`find $BUILD_PATH/libs -name "ing*.jar"`

JPDA_OPTS="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:4143"

export SPRING_RABBITMQ_HOST=$HOST
export SPRING_RABBITMQ_USERNAME=user
export SPRING_RABBITMQ_PASSWORD=password

java \
    -Dspring.profiles.active=development\
    -Dserver.max-http-header-size=65536\
    -jar $JAR_NAME

#    $JPDA_OPTS\
