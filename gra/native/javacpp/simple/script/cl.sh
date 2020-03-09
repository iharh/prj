#! /usr/bin/env bash
run_dir=$(dirname $(readlink -f ${BASH_SOURCE[0]}))/..
java -Dorg.bytedeco.javacpp.logger=slf4j -Dorg.bytedeco.javacpp.logger.debug=true -jar $run_dir/cl/build/libs/cl-0.1.jar
